package com.twiceyuan.script.android.res.prefix.files

import com.twiceyuan.script.android.res.prefix.getFlavorDirs
import com.twiceyuan.script.android.res.prefix.modulePaths
import com.twiceyuan.script.android.res.prefix.subFiles
import java.io.File

private fun getAllFiles(dir: File, fileMatcher: File.() -> Boolean): List<File> {
    val codeFiles = mutableListOf<File>()

    dir.subFiles.forEach {
        if (it.fileMatcher()) {
            codeFiles.add(it)
        }
        if (it.isDirectory) {
            codeFiles.addAll(getAllFiles(it, fileMatcher))
        }
    }

    return codeFiles
}

/**
 * 获取 module 内的所有代码文件（仅包含 kotlin 和 java 文件）
 * [modulePath] Module 的根目录
 */
fun getCodeFiles(modulePath: String) = getFlavorDirs(modulePath)
    .flatMap {
        it.subFiles.filter { subFile ->
            subFile.name in arrayOf("java", "kotlin")
        }
    }
    .flatMap { getAllFiles(it, File::isCodeFile) }

fun getCodeByFlavor(flavorPath: String) = File(flavorPath).subFiles
    .filter { subFile -> subFile.name in arrayOf("java", "kotlin") }
    .flatMap { getAllFiles(it, File::isCodeFile) }

/**
 * 获取 module 下所有 xml 文件
 */
fun getXmlFiles(modulePath: String) = getFlavorDirs(modulePath)
    .flatMap {
        val mutableList = mutableListOf<File>()
        mutableList.addAll(File(it, "res").subFiles)
        mutableList.add(it) // 添加 flavor 目录本身，下面有 AndroidManifest 文件
        mutableList
    }
    .flatMap { getAllFiles(it, File::isXmlFile) }

fun File.isCodeFile(): Boolean = isFile && (name.endsWith(".kt") || name.endsWith(".java"))
fun File.isXmlFile(): Boolean = isFile && name.endsWith(".xml")
