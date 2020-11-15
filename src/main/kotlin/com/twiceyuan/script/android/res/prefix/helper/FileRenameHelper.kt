package com.twiceyuan.script.android.res.prefix.helper

import com.twiceyuan.script.android.res.prefix.bean.RenameResult
import java.io.File


object FileRenameHelper {

    fun rename(
        oldFile: File,
        prefix: String,
        moduleFile: File
    ): RenameResult {
        // 如果文件已经以前缀命名，则跳过该文件
        if (oldFile.name.startsWith(prefix)) {
            return RenameResult.Passed
        }
        val newName = prefix + oldFile.name
        val newFile = File(oldFile.parent, newName)

        return if (renameFile(oldFile, newFile, moduleFile)) {
            val oldResName = oldFile.nameWithoutExtension
            val newResName = newFile.nameWithoutExtension
            RenameResult.Success(oldResName, newResName)
        } else {
            RenameResult.Failed
        }
    }

    /**
     * 使用 git mv 重命名文件，没有使用 File API 是因为需要保留 git 记录
     */
    private fun renameFile(oldFile: File, newFile: File, dir: File): Boolean {
        val cmd = "git mv ${oldFile.absolutePath} ${newFile.absolutePath}"
        return runCommand(cmd, dir)
    }
}

