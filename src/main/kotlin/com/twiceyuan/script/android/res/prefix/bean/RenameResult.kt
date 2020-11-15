package com.twiceyuan.script.android.res.prefix.bean

/**
 * 资源修改的结果
 */
sealed class RenameResult {
    class Success(
        val oldResName: String,
        val newResName: String
    ) : RenameResult()

    object Passed : RenameResult()
    object Failed : RenameResult()
}

