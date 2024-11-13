package com.rkm.tasky.feature.reminder.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.rkm.tasky.di.ApplicationCoroutineScope
import com.rkm.tasky.feature.common.Mode
import com.rkm.tasky.repository.implementation.TaskyReminderRepositoryImpl
import com.rkm.tasky.util.date.getCurrentDayInLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val repository: TaskyReminderRepositoryImpl,
    private val savedStateHandle: SavedStateHandle,
    @ApplicationCoroutineScope private val scope: CoroutineScope
): ViewModel() {

    private val mode = MutableStateFlow(Mode.CREATE)

    private val _isEditable = MutableStateFlow(false)
    val isEditable = _isEditable.asStateFlow()

    private val _showDateDialog = MutableStateFlow(false)
    val showDateDialog = _showDateDialog.asStateFlow()

    private val _showTimeDialog = MutableStateFlow(false)
    val showTimeDialog = _showTimeDialog.asStateFlow()

    private val _reminder = MutableStateFlow(ReminderUiModel())
    val reminder = _reminder.asStateFlow()

    fun onEdit() {
        _isEditable.update { !it }
    }

    fun onSave() {

    }

    fun onDelete() {

    }

    fun showDateDialog() {
        _showDateDialog.update { !it }
    }

    fun showTimeDialog() {
        _showTimeDialog.update { !it }
    }

}

data class ReminderUiModel(
    val title: String = "",
    val desc: String = "",
    val date: LocalDate = getCurrentDayInLocalDateTime().date,
    val time: LocalTime = getCurrentDayInLocalDateTime().time
)