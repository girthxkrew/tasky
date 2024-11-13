package com.rkm.tasky.feature.common

enum class Mode(isEditable: Boolean) {
    CREATE(true),
    VIEW(false),
    UPDATE(true),
}