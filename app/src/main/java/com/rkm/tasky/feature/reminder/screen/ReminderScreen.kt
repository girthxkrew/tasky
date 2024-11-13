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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rkm.tasky.R
import com.rkm.tasky.feature.reminder.viewmodel.ReminderUiModel
import com.rkm.tasky.feature.reminder.viewmodel.ReminderViewModel
import com.rkm.tasky.navigation.EditActionType
import com.rkm.tasky.ui.theme.DividerColor
import com.rkm.tasky.ui.theme.EditFieldColorCaret
import com.rkm.tasky.ui.theme.ItemMainBodyBackgroundColor
import com.rkm.tasky.ui.theme.ItemMainBodyForegroundColor
import com.rkm.tasky.ui.theme.RadioButtonOutlineColor
import com.rkm.tasky.ui.theme.ReminderColorId
import com.rkm.tasky.ui.theme.ReminderItemDesc
import com.rkm.tasky.ui.theme.ReminderItemTitle
import com.rkm.tasky.ui.theme.ReminderStroke
import com.rkm.tasky.ui.theme.ReminderTitleTextColor
import com.rkm.tasky.ui.theme.TopBarBackgroundColor
import com.rkm.tasky.ui.theme.TopBarIconColor
import com.rkm.tasky.ui.theme.TopBarTitleColor


@Composable
fun ReminderScreenRoot(
    modifier: Modifier,
    viewModel: ReminderViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onEditField: (EditActionType) -> Unit
) {
    val isEditable by viewModel.isEditable.collectAsStateWithLifecycle()
    val reminder by viewModel.reminder.collectAsStateWithLifecycle()
    val showDateDialog by viewModel.showDateDialog.collectAsStateWithLifecycle()
    val showTimeDialog by viewModel.showTimeDialog.collectAsStateWithLifecycle()
    ReminderScreen(
        modifier = modifier,
        isEditable = isEditable,
        reminder = reminder,
        showDateDialog = showDateDialog,
        onShowDateDialog = viewModel::showDateDialog,
        showTimeDialog = showTimeDialog,
        onShowTimeDialog = viewModel::showTimeDialog,
        onSave = viewModel::onSave,
        onClose = onNavigateBack,
        onEdit = viewModel::onEdit,
        onDelete = viewModel::onDelete,
        onEditField = onEditField
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReminderScreen(
    modifier: Modifier,
    isEditable: Boolean,
    reminder: ReminderUiModel,
    showDateDialog: Boolean,
    onShowDateDialog: () -> Unit,
    showTimeDialog: Boolean,
    onShowTimeDialog: () -> Unit,
    onSave: () -> Unit,
    onClose: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onEditField:(EditActionType) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CenterAlignedTopAppBar(
                title = { TopBarTitle(if (isEditable) stringResource(R.string.reminder_screen_top_app_bar_title)
                    else "") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = TopBarBackgroundColor
                ),
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            painter = painterResource(R.drawable.close_icon),
                            tint = TopBarIconColor,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    if (isEditable) {
                        TextButton(onClick = onSave) {
                            Text(
                                text = stringResource(R.string.item_screen_save),
                                style = MaterialTheme.typography.titleLarge,
                                color = TopBarTitleColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        IconButton(onClick = onEdit) {
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
                modifier = Modifier.weight(1f)
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
                            color = ReminderStroke,
                            size = size,
                            style = Stroke(width = 1.dp.toPx())
                        )
                    }
                    Text(
                        modifier = Modifier.padding(start = 12.dp),
                        text = stringResource(R.string.reminder_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = ReminderTitleTextColor
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp)
                        .fillMaxWidth().clickable {
                            if(isEditable) {
                                onEditField(EditActionType.Title)
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
                            color = ReminderItemTitle,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if(isEditable) {
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
                    modifier = Modifier.padding(top = 16.dp, start = 24.dp, end = 24.dp)
                        .fillMaxWidth().height(75.dp).clickable {
                        if(isEditable) {
                            onEditField(EditActionType.Description)
                        }
                    },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = reminder.desc,
                        style = MaterialTheme.typography.bodyLarge,
                        color = ReminderItemDesc,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    if(isEditable) {
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
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                        .padding(top = 16.dp, start = 24.dp, end = 24.dp)
                ) {
                    TextButton(
                        modifier = Modifier.weight(1f),
                        onClick = onShowTimeDialog
                    ) {
                        
                    }
                }
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
    ReminderScreen(
        Modifier,
        false,
        reminder,
        false,
        {},
        false,
        {},
        {},
        {},
        {},
        {},
        {},
    )
}

@Preview(
    showSystemUi = true
)
@Composable
private fun PreviewEditableReminderScreen() {
    val reminder = ReminderUiModel("Project X", desc = "This is a description This is a description This is a description This is a description This is a description This is a description th")
    ReminderScreen(
        Modifier,
        true,
        reminder,
        false,
        {},
        false,
        {},
        {},
        {},
        {},
        {},
        {},
        )
}