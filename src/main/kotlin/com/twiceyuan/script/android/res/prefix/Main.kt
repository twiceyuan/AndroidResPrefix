@file:Suppress("SameParameterValue")

package com.twiceyuan.script.android.res.prefix

import com.twiceyuan.script.android.res.prefix.bean.RenameResult
import com.twiceyuan.script.android.res.prefix.bean.RenamedMapping
import com.twiceyuan.script.android.res.prefix.bean.ResType
import com.twiceyuan.script.android.res.prefix.bean.getNameStylePrefix
import com.twiceyuan.script.android.res.prefix.files.getCodeByFlavor
import com.twiceyuan.script.android.res.prefix.files.getXmlFiles
import com.twiceyuan.script.android.res.prefix.handler.*
import com.twiceyuan.script.android.res.prefix.helper.AttrRenameHelper
import com.twiceyuan.script.android.res.prefix.helper.FileRenameHelper
import java.io.File
import java.util.*

val modulePaths = File("module_paths.txt")
    .readText()
    .split("\n")
    .filter { it.isNotBlank() }

val properties = Properties().apply {
    load(File("config.properties").inputStream())
}

val prefix: String = properties["prefix"] as String

val File.subFiles
    get() = listFiles()?.toList() ?: emptyList()

private val renameMapping = hashSetOf<RenamedMapping>()

fun main() {
    renameResInModule(prefix, modulePaths)
}

private fun renameResInModule(prefix: String, modulePaths: List<String>) {

    val modules = modulePaths.map { File(it) }

    ResType.values().forEach {
        renameResourceByType(prefix, it, modules)
    }
}

fun getFlavorDirs(modulePath: String): List<File> {
    val srcPath = File(modulePath, "src").absolutePath
    return File(srcPath)
        .subFiles
        .filter { "test" !in it.name.toLowerCase() }
}

/**
 * 重命名一类资源
 */
private fun renameResourceByType(prefix: String, resType: ResType, modules: List<File>) {
    val handler = getResTypeHandler(resType)

    // 重命名文件类型资源
    if (handler is FileResourceHandler) {
        for (module in modules) {
            // 获取所有的 drawable 文件
            for (file in handler.getResFiles(module.absolutePath)) {
                val resPrefix = handler.nameStyle().getNameStylePrefix(prefix)
                val result = FileRenameHelper.rename(file, resPrefix, module)
                if (result is RenameResult.Success) {
                    val mapping = RenamedMapping(resType, result.oldResName, result.newResName)
                    renameMapping.add(mapping)
                }
            }
        }
    }

    // 处理 attr 类型的资源
    if (handler is AttrResourceHandler) {
        for (module in modules) {
            for (file in handler.getAttrFiles(module.absolutePath)) {
                val resPrefix = handler.nameStyle().getNameStylePrefix(prefix)
                val results = AttrRenameHelper.renameAttrName(
                    file,
                    resPrefix,
                    handler.tagMatcher()
                )
                renameMapping.addAll(results
                    .filterIsInstance<RenameResult.Success>()
                    .map {
                        RenamedMapping(handler.resType, it.oldResName, it.newResName)
                    }
                )
            }
        }
    }

    // 处理资源引用
    modules.forEach { renameReferences(handler, it) }
}

private fun renameReferences(handler: ResTypeHandler, moduleFile: File) {
    // 改变代码中的引用
    for (flavorDir in getFlavorDirs(moduleFile.absolutePath)) {
        for (codeFile in getCodeByFlavor(flavorDir.absolutePath)) {
            var content = codeFile.readText()
            var isChanged = false

            for (mapping in renameMapping) {
                val oldValue = handler.codeComposer(mapping.oldName)
                val newValue = handler.codeComposer(mapping.newName)
                // 避免 android.R.xxx 也被匹配并且替换，排除 R.xxx 前面有 . 的情况
                val oldValueMatcher = Regex("(?<!android.)$oldValue")
                val matchResults = oldValueMatcher.findAll(content).toList()
                if (matchResults.isNotEmpty()) {
                    matchResults.forEach {
                        val oldFullValue = it.value
                        val newFullValue = it.value.replace(oldValue, newValue)
                        content = content.replace(oldFullValue, newFullValue)
                    }
                    isChanged = true
                }

                ExternalHandlers.extCodeHandler.forEach {
                    val newContent = it.handle(
                        flavorDir.name,
                        codeFile,
                        content,
                        handler.resType,
                        mapping.oldName,
                        mapping.newName
                    )
                    if (newContent != content) {
                        content = newContent
                        isChanged = true
                    }
                }
            }
            if (isChanged) {
                codeFile.writeText(content)
                println("[changed] ${codeFile.path}")
            }
        }
    }

    for (file in getXmlFiles(moduleFile.absolutePath)) {
        var content = file.readText()
        var isChanged = false
        for (mapping in renameMapping) {
            val oldValue = handler.xmlComposer(mapping.oldName)
            val newValue = handler.xmlComposer(mapping.newName)
            if (oldValue in content) {
                content = content.replace(oldValue, newValue)
                isChanged = true
            }
            for (extHandler in ExternalHandlers.extXmlHandler) {
                val newContent = extHandler.handle(
                    file,
                    content,
                    handler.resType,
                    mapping.oldName,
                    mapping.newName
                )
                if (content != newContent) {
                    content = newContent
                    isChanged = true
                }
            }
        }
        if (isChanged) {
            file.writeText(content)
            println("[changed] ${file.path}")
        }
    }
}
