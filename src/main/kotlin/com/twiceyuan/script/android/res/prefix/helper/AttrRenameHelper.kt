package com.twiceyuan.script.android.res.prefix.helper

import com.twiceyuan.script.android.res.prefix.bean.RenameResult
import java.io.File

object AttrRenameHelper {

    fun renameAttrName(attrFile: File, prefix: String, tagMatcher: Regex): List<RenameResult> {
        var content = attrFile.readText()
        var isChanged = false
        val results = mutableListOf<RenameResult>()
        val nameMatcher = Regex("name=\"(.*?)\"")
        tagMatcher.findAll(content).forEach { tagResult ->
            val result = nameMatcher.find(tagResult.value)?.groups ?: return@forEach
            val nameDefinition = result[0]?.value ?: return@forEach
            val oldName = result[1]?.value ?: return@forEach
            // 命名符合规则的跳过
            if (oldName.startsWith(prefix)) {
                return@forEach
            }
            val newName = prefix + oldName
            val newNameDefinition = nameDefinition.replace(oldName, newName)
            content = content.replace(nameDefinition, newNameDefinition)
            results.add(RenameResult.Success(oldName, newName))
            isChanged = true
        }

        if (isChanged) {
            attrFile.writeText(content)
        }

        return results
    }
}
