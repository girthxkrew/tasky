import okhttp3.Request

enum class RequestType {
    AUTHENTICATION,
    AUTHORIZATION;

    companion object {
        fun fromRequest(request: Request): RequestType =
            request.tag(RequestType::class.java) ?: AUTHENTICATION
    }
}