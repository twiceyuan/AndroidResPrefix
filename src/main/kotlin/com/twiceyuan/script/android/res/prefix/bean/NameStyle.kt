package com.twiceyuan.script.android.res.prefix.bean

import com.twiceyuan.script.android.res.prefix.prefix

enum class NameStyle {
    /**
     * 大驼峰命名
     */
    UpperCamelStyle,

    /**
     * 下划线分割命名（默认）
     */
    UnderScoreStyle
}

fun NameStyle.getNameStylePrefix(prefix: String) = when (this) {
    NameStyle.UnderScoreStyle -> {
        prefix
    }
    NameStyle.UpperCamelStyle -> {
        upperCamelStylePrefix
    }
}

val upperCamelStylePrefix by lazy {
    prefix.toUpperCamelStyle()
}

private fun String.toUpperCamelStyle(): String {
    val words = split("_").filter { it.isNotBlank() }
    return words.joinToString("") { it.capitalize() }
}
