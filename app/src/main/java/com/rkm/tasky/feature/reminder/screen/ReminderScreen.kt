@file:OptIn(ExperimentalMaterial3Api::class)

package com.rkm.tasky.feature.reminder.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rkm.tasky.R
import com.rkm.tasky.feature.reminder.viewmodel.ReminderUiModel
import com.rkm.tasky.feature.reminder.viewmodel.ReminderViewModel
import com.rkm.tasky.navigation.EditActionType
import com.rkm.tasky.ui.component.DatePickerModal
import com.rkm.tasky.ui.component.TimePickerModal
import com.rkm.tasky.ui.theme.DividerColor
import com.rkm.tasky.ui.theme.EditFieldColorCaret
import com.rkm.tasky.ui.theme.ItemDateTextColor
import com.rkm.tasky.ui.theme.ItemMainBodyBackgroundColor
import com.rkm.tasky.ui.theme.ItemMainBodyForegroundColor
import com.rkm.tasky.ui.theme.RadioButtonOutlineColor
import com.rkm.tasky.ui.theme.ReminderColorId
import com.rkm.tasky.ui.theme.ItemDescTextColor
import com.rkm.tasky.ui.theme.ItemTimeTextColor
import com.rkm.tasky.ui.theme.ItemTitleTextColor
import com.rkm.tasky.ui.theme.ReminderSquareStroke
import com.rkm.tasky.ui.theme.ItemTypeTitleTextColor
import com.rkm.tasky.ui.theme.TopBarBackgroundColor
import com.rkm.tasky.ui.theme.TopBarIconColor
import com.rkm.tasky.ui.theme.TopBarTitleColor
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
        actions = actions
    )
}

@Composable
private fun ReminderScreen(
    modifier: Modifier,
    reminder: ReminderUiModel,
    state: ItemUiState,
    actions: ItemScreenActions
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
            CenterAlignedTopAppBar(
                title = { TopBarTitle(if (state.isEditable) stringResource(R.string.reminder_screen_top_app_bar_title)
                    else "") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = TopBarBackgroundColor
                ),
                navigationIcon = {
                    IconButton(onClick = actions.onCloseClick) {
                        Icon(
                            painter = painterResource(R.drawable.close_icon),
                            tint = TopBarIconColor,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    if (state.isEditable) {
                        TextButton(onClick = actions.onSaveClick) {
                            Text(
                                text = stringResource(R.string.item_screen_save),
                                style = MaterialTheme.typography.titleLarge,
                                color = TopBarTitleColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        IconButton(onClick = actions.onEditClick) {
                            Icon(
                                painter = painterResource(R.drawable.edit_icon),
                                tint = TopBarIconColor,
                                contentDescription = null
                            )
                        }
                    }
                }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(ItemMainBodyBackgroundColor)
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(ItemMainBodyForegroundColor)
            ) {

                Row(
                    modifier = Modifier.padding(top = 24.dp, start = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Canvas(
                        Modifier.size(25.dp)
                    ) {
                        drawRect(
                            color = ReminderColorId,
                            size = size,
                        )

                        drawRect(
                            color = ReminderSquareStroke,
                            size = size,
                            style = Stroke(width = 1.dp.toPx())
                        )
                    }
                    Text(
                        modifier = Modifier.padding(start = 12.dp),
                        text = stringResource(R.string.reminder_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = ItemTypeTitleTextColor
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(top = 24.dp, start = 24.dp, end = 24.dp)
                        .fillMaxWidth()
                        .clickable {
                            if (state.isEditable) {
                                actions.onEditField(EditActionType.Title)
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            painter = painterResource(R.drawable.radio_button_icon),
                            tint = RadioButtonOutlineColor,
                            contentDescription = null
                        )

                        Text(
                            modifier = Modifier.padding(12.dp),
                            text = reminder.title,
                            color = ItemTitleTextColor,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if(state.isEditable) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            painter = painterResource(R.drawable.edit_field_icon),
                            tint = EditFieldColorCaret,
                            contentDescription = null
                        )
                    }
                }

                HorizontalDivider(
                    Modifier.padding(top = 8.dp, start = 24.dp, end = 24.dp),
                    thickness = 2.dp,
                    color = DividerColor
                )

                Row(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 24.dp, end = 24.dp)
                        .fillMaxWidth()
                        .height(75.dp)
                        .clickable {
                            if (state.isEditable) {
                                actions.onEditField(EditActionType.Description)
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = reminder.desc,
                        style = MaterialTheme.typography.bodyLarge,
                        color = ItemDescTextColor,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    if(state.isEditable) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            painter = painterResource(R.drawable.edit_field_icon),
                            tint = EditFieldColorCaret,
                            contentDescription = null
                        )
                    }
                }

                HorizontalDivider(
                    Modifier.padding(top = 16.dp, start = 24.dp, end = 24.dp),
                    thickness = 2.dp,
                    color = DividerColor
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(top = 16.dp, start = 24.dp, end = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f).clickable {
                            if (state.isEditable) {
                                actions.onTimeClick
                            }
                        }
                    ) {
                        Text(
                            modifier = Modifier.padding(end = 32.dp),
                            text = stringResource(R.string.item_start_time),
                            style = MaterialTheme.typography.bodyLarge,
                            color = ItemTimeTextColor,
                        )
                        Text(
                            text = reminder.time.toUiString(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = ItemTimeTextColor,
                        )
                    }

                    Text(
                        modifier = Modifier.weight(1f).clickable {
                            actions.onDateClick
                        },
                        text = reminder.date.toUiString(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = ItemDateTextColor,
                        textAlign = TextAlign.Center
                    )
                }

                HorizontalDivider(
                    Modifier.padding(top = 16.dp, start = 24.dp, end = 24.dp),
                    thickness = 2.dp,
                    color = DividerColor
                )

            }
        }

    }
}

@Composable
private fun TopBarTitle(
    date: String
) {
    Text(
        text = date,
        color = TopBarTitleColor,
        style = MaterialTheme.typography.titleLarge
    )
}


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
    ReminderScreen(
        Modifier,
        reminder,
        state,
        actions
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
    ReminderScreen(
        Modifier,
        reminder,
        state,
        actions
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
    ReminderScreen(
        Modifier,
        reminder,
        state,
        actions
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
    ReminderScreen(
        Modifier,
        reminder,
        state,
        actions
    )
}