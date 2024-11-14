@file:OptIn(ExperimentalMaterial3Api::class)

package com.rkm.tasky.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rkm.tasky.R
import com.rkm.tasky.util.date.toLocalDateTime
import com.rkm.tasky.util.date.toLong
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Composable
fun DatePickerModal(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate.toLong())
    DatePickerDialog(
        onDismissRequest = onDismiss,
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
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.agenda_datepicker_modal_dismiss))
            }
        }
    ) {
        DatePicker(state = datePickerState)
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
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onTimeSelected(convertTime(timePickerState.hour, timePickerState.minute, timePickerState.isAfternoon))
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
        },
        text = { TimePicker(timePickerState) }
    )
}

private fun convertTime(hour: Int, minute: Int, isAfternoon: Boolean): LocalTime {
    var newHour = hour
    if(isAfternoon && hour != 12) {
        newHour += 12
    } else if(!isAfternoon && hour == 12) {
        newHour = 0
    }

    return LocalTime(newHour, minute)
}
