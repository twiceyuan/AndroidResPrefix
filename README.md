# AndroidResPrefix

批量添加 Android 项目的资源文件前缀，并替换其在代码和其他资源文件中的引用，以解决配置了 `resourcePrefix` 后的 lint 检查的 `Resource with Wrong Prefix` 问题。在重命名文件时保留对原有文件的引用，需要环境安装了 git。

目前支持的资源类型：[ResType.kt](src/main/kotlin/com/twiceyuan/script/android/res/prefix/bean/ResType.kt)

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
- 优化过程输出
