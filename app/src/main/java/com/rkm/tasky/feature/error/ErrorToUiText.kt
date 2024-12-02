package com.rkm.tasky.feature.error

import com.rkm.tasky.R
import com.rkm.tasky.feature.common.item.ItemError
import com.rkm.tasky.network.util.NetworkError

fun errorToUiMessage(error: NetworkError.APIError): Int {
    return when(error) {
        NetworkError.APIError.NO_INTERNET -> R.string.api_error_no_internet
        NetworkError.APIError.REQUEST_TIMEOUT ->  R.string.api_error_request_timeout
        NetworkError.APIError.TOO_MANY_REQUESTS ->  R.string.api_error_too_many_requests
        NetworkError.APIError.CONNECTION_TIMEOUT -> R.string.api_error_connection_timeout
        NetworkError.APIError.PAYLOAD_TOO_LARGE ->  R.string.api_error_payload_too_large
        NetworkError.APIError.UNAUTHORIZED ->  R.string.api_error_unauthorized
        NetworkError.APIError.FORBIDDEN ->  R.string.api_error_forbidden
        NetworkError.APIError.NOT_FOUND ->  R.string.api_error_not_found
        NetworkError.APIError.SERVER_ERROR ->  R.string.api_error_server_error
        NetworkError.APIError.SERIALIZATION ->  R.string.api_error_serialization
        NetworkError.APIError.UNKNOWN ->  R.string.api_error_unknown
        NetworkError.APIError.NO_SESSION_INFO ->  R.string.api_error_no_session_info
        NetworkError.APIError.RETRY -> R.string.api_error_retry
        NetworkError.APIError.UPLOAD_FAILED -> R.string.api_error_upload_failed
    }
}

fun errorToUiMessage(error: ItemError.UiError): Int {
    return when (error) {
        ItemError.UiError.NO_TITLE -> R.string.item_detail_no_title_error
        ItemError.UiError.UNKNOWN_ERROR -> R.string.item_detail_unknown_error
    }
}