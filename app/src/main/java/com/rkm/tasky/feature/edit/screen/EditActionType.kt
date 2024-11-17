package com.rkm.tasky.feature.edit.screen

import androidx.annotation.StringRes
import com.rkm.tasky.R

enum class EditActionType(@StringRes val id: Int) {
    TITLE(R.string.edit_screen_toolbar_title),
    DESCRIPTION(R.string.edit_screen_toolbar_description)
}