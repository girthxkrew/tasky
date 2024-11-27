
package com.rkm.tasky.feature.reminder.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rkm.tasky.R
import com.rkm.tasky.feature.common.item.ItemScreenActions
import com.rkm.tasky.feature.common.item.ItemUiState
import com.rkm.tasky.feature.common.Mode
import com.rkm.tasky.feature.common.AgendaItemType
import com.rkm.tasky.feature.edit.screen.EditActionType
import com.rkm.tasky.feature.reminder.viewmodel.ReminderUiModel
import com.rkm.tasky.feature.reminder.viewmodel.ReminderViewModel
import com.rkm.tasky.feature.snackbar.SnackBarController
import com.rkm.tasky.feature.snackbar.SnackBarEvent
import com.rkm.tasky.ui.component.DatePickerModal
import com.rkm.tasky.feature.common.item.ItemDetailBody
import com.rkm.tasky.feature.common.item.ItemDetailDateTimePicker
import com.rkm.tasky.feature.common.item.ItemDetailDeleteTextBox
import com.rkm.tasky.feature.common.item.ItemDetailDescription
import com.rkm.tasky.feature.common.item.ItemDetailDivider
import com.rkm.tasky.feature.common.item.ItemDetailTitle
import com.rkm.tasky.feature.common.item.ItemDetailTopBar
import com.rkm.tasky.feature.common.item.ItemDetailTypeTitle
import com.rkm.tasky.feature.common.item.ItemUiEvent
import com.rkm.tasky.feature.common.item.ReminderDropDownMenu
import com.rkm.tasky.feature.common.item.ReminderDropDownMenuActions
import com.rkm.tasky.feature.common.item.ReminderDropDownMenuUiState
import com.rkm.tasky.ui.component.TimePickerModal
import com.rkm.tasky.ui.event.ObserveAsEvents
import com.rkm.tasky.ui.theme.ReminderColorId
import com.rkm.tasky.ui.theme.ReminderSquareStroke
import com.rkm.tasky.util.date.toUiString
import kotlinx.coroutines.launch

data class ReminderScreenEvents(
    val onNavigateBack: () -> Unit,
    val onEditField: (text: String, action: String) -> Unit
)

@Composable
fun ReminderScreenRoot(
    modifier: Modifier,
    viewModel: ReminderViewModel = hiltViewModel(),
    events: ReminderScreenEvents,
    title: String?,
    desc: String?
) {
    val itemUiState by viewModel.itemUiState.collectAsStateWithLifecycle()
    val reminder by viewModel.reminder.collectAsStateWithLifecycle()
    val dropDownState by viewModel.dropDownState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val createdString = stringResource(R.string.item_detail_create_success, stringResource(R.string.reminder))
    val updatedString = stringResource(R.string.item_detail_update_success, stringResource(R.string.reminder))
    val deletedString = stringResource(R.string.item_detail_delete_success, stringResource(R.string.reminder))

    val dropDownActions = ReminderDropDownMenuActions(
        onExpanded = viewModel::onExpanded,
        onSelectedDuration = viewModel::onSelectedDuration
    )

    val actions = ItemScreenActions(
        onTimeClick = viewModel::showTimeDialog,
        onDateClick = viewModel::showDateDialog,
        onSaveClick = viewModel::onSave,
        onEditClick = viewModel::onEdit,
        onCloseClick = events.onNavigateBack,
        onDeleteClick = viewModel::onDelete,
        onEditField = events.onEditField,
        updateDate = viewModel::updateDate,
        updateTime = viewModel::updateTime
    )
    ReminderScreen(
        modifier = modifier,
        reminder = reminder,
        state = itemUiState,
        dropDownMenuUiState = dropDownState,
        actions = actions,
        dropDownActions = dropDownActions
    )

    LaunchedEffect(
        title
    ) {
        if(title != null) {
            viewModel.onUpdateTitle(title)
        }
    }

    LaunchedEffect(
        desc
    ) {
        if(desc != null) {
            viewModel.onUpdateDescription(desc)
        }
    }

    ObserveAsEvents(viewModel.reminderScreenEventChannel) { event ->
        scope.launch {
            when (event) {
                is ItemUiEvent.ItemErrorEvent -> {
                    val message = event.message.asString(context)
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            message = message
                        )
                    )
                }

                ItemUiEvent.ItemCreateEvent -> {
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            message = createdString
                        )
                    )
                }

                ItemUiEvent.ItemDeleteEvent -> {
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            message = updatedString
                        )
                    )
                }

                ItemUiEvent.ItemUpdateEvent -> {
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            message = deletedString
                        )
                    )
                }
            }
        }
    }
}


