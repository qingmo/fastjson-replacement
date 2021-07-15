package io.github.qingmo.json

interface JSONLexer {
    companion object {
        const val EOI = 0x1A.toChar()
        const val LITERAL_ISO8601_DATE = 5
    }
}