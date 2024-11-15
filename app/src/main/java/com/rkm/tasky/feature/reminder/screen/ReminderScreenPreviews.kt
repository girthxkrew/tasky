package com.rkm.tasky.feature.reminder.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rkm.tasky.feature.reminder.viewmodel.ReminderUiModel
import com.rkm.tasky.ui.component.ReminderDropDownMenuActions
import com.rkm.tasky.ui.component.ReminderDropDownMenuUiState

@Preview(
    showSystemUi = true
)
@Composable
private fun PreviewNotEditableReminderScreen() {
    val reminder = ReminderUiModel("Project X", desc = "This is a description")
    val state = ItemUiState()
    val actions = ItemScreenActions(
        onTimeClick = {},
        onDateClick = {},
        onSaveClick = {},
        onEditClick = {},
        onCloseClick = {},
        onDeleteClick = {},
        onEditField = {},
        updateDate = {},
        updateTime = {}
    )
    val ddActions = ReminderDropDownMenuActions({}, {})
    val ddState = ReminderDropDownMenuUiState()
    ReminderScreen(
        Modifier,
        reminder,
        state,
        ddState,
        actions,
        ddActions
    )
}

@Preview(
    showSystemUi = true
)
@Composable
private fun PreviewEditableReminderScreen() {
    val reminder = ReminderUiModel("Project X", desc = "This is a description This is a description This is a description This is a description This is a description This is a description th")
    val state = ItemUiState(isEditable = true)
    val actions = ItemScreenActions(
        onTimeClick = {},
        onDateClick = {},
        onSaveClick = {},
        onEditClick = {},
        onCloseClick = {},
        onDeleteClick = {},
        onEditField = {},
        updateDate = {},
        updateTime = {}
    )
    val ddActions = ReminderDropDownMenuActions({}, {})
    val ddState = ReminderDropDownMenuUiState(isExpanded = true)
    ReminderScreen(
        Modifier,
        reminder,
        state,
        ddState,
        actions,
        ddActions
    )
}

@Preview(
    showSystemUi = true
)
@Composable
private fun PreviewDateDialogReminderScreen() {
    val reminder = ReminderUiModel("Project X", desc = "This is a description This is a description This is a description This is a description This is a description This is a description th")
    val state = ItemUiState(isEditable = true, showDateDialog = true)
    val actions = ItemScreenActions(
        onTimeClick = {},
        onDateClick = {},
        onSaveClick = {},
        onEditClick = {},
        onCloseClick = {},
        onDeleteClick = {},
        onEditField = {},
        updateDate = {},
        updateTime = {}
    )
    val ddActions = ReminderDropDownMenuActions({}, {})
    val ddState = ReminderDropDownMenuUiState()
    ReminderScreen(
        Modifier,
        reminder,
        state,
        ddState,
        actions,
        ddActions
    )
}
@Preview(
    showSystemUi = true
)
@Composable
private fun PreviewTimeDialogReminderScreen() {
    val reminder = ReminderUiModel("Project X", desc = "This is a description This is a description This is a description This is a description This is a description This is a description th")
    val state = ItemUiState(isEditable = true, showTimeDialog = true)
    val actions = ItemScreenActions(
        onTimeClick = {},
        onDateClick = {},
        onSaveClick = {},
        onEditClick = {},
        onCloseClick = {},
        onDeleteClick = {},
        onEditField = {},
        updateDate = {},
        updateTime = {}
    )
    val ddActions = ReminderDropDownMenuActions({}, {})
    val ddState = ReminderDropDownMenuUiState()
    ReminderScreen(
        Modifier,
        reminder,
        state,
        ddState,
        actions,
        ddActions
    )
}