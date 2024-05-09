package com.swaminarayan.shikshapatriApp.calender.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GridCalender(
    visibleDateList: List<LocalDate>,
    centerText: String,
    onDateClicked: (LocalDate) -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    showPrevNextBtn: Boolean,
    showTickMarks: (LocalDate) -> Boolean
) {

    Column {
        if (showPrevNextBtn) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = { onPreviousClick() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowLeft,
                        contentDescription = "Left arrow key"
                    )
                }

                Text(text = centerText)

                if (visibleDateList.last() < LocalDate.now()) {
                    IconButton(onClick = { onNextClick() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowRight,
                            contentDescription = "Right arrow key"
                        )
                    }
                } else {
                    Icon(
                        modifier = Modifier.padding(end = 20.dp),
                        imageVector = Icons.Default.ArrowRight,
                        contentDescription = "Right arrow key",
                        tint = MaterialTheme.colorScheme.background
                    )
                }

            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        LazyVerticalGrid(columns = GridCells.FixedSize(70.dp)) {
            items(visibleDateList) { date ->
                CalenderItem(
                    date = date,
                    onDateClicked = {
                        onDateClicked(date)
                    },
                    today = LocalDate.now(),
                    showTickMarks = showTickMarks(date)
                )
            }
        }
    }


}