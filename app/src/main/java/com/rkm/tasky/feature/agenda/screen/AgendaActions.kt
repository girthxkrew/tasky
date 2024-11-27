package com.rkm.tasky.feature.agenda.screen

data class AgendaActions(
    val updateSelectedDate: (Long) -> Unit,
    val onDateSelected: (Long) -> Unit,
    val onShowDatePicker: () -> Unit,
)
