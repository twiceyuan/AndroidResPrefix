package com.twiceyuan.script.android.res.prefix.handler

import com.twiceyuan.script.android.res.prefix.bean.NameStyle
import com.twiceyuan.script.android.res.prefix.bean.ResType
import java.io.File


interface ResTypeHandler {

    /**
     * 资源类型
     */
    val resType: ResType

    /**
     * 前缀的命名风格，默认是下划线分割命名
     */
    fun nameStyle(): NameStyle = NameStyle.UnderScoreStyle

    /**
     * 代码中引用匹配器
     */
    fun codeComposer(resName: String): String

    /**
     * XML 资源中引用匹配器
     */
    fun xmlComposer(resName: String): String
}

interface FileResourceHandler {
    /**
     * 当资源是文件类型时（文件名代表资源名称），获取 module 所有该类型资源文件
     */
    fun getResFiles(modulePath: String): List<File>
}

interface AttrResourceHandler : ResTypeHandler {

    /**
     * 获取 module 下所有可能存储该 attr 的文件
     */
    fun getAttrFiles(modulePath: String): List<File>

    /**
     * 匹配 attr 在 xml 中定义的 tag
     */
    fun tagMatcher(): Regex
}

/**
 * 引用修改，外部扩展。用于一些不符合常理的引用，例如 Kotlin Android Extension 生成的布局对象
 */
interface CodeRefExtHandler {
    fun handle(
        flavorName: String,
        codeFile: File,
        content: String,
        resType: ResType,
        oldValue: String,
        newValue: String
    ): String
}

interface XmlRefExtHandler {
    fun handle(file: File, content: String, resType: ResType, oldName: String, newName: String): String
}