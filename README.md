# AndroidResPrefix

批量添加 Android 的资源文件前缀，并替换其在代码和其他资源文件中的引用，在重命名文件时保留对原有文件的引用（使用 git mv）。目前支持的资源类型：

```kotlin
enum class ResType {
    /**
     * Drawable 资源，包含图片文件、xml 类 drawable 文件和 values 中的 <drawable> 定义
     */
    Drawable,

    /**
     * MipMap 资源，包含图片文件、xml 类 mipmap 文件
     */
    MipMap,

    /**
     * 布局文件
     */
    Layout,

    /**
     * 字符串资源
     */
    String,

    /**
     * 字符数组资源
     */
    StringArray,

    /**
     * 量词字符串资源
     */
    StringPlurals,

    /**
     * dimen 定义
     */
    Dimension,

    /**
     * Style 定义
     */
    Style,

    /**
     * Anim 资源
     */
    Animation,

    /**
     * Menu 资源
     */
    Menu,

    /**
     * 颜色资源
     */
    Color,
}
```

暂不打算支持的：

- declare-styleable 属性
- 自定义配置 xml 文件

## 用法

1. clone 项目
2. 在项目根目录新建文件 `module_paths.txt` 配置想要替换资源的 module 目录，每个 module 占一行；
3. 在项目根目录新建文件 `config.properties` 添加属性 `prefix` 配置资源前缀；
4. 运行 Main 函数

## TODO

- 支持 cli 方式使用
 
