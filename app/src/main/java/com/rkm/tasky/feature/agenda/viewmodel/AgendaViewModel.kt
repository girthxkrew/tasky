package com.rkm.tasky.feature.agenda.viewmodel

import androidx.lifecycle.ViewModel
import com.rkm.tasky.feature.agenda.screen.DateHelper
import com.rkm.tasky.feature.agenda.screen.DayUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

//Todo redo agenda viewmodel. Temporary for the time being
@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val helper: DateHelper
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(0L)
    val selectedDate = _selectedDate.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    private val _selectedMonth = MutableStateFlow("")
    val selectedMonth = _selectedMonth.asStateFlow()

    private val _sixDays = MutableStateFlow(emptyList<DayUiModel>())
    val sixDay = _sixDays.asStateFlow()


    init {
        setCurrentDay()
    }

    fun updateSelectedDate(date: Long) {
        _selectedDate.update { date }
        _selectedMonth.update { helper.getMonth(date) }
    }

    fun updateSixDays(date: Long) {
        _sixDays.update {
            helper.getSixDaySelectorList(date)
        }
    }

    fun updateShowDialog() {
        _showDialog.update { !it }
    }

    private fun setCurrentDay() {
        val day = helper.getCurrentDay()
        _selectedMonth.update { helper.getMonth(day) }
        _selectedDate.update { day }
        _sixDays.update { helper.getSixDaySelectorList(day) }
    }
}