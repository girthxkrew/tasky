package com.rkm.tasky.feature.common

enum class Mode(val isEditable: Boolean) {
    CREATE(true),
    VIEW(false),
    UPDATE(true),
}