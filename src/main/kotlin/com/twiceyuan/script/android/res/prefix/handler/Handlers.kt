package com.twiceyuan.script.android.res.prefix.handler

import com.twiceyuan.script.android.res.prefix.bean.NameStyle
import com.twiceyuan.script.android.res.prefix.bean.ResType
import com.twiceyuan.script.android.res.prefix.getFlavorDirs
import com.twiceyuan.script.android.res.prefix.subFiles
import java.io.File

/**
 * res 类型映射到 handler
 */
fun getResTypeHandler(resType: ResType): ResTypeHandler {
    return when (resType) {
        ResType.Drawable -> DrawableHandler
        ResType.Layout -> LayoutHandler
        ResType.MipMap -> MipMapHandler
        ResType.String -> StringHandler
        ResType.StringArray -> StringArrayHandler
        ResType.StringPlurals -> StringPluralsHandler
        ResType.Dimension -> DimensionHandler
        ResType.Style -> StyleHandler
        ResType.Animation -> AnimationHandler
        ResType.Menu -> MenuHandler
        ResType.Color -> ColorHandler
    }
}

object DrawableHandler : ResTypeHandler, FileResourceHandler, AttrResourceHandler {
    override fun getAttrFiles(modulePath: String): List<File> = getValuesDirs(modulePath)
    override fun tagMatcher(): Regex = tagMatcher("drawable")
    override val resType: ResType = ResType.Drawable
    override fun codeComposer(resName: String): String = "R.drawable.$resName"
    override fun xmlComposer(resName: String): String = "@drawable/$resName"

    override fun getResFiles(modulePath: String): List<File> = getFlavorDirs(modulePath)
        .map { File(it, "res") }
        .flatMap { it.subFiles.filter { resDir -> resDir.name.startsWith("drawable") } }
        .flatMap { it.subFiles }
}

object MipMapHandler : ResTypeHandler, FileResourceHandler {

    override val resType: ResType = ResType.Drawable
    override fun codeComposer(resName: String): String = "R.mipmap.$resName"
    override fun xmlComposer(resName: String): String = "@mipmap/$resName"

    override fun getResFiles(modulePath: String): List<File> = getFlavorDirs(modulePath)
        .map { File(it, "res") }
        .flatMap { it.subFiles.filter { resDir -> resDir.name.startsWith("mipmap") } }
        .flatMap { it.subFiles }
}

object LayoutHandler : ResTypeHandler, FileResourceHandler {

    override val resType: ResType = ResType.Layout
    override fun codeComposer(resName: String): String = "R.layout.$resName"
    override fun xmlComposer(resName: String): String = "@layout/$resName"

    override fun getResFiles(modulePath: String): List<File> = getFlavorDirs(modulePath)
        .map { File(it, "res") }
        .flatMap { it.subFiles.filter { resDir -> resDir.name == "layout" } }
        .flatMap { it.subFiles }
}

object StringHandler : ResTypeHandler, AttrResourceHandler {

    override val resType: ResType = ResType.String
    override fun tagMatcher(): Regex = tagMatcher("string")
    override fun codeComposer(resName: String): String = "R.string.$resName"
    override fun xmlComposer(resName: String): String = "@string/$resName"

    override fun getAttrFiles(modulePath: String): List<File> {
        return getValuesDirs(modulePath)
    }
}

object StringArrayHandler : ResTypeHandler, AttrResourceHandler {

    override val resType: ResType = ResType.StringArray
    override fun tagMatcher(): Regex = tagMatcher("string-array")
    override fun codeComposer(resName: String): String = "R.array.$resName"
    override fun xmlComposer(resName: String): String = "@array/$resName"

    override fun getAttrFiles(modulePath: String): List<File> {
        return getValuesDirs(modulePath)
    }
}

object StringPluralsHandler : ResTypeHandler, AttrResourceHandler {

    override val resType: ResType = ResType.StringPlurals
    override fun tagMatcher(): Regex = tagMatcher("plurals")
    override fun codeComposer(resName: String): String = "R.plurals.$resName"
    override fun xmlComposer(resName: String): String = "@plurals/$resName"

    override fun getAttrFiles(modulePath: String): List<File> {
        return getValuesDirs(modulePath)
    }
}

object DimensionHandler : ResTypeHandler, AttrResourceHandler {

    override val resType: ResType = ResType.Dimension
    override fun tagMatcher(): Regex = tagMatcher("dimen")
    override fun codeComposer(resName: String): String = "R.dimen.$resName"
    override fun xmlComposer(resName: String): String = "@dimen/$resName"

