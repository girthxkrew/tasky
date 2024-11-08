package com.rkm.tasky.feature.agenda.viewmodel

import androidx.lifecycle.ViewModel
import com.rkm.tasky.feature.agenda.screen.dayselector.DateItem
import com.rkm.tasky.util.date.getCurrentDay
import com.rkm.tasky.util.date.getMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

//Todo redo agenda viewmodel. Temporary for the time being
@HiltViewModel
class AgendaViewModel @Inject constructor(
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(0L)
    val selectedDate = _selectedDate.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    private val _selectedMonth = MutableStateFlow("")
    val selectedMonth = _selectedMonth.asStateFlow()



    init {
        setCurrentDay()
    }

    fun updateSelectedDate(date: Long) {
        _selectedDate.update { date }
        _selectedMonth.update { getMonth(date) }
    }

    fun updateShowDialog() {
        _showDialog.update { !it }
    }

    fun loadDate(date: Long) {
    }

    private fun setCurrentDay() {
        val day = getCurrentDay()
        _selectedMonth.update { getMonth(day) }
        _selectedDate.update { day }
    }
}