
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rkm.tasky.feature.common.ScreenType
import com.rkm.tasky.feature.reminder.viewmodel.ReminderUiModel
import com.rkm.tasky.feature.reminder.viewmodel.ReminderViewModel
import com.rkm.tasky.navigation.EditActionType
import com.rkm.tasky.ui.component.DatePickerModal
import com.rkm.tasky.ui.component.ItemDetailBody
import com.rkm.tasky.ui.component.ItemDetailDateTimePicker
import com.rkm.tasky.ui.component.ItemDetailDeleteTextBox
import com.rkm.tasky.ui.component.ItemDetailDescription
import com.rkm.tasky.ui.component.ItemDetailDivider
import com.rkm.tasky.ui.component.ItemDetailTitle
import com.rkm.tasky.ui.component.ItemDetailTopBar
import com.rkm.tasky.ui.component.ItemDetailTypeTitle
import com.rkm.tasky.ui.component.ReminderDropDownMenu
import com.rkm.tasky.ui.component.ReminderDropDownMenuActions
import com.rkm.tasky.ui.component.ReminderDropDownMenuUiState
import com.rkm.tasky.ui.component.TimePickerModal
import com.rkm.tasky.ui.theme.ReminderColorId
import com.rkm.tasky.ui.theme.ReminderSquareStroke
import com.rkm.tasky.util.date.toUiString


@Composable
fun ReminderScreenRoot(
    modifier: Modifier,
    viewModel: ReminderViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onEditField: (EditActionType) -> Unit
) {
    val itemUiState by viewModel.itemUiState.collectAsStateWithLifecycle()
    val reminder by viewModel.reminder.collectAsStateWithLifecycle()
    val dropDownState by viewModel.dropDownState.collectAsStateWithLifecycle()

    val dropDownActions = ReminderDropDownMenuActions(
        onExpanded = viewModel::onExpanded,
        onSelectedDuration = viewModel::onSelectedDuration
    )

    val actions = ItemScreenActions(
        onTimeClick = viewModel::showDateDialog,
        onDateClick = viewModel::showTimeDialog,
        onSaveClick = viewModel::onSave,
        onEditClick = viewModel::onEdit,
        onCloseClick = onNavigateBack,
        onDeleteClick = viewModel::onDelete,
        onEditField = onEditField,
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
                type = ScreenType.REMINDER,
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
                    type = ScreenType.REMINDER
                )

                ItemDetailTitle(
                    modifier = Modifier
                        .padding(top = 24.dp, start = 24.dp, end = 24.dp)
                        .fillMaxWidth(),
                    isEditable = state.isEditable,
                    title = reminder.title,
                    onEditClick = { actions.onEditField(EditActionType.Title) }
                )

                ItemDetailDivider(modifier = Modifier.padding(top = 8.dp, start = 24.dp, end = 24.dp))

                ItemDetailDescription(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 24.dp, end = 24.dp)
                        .fillMaxWidth()
                        .height(75.dp),
                    isEditable = state.isEditable,
                    desc = reminder.desc,
                    onEditClick = { actions.onEditField(EditActionType.Description) }
                )

                ItemDetailDivider(modifier = Modifier.padding(top = 8.dp, start = 24.dp, end = 24.dp))

                ItemDetailDateTimePicker(
                    modifier = Modifier.fillMaxWidth()
                        .height(50.dp)
                        .padding(top = 16.dp, start = 24.dp, end = 24.dp),
                    isEditable = state.isEditable,
                    time = reminder.time.toUiString(),
                    date = reminder.date.toUiString(),
                    onTimeClick = { actions.onTimeClick },
                    onDateClick = {actions.onDateClick }
                )

                ItemDetailDivider(modifier = Modifier.padding(top = 8.dp, start = 24.dp, end = 24.dp))

                ReminderDropDownMenu(
                    dropDownMenuUiState,
                    dropDownActions,
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 24.dp, end = 24.dp)
                )

                Spacer(Modifier.weight(1f))

                ItemDetailDivider(modifier = Modifier.padding(top = 8.dp, start = 24.dp, end = 24.dp))

                ItemDetailDeleteTextBox(
                    modifier = Modifier.fillMaxWidth().height(75.dp).padding(top = 8.dp, start = 24.dp, end = 24.dp),
                    type = ScreenType.REMINDER,
                    onDeleteClick = actions.onDeleteClick
                )

            }
        }

    }
}