@Composable
internal fun ReminderScreen(
    modifier: Modifier,
    reminder: ReminderUiModel,
    state: ItemUiState,
    dropDownMenuUiState: ReminderDropDownMenuUiState,
    actions: ItemScreenActions,
    dropDownActions: ReminderDropDownMenuActions
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {

        if(state.showDateDialog) {
            DatePickerModal(
                selectedDate = reminder.date,
                onDateSelected = actions.updateDate,
                onDismiss = actions.onDateClick
            )
        }

        if(state.showTimeDialog) {
            TimePickerModal(
                selectedTime = reminder.time,
                onTimeSelected = actions.updateTime,
                onDismiss = actions.onTimeClick
            )
        }


        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            ItemDetailTopBar(
                selectedDate = reminder.date.toUiString(),
                type = AgendaItemType.REMINDER,
                isEditable = state.isEditable,
                onCloseClick = actions.onCloseClick,
                onEditClick = actions.onEditClick,
                onSaveClick = actions.onSaveClick
            )

            ItemDetailBody(
                modifier = Modifier.weight(1f)
            ) {

                ItemDetailTypeTitle(
                    modifier = Modifier.padding(top = 24.dp, start = 24.dp),
                    rectangleColor = ReminderColorId,
                    rectangleOutlineColor = ReminderSquareStroke,
                    type = AgendaItemType.REMINDER
                )

                ItemDetailTitle(
                    modifier = Modifier
                        .padding(top = 24.dp, start = 24.dp, end = 24.dp)
                        .fillMaxWidth(),
                    isEditable = state.isEditable,
                    title = reminder.title,
                    onEditClick = { actions.onEditField(reminder.title, EditActionType.TITLE.name) }
                )

                ItemDetailDivider(modifier = Modifier.padding(top = 8.dp, start = 24.dp, end = 24.dp))

                ItemDetailDescription(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 24.dp, end = 24.dp)
                        .fillMaxWidth()
                        .height(75.dp),
                    isEditable = state.isEditable,
                    desc = reminder.desc,
                    onEditClick = { actions.onEditField(reminder.desc, EditActionType.DESCRIPTION.name) }
                )

                ItemDetailDivider(modifier = Modifier.padding(top = 8.dp, start = 24.dp, end = 24.dp))

                ItemDetailDateTimePicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(top = 16.dp, start = 24.dp, end = 24.dp),
                    isEditable = state.isEditable,
                    time = reminder.time.toUiString(),
                    date = reminder.date.toUiString(),
                    onTimeClick = { actions.onTimeClick() },
                    onDateClick = { actions.onDateClick() }
                )

                ItemDetailDivider(modifier = Modifier.padding(top = 8.dp, start = 24.dp, end = 24.dp))

                ReminderDropDownMenu(
                    dropDownMenuUiState,
                    dropDownActions,
                    state.isEditable,
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 24.dp, end = 24.dp)
                )

                if(state.mode == Mode.UPDATE) {
                    Spacer(Modifier.weight(1f))

                    ItemDetailDivider(modifier = Modifier.padding(top = 8.dp, start = 24.dp, end = 24.dp))

                    ItemDetailDeleteTextBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(75.dp)
                            .padding(top = 8.dp, start = 24.dp, end = 24.dp),
                        type = AgendaItemType.REMINDER,
                        onDeleteClick = {
                            actions.onDeleteClick
                            actions.onCloseClick
                        }
                    )
                }
            }
        }

    }
}