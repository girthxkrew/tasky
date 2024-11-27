@file:OptIn(ExperimentalMaterial3Api::class)

package com.rkm.tasky.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rkm.tasky.R
import com.rkm.tasky.util.date.getCurrentDayInLong
import com.rkm.tasky.util.date.toLocalDateTime
import com.rkm.tasky.util.date.toLong
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun DatePickerModal(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate.toLong(), selectableDates = DateValidator)
    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateSelected(it.toLocalDateTime().date)
                    }
                    onDismiss()
                }
            ) {
                Text(text = stringResource(R.string.agenda_datepicker_modal_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = stringResource(R.string.agenda_datepicker_modal_dismiss))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

object DateValidator: SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis >= getCurrentDayInLong()
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year >= Clock.System.now().toLocalDateTime(TimeZone.UTC).year
    }

}

@Composable
fun TimePickerModal(
    selectedTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        selectedTime.hour,
        selectedTime.minute,
        false
    )

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = {
                    onTimeSelected(LocalTime(timePickerState.hour, timePickerState.minute))
                    onDismiss()
                }
            ) {
                Text(text = stringResource(R.string.agenda_datepicker_modal_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = stringResource(R.string.agenda_datepicker_modal_dismiss))
            }
        },
        text = { TimePicker(timePickerState) }
    )
}
