package com.rkm.tasky.feature.common.item

import com.rkm.tasky.util.result.Error

interface ItemError: Error {

    enum class UiError: Error {
        NO_TITLE,
        UNKNOWN_ERROR
    }
}