package com.rkm.tasky.feature.agenda.screen.dayselector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rkm.tasky.ui.theme.SixDaySelectorDateTextColor
import com.rkm.tasky.ui.theme.SixDaySelectorDayCardSelected
import com.rkm.tasky.ui.theme.SixDaySelectorDayCardUnselected
import com.rkm.tasky.ui.theme.SixDaySelectorSelectedDay
import com.rkm.tasky.ui.theme.SixDaySelectorUnselectedDay

@Composable
fun DateSelector(
    modifier: Modifier = Modifier,
    state: DateSelectorState,
    onClick: (Long) -> Unit
) {
    DateSelectorImpl(modifier, state as DateSelectorStateImpl, onClick)
}

@Composable
private fun DateSelectorImpl(
    modifier: Modifier,
    state: DateSelectorStateImpl,
    onClick: (Long) -> Unit
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        itemsIndexed(state.dateList) { index, day ->
            DayCard(
                dateItem = day,
                isSelected = index == state.selectedIndex,
                onClick = {
                    state.selectedIndex = index
                    onClick(day.dayLong)
                }
            )
        }
    }
}

@Composable
internal fun DayCard(
    dateItem: DateItem,
    onClick: () -> Unit,
    isSelected: Boolean,
) {
    Column(modifier = Modifier
        .width(35.dp)
        .height(60.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(if (isSelected) SixDaySelectorDayCardSelected else SixDaySelectorDayCardUnselected)
        .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = dateItem.dayOfWeek,
            color = if(isSelected) SixDaySelectorSelectedDay else SixDaySelectorUnselectedDay,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = dateItem.dayOfMonth.toString(),
            color = SixDaySelectorDateTextColor,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun DayCardPreview(
) {
    val day = DateItem(0L, "S", 5)
    DayCard(
        day,
        {},
        false
    )
}

@Preview(
    showBackground = false
)
@Composable
private fun DateSelectorPreview() {
    val state = rememberDateSelectorState(0L)
    DateSelector(Modifier, state, {})
}