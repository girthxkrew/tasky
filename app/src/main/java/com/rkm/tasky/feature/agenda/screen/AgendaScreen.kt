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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rkm.tasky.R
import com.rkm.tasky.feature.agenda.viewmodel.AgendaViewModel
import com.rkm.tasky.ui.theme.AgendaMainBodyBackgroundColor
import com.rkm.tasky.ui.theme.AgendaMainBodyForegroundColor
import com.rkm.tasky.ui.theme.AgendaTopBarBackgroundColor
import com.rkm.tasky.ui.theme.AgendaTopBarTitleColor

@Composable
fun AgendaScreenRoot(
    modifier: Modifier,
    viewModel: AgendaViewModel = hiltViewModel()
) {
    val showDialog by viewModel.showDialog.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val selectedMonth by viewModel.selectedMonth.collectAsStateWithLifecycle()
    val sixDays by viewModel.sixDay.collectAsStateWithLifecycle()

    AgendaScreen(
        modifier = modifier,
        showDialog = showDialog,
        selectedDate = selectedDate,
        selectedMonth = selectedMonth,
        sixDay = sixDays,
        onDateSelected = viewModel::updateSelectedDate,
        updateSixDays = viewModel::updateSixDays,
        onShowDatePicker = viewModel::updateShowDialog
    )
}

@Composable
private fun AgendaScreen(
    modifier: Modifier,
    showDialog: Boolean,
    selectedDate: Long,
    selectedMonth: String,
    sixDay: List<DayUiModel>,
    onDateSelected: (Long) -> Unit,
    updateSixDays: (Long) -> Unit,
    onShowDatePicker: () -> Unit
) {
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
                    titleContentColor = AgendaTopBarTitleColor,
                    containerColor = AgendaTopBarBackgroundColor
                )
            )
            LazyColumn(
                modifier = modifier.fillMaxSize()
                    .background(AgendaMainBodyBackgroundColor)
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(AgendaMainBodyForegroundColor)
            ) {
                item {
                    SixDaySelector(modifier.padding(top = 16.dp), sixDay, {})
                }
            }
        }

        if (showDialog) {
            DatePickerModal(
                selectedDate = selectedDate,
                onDateSelected = onDateSelected,
                updateSixDays = updateSixDays,
                onDismiss = onShowDatePicker
            )
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

@Composable
private fun DatePickerModal(
    selectedDate: Long,
    onDateSelected: (Long) -> Unit,
    updateSixDays: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateSelected(it)
                        updateSixDays(it)
                    }
                    onDismiss()
                }
            ) {
                Text(text = stringResource(R.string.agenda_datepicker_modal_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.agenda_datepicker_modal_dismiss))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun PreviewAgendaScreen() {
    val list = mutableListOf(
        DayUiModel(0L, "M", 5),
        DayUiModel(0L, "T", 6),
        DayUiModel(0L, "W", 7),
        DayUiModel(0L, "T", 8),
        DayUiModel(0L, "F", 9),
        DayUiModel(0L, "S", 10)
    )
    AgendaScreen(
        Modifier,
        showDialog = false,
        selectedMonth = "DECEMBER",
        sixDay = list,
        selectedDate = 0L,
        onDateSelected = {},
        updateSixDays = {},
        onShowDatePicker = {}
    )
}