package com.rkm.tasky.feature.agenda.screen

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
internal fun SixDaySelector(
    modifier: Modifier,
    days: List<DayUiModel>,
    onClick: (Long) -> Unit
) {

    var selectedIndex by remember { mutableStateOf(0) }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        itemsIndexed(days) { index, day ->
            DayCard(
                dayUiModel = day,
                isSelected = index == selectedIndex,
                onClick = {
                    selectedIndex = index
                    onClick(day.day)
                }
            )
        }
    }

}

@Composable
internal fun DayCard(
    dayUiModel: DayUiModel,
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
            text = dayUiModel.dayOfWeek,
            color = if(isSelected) SixDaySelectorSelectedDay else SixDaySelectorUnselectedDay,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = dayUiModel.dayOfWeekNumeric.toString(),
            color = SixDaySelectorDateTextColor,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun DayCardPreview(
) {
    val day = DayUiModel(0L, "S", 5)
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
private fun SixDaySelectorPreview() {
    val list = mutableListOf(
        DayUiModel(0L, "M", 5),
        DayUiModel(0L, "T", 6),
        DayUiModel(0L, "W", 7),
        DayUiModel(0L, "T", 8),
        DayUiModel(0L, "F", 9),
        DayUiModel(0L, "S", 10)
    )

    SixDaySelector(Modifier, list, {})
}

data class DayUiModel(
    val day: Long,
    val dayOfWeek: String,
    val dayOfWeekNumeric: Int,
)