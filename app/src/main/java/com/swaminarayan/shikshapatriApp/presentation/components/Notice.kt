package com.swaminarayan.shikshapatriApp.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swaminarayan.shikshapatriApp.ui.theme.Red

@Composable
fun Notice(
    text: String,
    isNoticeVisible: Boolean,
    leftArrowColor: Color,
    isLeftIconVisible: Boolean,
    isRightIconVisible: Boolean
) {

    AnimatedVisibility(visible = isNoticeVisible) {
        Box {
            Card(
                elevation = CardDefaults.cardElevation(2.5.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
                shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (isLeftIconVisible) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = leftArrowColor
                        )
                    }
                    Text(
                        text = text,
                        fontSize = 17.sp,
                        modifier = Modifier
                            .weight(9f)
                            .padding(horizontal = 5.dp),
                        textAlign = TextAlign.Center
                    )
                    if (isRightIconVisible) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Red
                        )
                    }
                }
            }
        }
    }

}