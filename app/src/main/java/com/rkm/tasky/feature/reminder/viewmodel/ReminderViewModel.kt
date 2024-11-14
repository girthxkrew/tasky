package com.rkm.tasky.feature.reminder.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.rkm.tasky.di.ApplicationCoroutineScope
import com.rkm.tasky.feature.common.Mode
import com.rkm.tasky.feature.reminder.screen.ItemUiState
import com.rkm.tasky.repository.abstraction.TaskyReminderRepository
import com.rkm.tasky.util.date.getCurrentDayInLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val repository: TaskyReminderRepository,
    private val savedStateHandle: SavedStateHandle,
    @ApplicationCoroutineScope private val scope: CoroutineScope
): ViewModel() {

    private val mode = MutableStateFlow(Mode.CREATE)

    private val _itemUiState = MutableStateFlow(ItemUiState())
    val itemUiState = _itemUiState.asStateFlow()

    private val _reminder = MutableStateFlow(ReminderUiModel())
    val reminder = _reminder.asStateFlow()

    fun onEdit() {
        _itemUiState.update { it.copy(isEditable = !it.isEditable) }
    }

    fun onSave() {

    }

    fun onDelete() {

    }

    fun showDateDialog() {
        _itemUiState.update { it.copy(showDateDialog = !it.showDateDialog) }
    }

    fun showTimeDialog() {
        _itemUiState.update { it.copy(showTimeDialog = !it.showTimeDialog) }
    }

    fun updateDate(date: LocalDate) {
        _reminder.update { it.copy(date = date) }
    }

    fun updateTime(time: LocalTime) {
        _reminder.update { it.copy(time = time) }
    }
}

data class ReminderUiModel(
    val title: String = "",
    val desc: String = "",
    val date: LocalDate = getCurrentDayInLocalDateTime().date,
    val time: LocalTime = getCurrentDayInLocalDateTime().time
)

