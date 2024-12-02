package com.rkm.tasky.feature.common.item

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rkm.tasky.R
import com.rkm.tasky.feature.common.AgendaItemType
import com.rkm.tasky.feature.common.toUiString
import com.rkm.tasky.ui.theme.EditFieldColorCaret
import com.rkm.tasky.ui.theme.ItemDateTextColor
import com.rkm.tasky.ui.theme.ItemDescTextColor
import com.rkm.tasky.ui.theme.ItemDetailDeleteTextColor
import com.rkm.tasky.ui.theme.ItemDetailDividerColor
import com.rkm.tasky.ui.theme.ItemMainBodyBackgroundColor
import com.rkm.tasky.ui.theme.ItemMainBodyForegroundColor
import com.rkm.tasky.ui.theme.ItemNoDescTextColor
import com.rkm.tasky.ui.theme.ItemNoTitleTextColor
import com.rkm.tasky.ui.theme.ItemTimeTextColor
import com.rkm.tasky.ui.theme.ItemTitleTextColor
import com.rkm.tasky.ui.theme.ItemTypeTitleTextColor
import com.rkm.tasky.ui.theme.RadioButtonOutlineColor
import com.rkm.tasky.ui.theme.TopBarBackgroundColor
import com.rkm.tasky.ui.theme.TopBarIconColor
import com.rkm.tasky.ui.theme.TopBarTitleColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailTopBar(
    modifier: Modifier = Modifier,
    selectedDate: String,
    type: AgendaItemType,
    isEditable: Boolean,
    onSaveClick: () -> Unit,
    onEditClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            ItemDetailTopBarTitle(
                if (isEditable) stringResource(
                    R.string.item_detail_top_app_bar_title,
                    stringResource(type.toUiString()).uppercase()
                )
                else selectedDate
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TopBarBackgroundColor
        ),
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    painter = painterResource(R.drawable.close_icon),
                    tint = TopBarIconColor,
                    contentDescription = null
                )
            }
        },
        actions = {
            if (isEditable) {
                TextButton(onClick = onSaveClick) {
                    Text(
                        text = stringResource(R.string.item_detail_save),
                        style = MaterialTheme.typography.titleLarge,
                        color = TopBarTitleColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                IconButton(onClick = onEditClick) {
                    Icon(
                        painter = painterResource(R.drawable.edit_icon),
                        tint = TopBarIconColor,
                        contentDescription = null
                    )
                }
            }
        }
    )
}

@Composable
private fun ItemDetailTopBarTitle(
    text: String
) {
    Text(
        text = text,
        color = TopBarTitleColor,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun ItemDetailBody(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ItemMainBodyBackgroundColor)
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(ItemMainBodyForegroundColor)
    ) {
        content()
    }
}

@Composable
fun ItemDetailTypeTitle(
    modifier: Modifier,
    rectangleColor: Color,
    rectangleOutlineColor: Color,
    type: AgendaItemType
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            Modifier.size(25.dp)
        ) {
            drawRect(
                color = rectangleColor,
                size = size,
            )

            drawRect(
                color = rectangleOutlineColor,
                size = size,
                style = Stroke(width = 1.dp.toPx())
            )
        }
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = stringResource(type.toUiString()),
            style = MaterialTheme.typography.titleLarge,
            color = ItemTypeTitleTextColor
        )
    }
}

@Composable
fun ItemDetailTitle(
    modifier: Modifier,
    isEditable: Boolean,
    title: String,
    onEditClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable {
                if (isEditable) {
                    onEditClick()
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
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
                text = if(title.isEmpty()) stringResource(R.string.item_detail_no_title) else title,
                color = if(title.isEmpty()) ItemNoTitleTextColor else ItemTitleTextColor,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (isEditable) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(R.drawable.edit_field_icon),
                tint = EditFieldColorCaret,
                contentDescription = null
            )
        }
    }
}

@Composable
fun ItemDetailDivider(
    modifier: Modifier
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 2.dp,
        color = ItemDetailDividerColor
    )
}

@Composable
fun ItemDetailDescription(
    modifier: Modifier,
    isEditable: Boolean,
    desc: String,
    onEditClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable {
                if (isEditable) {
                    onEditClick()
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = desc.ifEmpty { stringResource(R.string.item_detail_no_desc) },
            style = MaterialTheme.typography.bodyLarge,
            color = if(desc.isEmpty()) ItemNoDescTextColor else ItemDescTextColor,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        if (isEditable) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(R.drawable.edit_field_icon),
                tint = EditFieldColorCaret,
                contentDescription = null
            )
        }
    }
}

@Composable
fun ItemDetailDateTimePicker(
    modifier: Modifier,
    isEditable: Boolean,
    time: String,
    date: String,
    onTimeClick: () -> Unit,
    onDateClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    if (isEditable) {
                        onTimeClick()
                    }
                }
        ) {
            Text(
                modifier = Modifier.padding(end = 32.dp),
                text = stringResource(R.string.item_detail_start_time),
                style = MaterialTheme.typography.bodyLarge,
                color = ItemTimeTextColor,
            )
            Text(
                text = time,
                style = MaterialTheme.typography.bodyLarge,
                color = ItemTimeTextColor,
            )
        }

        Text(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    onDateClick()
                },
            text = date,
            style = MaterialTheme.typography.bodyLarge,
            color = ItemDateTextColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ItemDetailDeleteTextBox(
    modifier: Modifier,
    type: AgendaItemType,
    onDeleteClick: () -> Unit
) {
    Text(
        modifier = modifier.clickable {
            onDeleteClick()
        },
        text = stringResource(R.string.item_detail_delete, stringResource(type.toUiString()).uppercase()),
        style = MaterialTheme.typography.bodyLarge,
        color = ItemDetailDeleteTextColor,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )
}