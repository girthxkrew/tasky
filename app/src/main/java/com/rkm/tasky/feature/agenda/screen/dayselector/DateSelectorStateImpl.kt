package com.rkm.tasky.feature.agenda.screen.dayselector

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable

@Stable
internal class DateSelectorStateImpl(
    initialSelectedDate: Long,
    private val timeSpan: TimeSpan,
) : DateSelectorState {

    private var _selectedDate = mutableLongStateOf(
        initialSelectedDate
    )

    private var _selectedIndex = mutableIntStateOf(
        0
    )
    override var selectedDate: Long
        get() = _selectedDate.longValue
        set(date) {
            _selectedDate.longValue = date
            _selectedIndex.intValue = 0
        }
    val dateList by derivedStateOf {
        generateDayItemList(_selectedDate.longValue, timeSpan)
    }

    var selectedIndex: Int
        get() = _selectedIndex.intValue
        set(index) {
            _selectedIndex.intValue = index
        }

    companion object {
        fun Saver(timeSpan: TimeSpan): Saver<DateSelectorStateImpl, Any> = listSaver(
            save = {
                listOf(
                    it.selectedIndex,
                    it.selectedDate
                )
            },
            restore = { value ->
                DateSelectorStateImpl(
                    initialSelectedDate = value[1] as Long,
                    timeSpan = timeSpan
                ).apply {
                   this.selectedIndex = value[0] as Int
                }
            }

        )
    }
}

@Composable
fun rememberDateSelectorState(
    initialSelectedDate: Long,
    timeSpan: TimeSpan = TimeSpan.SIX_DAYS
): DateSelectorState {
    return rememberSaveable(saver = DateSelectorStateImpl.Saver(timeSpan)) {
        DateSelectorStateImpl(
            initialSelectedDate = initialSelectedDate,
            timeSpan = timeSpan
        )
    }
}