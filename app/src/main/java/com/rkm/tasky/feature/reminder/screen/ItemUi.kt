package com.rkm.tasky.feature.reminder.screen

import androidx.compose.runtime.Stable
import com.rkm.tasky.navigation.EditActionType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Stable
data class ItemUiState(
    val isEditable: Boolean = false,
    val showTimeDialog: Boolean = false,
    val showDateDialog: Boolean = false
)

@Stable
data class ItemScreenActions(
    val onTimeClick: () -> Unit,
    val onDateClick: () -> Unit,
    val onSaveClick: () -> Unit,
    val onEditClick: () -> Unit,
    val onCloseClick: () -> Unit,
    val onDeleteClick: () -> Unit,
    val onEditField: (EditActionType) -> Unit,
    val updateDate: (LocalDate) -> Unit,
    val updateTime: (LocalTime) -> Unit
)
