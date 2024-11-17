package com.rkm.tasky.feature.reminder.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rkm.tasky.di.ApplicationCoroutineScope
import com.rkm.tasky.feature.common.ItemUiState
import com.rkm.tasky.feature.common.Mode
import com.rkm.tasky.feature.error.errorToUiMessage
import com.rkm.tasky.feature.reminder.error.ItemError
import com.rkm.tasky.feature.reminder.model.Reminder
import com.rkm.tasky.navigation.Destination
import com.rkm.tasky.repository.abstraction.TaskyReminderRepository
import com.rkm.tasky.ui.component.ReminderDropDownMenuUiState
import com.rkm.tasky.ui.component.UiText
import com.rkm.tasky.util.date.ReminderBeforeDuration
import com.rkm.tasky.util.date.getCurrentDayInLocalDateTime
import com.rkm.tasky.util.date.getReminderBeforeDuration
import com.rkm.tasky.util.date.getReminderTime
import com.rkm.tasky.util.date.toLocalDateTime
import com.rkm.tasky.util.date.toLong
import com.rkm.tasky.util.result.onFailure
import com.rkm.tasky.util.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val repository: TaskyReminderRepository,
    private val savedStateHandle: SavedStateHandle,
    @ApplicationCoroutineScope private val scope: CoroutineScope
) : ViewModel() {

    private companion object {
        val TITLE_KEY = "TITLE"
        val DESC_KEY = "DESCRIPTION"
        val SHOW_TIME_KEY = "SHOW_TIME"
        val SHOW_DATE_KEY = "SHOW_DATE"
        val TIME_KEY = "TIME"
        val DATE_KEY = "DATE"
        val EDITABLE_KEY = "EDITABLE"
        val EXPANDED_KEY = "EXPANDED"
        val MODE_KEY = "MODE"
        val DURATION_KEY = "SHOW_TIME"
    }

    private val _itemUiState = MutableStateFlow(ItemUiState())
    val itemUiState = _itemUiState.asStateFlow()

    private val _reminder = MutableStateFlow(ReminderUiModel())
    val reminder = _reminder.asStateFlow()

    private val _dropDownState = MutableStateFlow(ReminderDropDownMenuUiState())
    val dropDownState = _dropDownState.asStateFlow()

    private val routeData = savedStateHandle.toRoute<Destination.Reminder>()

    private lateinit var oldReminder: Reminder

    init {
        if (isRestoredInstance()) {
            _itemUiState.update {
                it.copy(
                    isEditable = savedStateHandle.get<Boolean>(EDITABLE_KEY) ?: false,
                    showDateDialog = savedStateHandle.get<Boolean>(SHOW_DATE_KEY) ?: false,
                    showTimeDialog = savedStateHandle.get<Boolean>(SHOW_TIME_KEY) ?: false,
                    mode = Mode.valueOf(savedStateHandle.get<String>(MODE_KEY) ?: Mode.CREATE.name)
                )
            }
            val date = savedStateHandle.get<String>(DATE_KEY)
            val time = savedStateHandle.get<String>(TIME_KEY)
            val duration = savedStateHandle.get<String>(DURATION_KEY)
                ?: ReminderBeforeDuration.TEN_MINUTES.name
            _reminder.update {
                it.copy(
                    title = savedStateHandle.get<String>(TITLE_KEY) ?: "",
                    desc = savedStateHandle.get<String>(DESC_KEY) ?: "",
                    date = if (date.isNullOrEmpty()) LocalDate.parse(date!!) else getCurrentDayInLocalDateTime().date,
                    time = if (time.isNullOrEmpty()) LocalTime.parse(time!!) else getCurrentDayInLocalDateTime().time,
                    duration = ReminderBeforeDuration.valueOf(duration)
                )
            }
            _dropDownState.update {
                it.copy(
                    isExpanded = savedStateHandle.get<Boolean>(EXPANDED_KEY) ?: false,
                    selectedDuration = ReminderBeforeDuration.valueOf(duration)
                )
            }
        } else {
            val mode = Mode.valueOf(routeData.mode)
            if (mode == Mode.CREATE) {
                val selectedDateTime = LocalDateTime.parse(routeData.date)
                _reminder.update { it.copy(date = selectedDateTime.date) }
                _itemUiState.update { it.copy(isEditable = mode.isEditable) }
            } else {
                setUpReminder()
            }
        }

    }

    private val _reminderScreenEventChannel = Channel<ReminderUiEvent>()
    val reminderScreenEventChannel = _reminderScreenEventChannel.receiveAsFlow()

    fun onEdit() {
        savedStateHandle[EDITABLE_KEY] = !_itemUiState.value.isEditable
        _itemUiState.update { it.copy(isEditable = !it.isEditable) }
        if (_itemUiState.value.isEditable) {
            _itemUiState.update { it.copy(mode = Mode.UPDATE) }
            savedStateHandle[MODE_KEY] = Mode.UPDATE.name
        } else {
            _itemUiState.update { it.copy(mode = Mode.VIEW) }
            savedStateHandle[MODE_KEY] = Mode.VIEW.name
        }
    }

    fun onSave() {
        scope.launch {
            if (reminder.value.title.isEmpty()) {
                _reminderScreenEventChannel.send(
                    ReminderUiEvent.ReminderErrorEvent(
                        UiText.StringResource(
                            errorToUiMessage(ItemError.UiError.NO_TITLE)
                        )
                    )
                )
                return@launch
            }
            if (itemUiState.value.mode == Mode.CREATE) {
                onCreateReminder()
            } else {
                onUpdateReminder()
            }
            onEdit()
        }
    }

    private suspend fun onCreateReminder() {
        val time = LocalDateTime(reminder.value.date, reminder.value.time)
        val newReminder = Reminder(
            id = UUID.randomUUID().toString(),
            title = reminder.value.title,
            description = reminder.value.desc.ifEmpty { null },
            time = time,
            remindAt = getReminderTime(
                time.toLong(),
                dropDownState.value.selectedDuration
            ).toLocalDateTime()
        )

        repository.createReminder(newReminder)
        _reminderScreenEventChannel.send(ReminderUiEvent.ReminderCreateEvent)
    }

    private suspend fun onUpdateReminder() {
        val time = LocalDateTime(reminder.value.date, reminder.value.time)
        val newReminder = Reminder(
            id = oldReminder.id,
            title = reminder.value.title,
            description = reminder.value.desc.ifEmpty { null },
            time = time,
            remindAt = getReminderTime(
                time.toLong(),
                dropDownState.value.selectedDuration
            ).toLocalDateTime(),
        )
        repository.updateReminder(newReminder)
        _reminderScreenEventChannel.send(ReminderUiEvent.ReminderUpdateEvent)
    }

    fun onDelete() {

    }

    private fun setUpReminder() {
        viewModelScope.launch {
            val mode = Mode.valueOf(routeData.mode)
            val result = repository.getReminder(routeData.id)
            result.onFailure {
                _reminderScreenEventChannel.send(
                    ReminderUiEvent.ReminderErrorEvent(
                        UiText.StringResource(
                            errorToUiMessage(ItemError.UiError.UNKNOWN_ERROR)
                        )
                    )
                )
            }
            result.onSuccess {
                oldReminder = it
                val duration = getReminderBeforeDuration(
                    oldReminder.time.toLong(),
                    oldReminder.remindAt.toLong()
                )
                _reminder.update { it.copy(
                    title = oldReminder.title,
                    desc = oldReminder.description ?: "",
                    date = oldReminder.time.date,
                    time = oldReminder.time.time,
                    duration = duration
                ) }
                _itemUiState.update { it.copy(isEditable = mode.isEditable, mode = mode) }
                _dropDownState.update { it.copy(selectedDuration = duration) }
            }
        }
    }

    fun onUpdateTitle(title: String) {
        _reminder.update { it.copy(title = title) }
        savedStateHandle[TITLE_KEY] = title
    }

    fun onUpdateDescription(desc: String) {
        _reminder.update { it.copy(desc = desc) }
        savedStateHandle[DESC_KEY] = desc
    }

    fun showDateDialog() {
        savedStateHandle[SHOW_DATE_KEY] = !_itemUiState.value.showDateDialog
        _itemUiState.update { it.copy(showDateDialog = !it.showDateDialog) }
    }

    fun showTimeDialog() {
        savedStateHandle[SHOW_TIME_KEY] = !_itemUiState.value.showTimeDialog
        _itemUiState.update { it.copy(showTimeDialog = !it.showTimeDialog) }
    }

    fun updateDate(date: LocalDate) {
        savedStateHandle[DATE_KEY] = date.toString()
        _reminder.update { it.copy(date = date) }
    }

    fun updateTime(time: LocalTime) {
        savedStateHandle[TIME_KEY] = "${time.hour}:${time.minute}"
        _reminder.update { it.copy(time = time) }
    }

    fun onExpanded() {
        savedStateHandle[EXPANDED_KEY] = !_dropDownState.value.isExpanded
        _dropDownState.update { it.copy(isExpanded = !it.isExpanded) }
    }

    fun onSelectedDuration(duration: ReminderBeforeDuration) {
        savedStateHandle[DURATION_KEY] = duration.name
        _dropDownState.update { it.copy(selectedDuration = duration) }
    }

    private fun isRestoredInstance(): Boolean {
        return savedStateHandle.contains(SHOW_TIME_KEY) && savedStateHandle.contains(SHOW_DATE_KEY) && savedStateHandle.contains(
            TIME_KEY
        )
                && savedStateHandle.contains(DATE_KEY) && savedStateHandle.contains(EDITABLE_KEY) && savedStateHandle.contains(
            EXPANDED_KEY
        )
                && savedStateHandle.contains(MODE_KEY) && savedStateHandle.contains(DURATION_KEY)
    }
}

data class ReminderUiModel(
    val title: String = "",
    val desc: String = "",
    val date: LocalDate = getCurrentDayInLocalDateTime().date,
    val time: LocalTime = LocalTime(hour = 8, minute = 0),
    val duration: ReminderBeforeDuration = ReminderBeforeDuration.TEN_MINUTES
)

sealed interface ReminderUiEvent {
    data object ReminderCreateEvent : ReminderUiEvent
    data object ReminderUpdateEvent : ReminderUiEvent
    data object ReminderDeleteEvent : ReminderUiEvent
    data class ReminderErrorEvent(
        val message: UiText
    ) : ReminderUiEvent
}

