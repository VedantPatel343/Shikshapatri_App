package com.swaminarayan.shikshapatriApp.calender.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.swaminarayan.shikshapatriApp.utils.dayFormatter
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Calender(
    visibleDateList: List<LocalDate>,
    startEndDate: String,
    onDateClicked: (LocalDate) -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    showPrevNextBtn: Boolean
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
                        imageVector = Icons.Default.ArrowLeft,
                        contentDescription = "Left arrow key"
                    )
                }

                Text(text = startEndDate)

                IconButton(onClick = { onNextClick() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowRight,
                        contentDescription = "Right arrow key"
                    )
                }

            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        LazyRow {
            items(visibleDateList) { date ->
                CalenderItem(
                    date = date,
                    onDateClicked = {
                        onDateClicked(date)
                    },
                    today = LocalDate.now()
                )
            }
        }
    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalenderItem(
    date: LocalDate,
    onDateClicked: () -> Unit,
    today: LocalDate
) {

    val textColor = if (date == today) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.primary
    }

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .padding(horizontal = 5.dp)
            .background(
                if (date == today) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = CircleShape
            )
            .border(
                2.5.dp,
                MaterialTheme.colorScheme.primary,
                CircleShape
            )
            .clickable {
                onDateClicked()
            },
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .size(70.dp)
                .padding(10.dp)
        ) {
            Text(
                text = dayFormatter(date),
                color = textColor,
                fontWeight = if (date == today) FontWeight.ExtraBold else FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = date.dayOfMonth.toString(),
                color = textColor,
                fontWeight = if (date == today) FontWeight.ExtraBold else FontWeight.Bold
            )
        }

    }

}