@file:OptIn(ExperimentalMaterial3Api::class)

package com.rkm.tasky.feature.agenda.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rkm.tasky.feature.agenda.screen.dayselector.DateItem
import com.rkm.tasky.feature.agenda.screen.dayselector.DateSelector
import com.rkm.tasky.feature.agenda.screen.dayselector.rememberDateSelectorState
import com.rkm.tasky.feature.agenda.viewmodel.AgendaViewModel
import com.rkm.tasky.feature.common.Mode
import com.rkm.tasky.ui.component.DatePickerModal
import com.rkm.tasky.ui.theme.ItemMainBodyBackgroundColor
import com.rkm.tasky.ui.theme.ItemMainBodyForegroundColor
import com.rkm.tasky.ui.theme.TopBarBackgroundColor
import com.rkm.tasky.ui.theme.TopBarTitleColor
import com.rkm.tasky.util.date.toLocalDateTime

@Composable
fun AgendaScreenRoot(
    modifier: Modifier,
    viewModel: AgendaViewModel = hiltViewModel(),
    onReminderClick: (id: String, mode: String, date: String) -> Unit
) {
    val showDialog by viewModel.showDialog.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val selectedMonth by viewModel.selectedMonth.collectAsStateWithLifecycle()

    AgendaScreen(
        modifier = modifier,
        showDialog = showDialog,
        selectedDate = selectedDate,
        selectedMonth = selectedMonth,
        updateSelectedDate = viewModel::updateSelectedDate,
        onDateSelected = viewModel::loadDate,
        onShowDatePicker = viewModel::updateShowDialog,
        onReminderClick = onReminderClick
    )
}

@Composable
private fun AgendaScreen(
    modifier: Modifier,
    showDialog: Boolean,
    selectedDate: Long,
    selectedMonth: String,
    updateSelectedDate: (Long) -> Unit,
    onDateSelected: (Long) -> Unit,
    onShowDatePicker: () -> Unit,
    onReminderClick: (id: String, mode: String, date: String) -> Unit,
) {

    val state = rememberDateSelectorState(selectedDate)
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = { TopBarDateSelector(month = selectedMonth, onClick = onShowDatePicker) },
                modifier = modifier.statusBarsPadding(),
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = TopBarTitleColor,
                    containerColor = TopBarBackgroundColor
                )
            )
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(ItemMainBodyBackgroundColor)
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(ItemMainBodyForegroundColor)
            ) {
                item {
                    DateSelector(
                        Modifier.padding(top = 16.dp),
                        state = state,
                        onClick = onDateSelected
                    )
                }
            }
        }

        Button(
            onClick = {
                onReminderClick(
                    "2033ecda-9dd1-44fa-bbc6-df500fc04292",
                    Mode.VIEW.name,
                    selectedDate.toLocalDateTime().toString()
                )
            }
        ) {
            Text("Reminder Screen flow")
        }

        if (showDialog) {
            //Todo: fix datepickermodal here to use LocalDate
//            DatePickerModal(
//                selectedDate = selectedDate,
//                onDateSelected = {
//                    updateSelectedDate(it)
//                    state.selectedDate = it
//                },
//                onDismiss = onShowDatePicker
//            )
        }
    }
}

@Composable
private fun TopBarDateSelector(
    month: String,
    onClick: () -> Unit,
) {

    Text(text = month, modifier = Modifier
        .wrapContentSize()
        .clickable {
            onClick()
        })

}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun PreviewAgendaScreen() {
    val list = mutableListOf(
        DateItem(0L, "M", 5),
        DateItem(0L, "T", 6),
        DateItem(0L, "W", 7),
        DateItem(0L, "T", 8),
        DateItem(0L, "F", 9),
        DateItem(0L, "S", 10)
    )
    AgendaScreen(
        Modifier,
        showDialog = false,
        selectedMonth = "DECEMBER",
        selectedDate = 0L,
        updateSelectedDate = {},
        onDateSelected = {},
        onShowDatePicker = {},
        onReminderClick = {_,_,_ -> }
    )
}