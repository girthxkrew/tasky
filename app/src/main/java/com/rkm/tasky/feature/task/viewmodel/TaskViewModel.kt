package com.rkm.tasky.feature.task.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rkm.tasky.di.ApplicationCoroutineScope
import com.rkm.tasky.feature.common.item.ItemUiState
import com.rkm.tasky.feature.common.Mode
import com.rkm.tasky.feature.common.item.ItemUiEvent
import com.rkm.tasky.feature.common.item.SavedStateConstants.DATE_KEY
import com.rkm.tasky.feature.common.item.SavedStateConstants.DESC_KEY
import com.rkm.tasky.feature.common.item.SavedStateConstants.DURATION_KEY
import com.rkm.tasky.feature.common.item.SavedStateConstants.EDITABLE_KEY
import com.rkm.tasky.feature.common.item.SavedStateConstants.EXPANDED_KEY
import com.rkm.tasky.feature.common.item.SavedStateConstants.MODE_KEY
import com.rkm.tasky.feature.common.item.SavedStateConstants.SHOW_DATE_KEY
import com.rkm.tasky.feature.common.item.SavedStateConstants.SHOW_TIME_KEY
import com.rkm.tasky.feature.common.item.SavedStateConstants.TIME_KEY
import com.rkm.tasky.feature.common.item.SavedStateConstants.TITLE_KEY
import com.rkm.tasky.feature.error.errorToUiMessage
import com.rkm.tasky.feature.common.item.ItemError
import com.rkm.tasky.navigation.Destination
import com.rkm.tasky.feature.common.item.ReminderDropDownMenuUiState
import com.rkm.tasky.feature.task.model.Task
import com.rkm.tasky.repository.abstraction.TaskyTaskRepository
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
class TaskViewModel @Inject constructor(
    private val repository: TaskyTaskRepository,
    private val savedStateHandle: SavedStateHandle,
    @ApplicationCoroutineScope private val scope: CoroutineScope
) : ViewModel() {



    private val _itemUiState = MutableStateFlow(ItemUiState())
    val itemUiState = _itemUiState.asStateFlow()

    private val _task = MutableStateFlow(TaskUiModel())
    val task = _task.asStateFlow()

    private val _dropDownState = MutableStateFlow(ReminderDropDownMenuUiState())
    val dropDownState = _dropDownState.asStateFlow()

    private val routeData = savedStateHandle.toRoute<Destination.Reminder>()

    private lateinit var oldTask: Task

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
            _task.update {
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
                _task.update { it.copy(date = selectedDateTime.date) }
                _itemUiState.update { it.copy(isEditable = mode.isEditable) }
            } else {
                setUpReminder()
            }
        }

    }

    private val _taskScreenEventChannel = Channel<ItemUiEvent>()
    val taskScreenEventChannel = _taskScreenEventChannel.receiveAsFlow()

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
            if (task.value.title.isEmpty()) {
                _taskScreenEventChannel.send(
                    ItemUiEvent.ItemErrorEvent(
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
        val time = LocalDateTime(task.value.date, task.value.time)
        val newTask = Task(
            id = UUID.randomUUID().toString(),
            title = task.value.title,
            description = task.value.desc.ifEmpty { null },
            time = time,
            remindAt = getReminderTime(
                time.toLong(),
                dropDownState.value.selectedDuration
            ).toLocalDateTime(),
            isDone = false,
        )

        repository.createTask(newTask)
        _taskScreenEventChannel.send(ItemUiEvent.ItemCreateEvent)
    }

    private suspend fun onUpdateReminder() {
        val time = LocalDateTime(task.value.date, task.value.time)
        val newTask = Task(
            id = oldTask.id,
            title = task.value.title,
            description = task.value.desc.ifEmpty { null },
            time = time,
            remindAt = getReminderTime(
                time.toLong(),
                dropDownState.value.selectedDuration
            ).toLocalDateTime(),
            isDone = oldTask.isDone
        )
        repository.updateTask(newTask)
        _taskScreenEventChannel.send(ItemUiEvent.ItemUpdateEvent)
    }

    fun onDelete() {
        scope.launch {
            repository.deleteTask(oldTask)
            _taskScreenEventChannel.send(ItemUiEvent.ItemDeleteEvent)
        }
    }

    private fun setUpReminder() {
        viewModelScope.launch {
            val mode = Mode.valueOf(routeData.mode)
            val result = repository.getTask(routeData.id)
            result.onFailure {
                _taskScreenEventChannel.send(
                    ItemUiEvent.ItemErrorEvent(
                        UiText.StringResource(
                            errorToUiMessage(ItemError.UiError.UNKNOWN_ERROR)
                        )
                    )
                )
            }
            result.onSuccess { task ->
                oldTask = task
                val duration = getReminderBeforeDuration(
                    oldTask.time.toLong(),
                    oldTask.remindAt.toLong()
                )
                _task.update { it.copy(
                    title = oldTask.title,
                    desc = oldTask.description ?: "",
                    date = oldTask.time.date,
                    time = oldTask.time.time,
                    duration = duration
                ) }
                _itemUiState.update { it.copy(isEditable = mode.isEditable, mode = mode) }
                _dropDownState.update { it.copy(selectedDuration = duration) }
            }
        }
    }

    fun onUpdateTitle(title: String) {
        _task.update { it.copy(title = title) }
        savedStateHandle[TITLE_KEY] = title
    }

    fun onUpdateDescription(desc: String) {
        _task.update { it.copy(desc = desc) }
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
        _task.update { it.copy(date = date) }
    }

    fun updateTime(time: LocalTime) {
        savedStateHandle[TIME_KEY] = "${time.hour}:${time.minute}"
        _task.update { it.copy(time = time) }
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

data class TaskUiModel(
    val title: String = "",
    val desc: String = "",
    val date: LocalDate = getCurrentDayInLocalDateTime().date,
    val time: LocalTime = LocalTime(hour = 8, minute = 0),
    val duration: ReminderBeforeDuration = ReminderBeforeDuration.TEN_MINUTES
)