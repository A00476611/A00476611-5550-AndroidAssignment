package com.example.hotels

fun String.isBlank():Boolean {
        return this == ""
}
fun String.isEmail():Boolean {
    return this.matches("""\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*""".toRegex())
}

fun String.isName():Boolean {
    return this.matches("""^[a-zA-Z]+(?:[\s'-][a-zA-Z]+)*${'$'}""".toRegex())
}