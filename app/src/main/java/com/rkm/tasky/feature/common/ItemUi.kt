package com.rkm.tasky.feature.common

import androidx.compose.runtime.Stable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Stable
data class ItemUiState(
    val isEditable: Boolean = false,
    val showTimeDialog: Boolean = false,
    val showDateDialog: Boolean = false,
    val mode: Mode = Mode.CREATE
)

@Stable
data class ItemScreenActions(
    val onTimeClick: () -> Unit,
    val onDateClick: () -> Unit,
    val onSaveClick: () -> Unit,
    val onEditClick: () -> Unit,
    val onCloseClick: () -> Unit,
    val onDeleteClick: () -> Unit,
    val onEditField: (text: String, type: String) -> Unit,
    val updateDate: (LocalDate) -> Unit,
    val updateTime: (LocalTime) -> Unit
)
