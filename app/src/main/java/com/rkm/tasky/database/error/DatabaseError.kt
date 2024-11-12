package com.rkm.tasky.database.error

import com.rkm.tasky.util.result.Error

sealed interface DatabaseError: Error {
    enum class ItemError: Error {
        ITEM_DOES_NOT_EXIST
    }
}