package com.rkm.tasky.feature.task.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rkm.tasky.feature.common.item.ItemScreenActions
import com.rkm.tasky.feature.common.item.ItemUiState
import com.rkm.tasky.feature.common.Mode
import com.rkm.tasky.feature.common.item.ReminderDropDownMenuActions
import com.rkm.tasky.feature.common.item.ReminderDropDownMenuUiState
import com.rkm.tasky.feature.task.viewmodel.TaskUiModel

@Preview(
    showSystemUi = true
)
@Composable
private fun PreviewNotEditableTaskScreen() {
    val reminder = TaskUiModel("Project X", desc = "This is a description")
    val state = ItemUiState()
    val actions = ItemScreenActions(
        onTimeClick = {},
        onDateClick = {},
        onSaveClick = {},
        onEditClick = {},
        onCloseClick = {},
        onDeleteClick = {},
        onEditField = {_, _ -> },
        updateDate = {},
        updateTime = {}
    )
    val ddActions = ReminderDropDownMenuActions({}, {})
    val ddState = ReminderDropDownMenuUiState()
    TaskScreen(
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
private fun PreviewEditableCreateTaskScreen() {
    val reminder = TaskUiModel("Project X", desc = "This is a description This is a description This is a description This is a description This is a description This is a description th")
    val state = ItemUiState(isEditable = true)
    val actions = ItemScreenActions(
        onTimeClick = {},
        onDateClick = {},
        onSaveClick = {},
        onEditClick = {},
        onCloseClick = {},
        onDeleteClick = {},
        onEditField = {_, _ -> },
        updateDate = {},
        updateTime = {}
    )
    val ddActions = ReminderDropDownMenuActions({}, {})
    val ddState = ReminderDropDownMenuUiState(isExpanded = true)
    TaskScreen(
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
private fun PreviewEditableUpdatedTaskScreen() {
    val reminder = TaskUiModel("Project X", desc = "This is a description This is a description This is a description This is a description This is a description This is a description th")
    val state = ItemUiState(isEditable = true, mode = Mode.UPDATE)
    val actions = ItemScreenActions(
        onTimeClick = {},
        onDateClick = {},
        onSaveClick = {},
        onEditClick = {},
        onCloseClick = {},
        onDeleteClick = {},
        onEditField = {_, _ -> },
        updateDate = {},
        updateTime = {}
    )
    val ddActions = ReminderDropDownMenuActions({}, {})
    val ddState = ReminderDropDownMenuUiState(isExpanded = true)
    TaskScreen(
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
private fun PreviewDateDialogTaskScreen() {
    val reminder = TaskUiModel("Project X", desc = "This is a description This is a description This is a description This is a description This is a description This is a description th")
    val state = ItemUiState(isEditable = true, showDateDialog = true)
    val actions = ItemScreenActions(
        onTimeClick = {},
        onDateClick = {},
        onSaveClick = {},
        onEditClick = {},
        onCloseClick = {},
        onDeleteClick = {},
        onEditField = {_, _ -> },
        updateDate = {},
        updateTime = {}
    )
    val ddActions = ReminderDropDownMenuActions({}, {})
    val ddState = ReminderDropDownMenuUiState()
    TaskScreen(
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
private fun PreviewTimeDialogTaskScreen() {
    val reminder = TaskUiModel("Project X", desc = "This is a description This is a description This is a description This is a description This is a description This is a description th")
    val state = ItemUiState(isEditable = true, showTimeDialog = true)
    val actions = ItemScreenActions(
        onTimeClick = {},
        onDateClick = {},
        onSaveClick = {},
        onEditClick = {},
        onCloseClick = {},
        onDeleteClick = {},
        onEditField = {_, _ -> },
        updateDate = {},
        updateTime = {}
    )
    val ddActions = ReminderDropDownMenuActions({}, {})
    val ddState = ReminderDropDownMenuUiState()
    TaskScreen(
        Modifier,
        reminder,
        state,
        ddState,
        actions,
        ddActions
    )
}