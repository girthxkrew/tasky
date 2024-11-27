package com.rkm.tasky.feature.common.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rkm.tasky.R
import com.rkm.tasky.ui.theme.ItemDetailContainerColor
import com.rkm.tasky.ui.theme.ItemDetailReminderDividerColor
import com.rkm.tasky.ui.theme.ItemIconNotificationBackgroundColor
import com.rkm.tasky.ui.theme.ItemIconNotificationBellColor
import com.rkm.tasky.util.date.ReminderBeforeDuration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderDropDownMenu(
    state: ReminderDropDownMenuUiState,
    actions: ReminderDropDownMenuActions,
    isEditable: Boolean,
    modifier: Modifier = Modifier
) {


    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = state.isExpanded,
        onExpandedChange = { actions.onExpanded() }
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = isEditable)
                .fillMaxWidth(),
            value = stringResource(state.selectedDuration.toUiString()),
            onValueChange = {},
            readOnly = true,
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                focusedBorderColor = ItemDetailReminderDividerColor,
                unfocusedBorderColor = ItemDetailReminderDividerColor,
            ),
            leadingIcon = {
                IconWithBackground(
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.notications_icon),
                            contentDescription = null,
                            tint = ItemIconNotificationBellColor
                        )
                    },
                    size = 30
                )
            }
        )

        ExposedDropdownMenu(
            expanded = state.isExpanded,
            onDismissRequest = { actions.onExpanded() },
            containerColor = ItemDetailContainerColor
        ) {
            ReminderBeforeDuration.entries.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(option.toUiString()),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    onClick = {
                        actions.onExpanded()
                        actions.onSelectedDuration(option)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Composable
private fun IconWithBackground(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    backgroundColor: Color = ItemIconNotificationBackgroundColor,
    size: Int = 50
) {

    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }

}

data class ReminderDropDownMenuUiState(
    val isExpanded: Boolean = false,
    val selectedDuration: ReminderBeforeDuration = ReminderBeforeDuration.TEN_MINUTES
)

data class ReminderDropDownMenuActions(
    val onExpanded: () -> Unit,
    val onSelectedDuration: (ReminderBeforeDuration) -> Unit
)


fun ReminderBeforeDuration.toUiString(): Int {
    return when (this) {
        ReminderBeforeDuration.TEN_MINUTES -> R.string.reminder_option_one
        ReminderBeforeDuration.THIRTY_MINUTES -> R.string.reminder_option_two
        ReminderBeforeDuration.ONE_HOUR -> R.string.reminder_option_three
        ReminderBeforeDuration.SIX_HOURS -> R.string.reminder_option_four
        ReminderBeforeDuration.ONE_DAY -> R.string.reminder_option_five
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun ReminderClosedDropDownPreview() {
    val actions = ReminderDropDownMenuActions({}, {})
    ReminderDropDownMenu(
        state = ReminderDropDownMenuUiState(),
        actions = actions,
        false,
    )
}

@Preview(
    showBackground = true
)
@Composable
private fun ReminderOpenDropDownPreview() {
    val actions = ReminderDropDownMenuActions({}, {})
    ReminderDropDownMenu(
        state = ReminderDropDownMenuUiState(isExpanded = true, ReminderBeforeDuration.ONE_HOUR),
        actions = actions,
        true
    )
}


