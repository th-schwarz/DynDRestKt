package codes.thischwa.dyndrestkt.service

class ZoneSdkException : RuntimeException {
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}
    constructor(message: String?) : super(message) {}
    constructor(cause: Throwable?) : super(cause) {}
}