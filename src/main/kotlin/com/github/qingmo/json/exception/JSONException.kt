package com.github.qingmo.json.exception

class JSONException : RuntimeException {
    constructor() : super() {}
    constructor(message: String?) : super(message) {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}
    constructor(cause: Throwable?) : super(cause!!.message, cause) {}
}