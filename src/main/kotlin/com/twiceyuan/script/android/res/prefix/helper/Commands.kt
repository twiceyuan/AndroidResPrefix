package com.twiceyuan.script.android.res.prefix.helper

import java.io.File


/**
 * [commands] 执行命令
 * [dir] 执行路径
 * @return 是否成功
 */
fun runCommand(commands: String, dir: File? = null): Boolean {

    val process = Runtime.getRuntime().exec(commands.split(" ").toTypedArray(), null, dir)
    val bufferedReader = process.inputStream.bufferedReader()

    var line: String?
    while (bufferedReader.readLine().also { line = it } != null) {
        println(line) // stdout
    }

    // return process return code
    val returnCode = process.waitFor()
    return returnCode == 0
}