    override fun getAttrFiles(modulePath: String): List<File> {
        return getValuesDirs(modulePath)
    }
}

object StyleHandler : ResTypeHandler, AttrResourceHandler {
    override val resType: ResType = ResType.Style
    override fun nameStyle(): NameStyle = NameStyle.UpperCamelStyle
    override fun tagMatcher(): Regex = tagMatcher("style")
    override fun codeComposer(resName: String): String = "R.style.$resName"
    override fun xmlComposer(resName: String): String = "@style/$resName"

    override fun getAttrFiles(modulePath: String): List<File> {
        return getValuesDirs(modulePath)
    }
}

object AnimationHandler : ResTypeHandler, AttrResourceHandler {
    override val resType: ResType = ResType.Animation
    override fun tagMatcher(): Regex = tagMatcher("anim")
    override fun codeComposer(resName: String): String = "R.anim.$resName"
    override fun xmlComposer(resName: String): String = "@anim/$resName"

    override fun getAttrFiles(modulePath: String): List<File> {
        return getFlavorDirs(modulePath)
            .map { File(it, "res") }
            .flatMap { it.subFiles.filter { resDir -> resDir.name.startsWith("anim") } }
            .flatMap { it.subFiles }
            .filter { it.name.endsWith(".xml") }
    }
}

object MenuHandler : ResTypeHandler, FileResourceHandler {
    override val resType: ResType = ResType.Menu
    override fun codeComposer(resName: String): String = "R.menu.$resName"
    override fun xmlComposer(resName: String): String = "@menu/$resName"

    override fun getResFiles(modulePath: String): List<File> = getFlavorDirs(modulePath)
        .map { File(it, "res") }
        .flatMap { it.subFiles.filter { resDir -> resDir.name == "menu" } }
        .flatMap { it.subFiles }
}

object ColorHandler : ResTypeHandler, FileResourceHandler, AttrResourceHandler {

    override fun getAttrFiles(modulePath: String): List<File> = getValuesDirs(modulePath)
    override fun tagMatcher(): Regex = tagMatcher("color")

    override val resType: ResType = ResType.Color
    override fun codeComposer(resName: String): String = "R.color.$resName"
    override fun xmlComposer(resName: String): String = "@color/$resName"

    override fun getResFiles(modulePath: String): List<File> {
        return getFlavorDirs(modulePath)
            .map { File(it, "res") }
            .flatMap { it.subFiles.filter { resDir -> resDir.name == "color" } }
            .flatMap { it.subFiles }
    }
}

private fun getValuesDirs(modulePath: String): List<File> {
    return getFlavorDirs(modulePath)
        .map { File(it, "res") }
        .flatMap { it.subFiles.filter { resDir -> resDir.name.startsWith("value") } }
        .flatMap { it.subFiles }
        .filter { it.name.endsWith(".xml") }
}

/**
 * tag 匹配器构造
 */
private fun tagMatcher(tag: String): Regex = Regex("<$tag [\\s\\S]+?>[\\s\\S]+?</$tag>")

object KotlinSyntheticRefHandlerCode : CodeRefExtHandler {

    override fun handle(
        flavorName: String,
        codeFile: File,
        content: String,
        resType: ResType,
        oldValue: String,
        newValue: String
    ): String {
        if (!codeFile.name.endsWith(".kt") || resType != ResType.Layout) {
            return content
        }
        var newContent = content
        val oldRefValue = "kotlinx.android.synthetic.$flavorName.$oldValue."
        val newRefValue = "kotlinx.android.synthetic.$flavorName.$newValue."

        if (oldValue in content) {
            newContent = newContent.replace(oldRefValue, newRefValue)
        }

        return newContent
    }
}

object StyleParentRefHandler : XmlRefExtHandler {

    override fun handle(file: File, content: String, resType: ResType, oldName: String, newName: String): String {
        if (resType != ResType.Style) {
            return content
        }

        val oldRefValue = "parent=\"$oldName\""
        val newRefValue = "parent=\"$newName\""
        return content.replace(oldRefValue, newRefValue)
    }
}

object ExternalHandlers {
    // 暂时写死，视情况需要方便后期扩展
    val extCodeHandler = listOf<CodeRefExtHandler>(KotlinSyntheticRefHandlerCode)
    val extXmlHandler = listOf<XmlRefExtHandler>(StyleParentRefHandler)
}
