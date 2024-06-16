package com.swaminarayan.shikshapatriApp.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swaminarayan.shikshapatriApp.constants.maharajList
import com.swaminarayan.shikshapatriApp.domain.models.PieChartInput
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PieChart(
    list: List<PieChartInput>,
    radius: Float = 500f,
    innerRadius: Float = 250f,
    transparentWidth: Float = 60f,
    spaceBetween: Dp = 30.dp,
    animationDuration: Int = 800,
    showArrowBtn: Boolean,
    onPreviousMonthClicked: () -> Unit,
    onNextMonthClicked: () -> Unit,
    currentMonth: String,
    currentYear: String
) {

    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    var maharaj by rememberSaveable {
        mutableIntStateOf(maharajList.random().image)
    }

    var showImageDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    var animationPlayed by rememberSaveable {
        mutableStateOf(false)
    }

    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 2f else 0f,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ), label = ""
    )

    val space by animateFloatAsState(
        targetValue = if (!animationPlayed) 90f else 0f,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ), label = ""
    )

    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 11f else 0f,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ), label = ""
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Column {

        if (showArrowBtn) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        onPreviousMonthClicked()
                        scope.launch {
                            animationPlayed = false
                            delay(600)
                            maharaj = maharajList.random().image
                            animationPlayed = true
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowLeft,
                        contentDescription = "Left arrow key"
                    )
                }
                Text(
                    text = "$currentMonth $currentYear",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(
                    onClick = {
                        onNextMonthClicked()
                        scope.launch {
                            animationPlayed = false
                            delay(600)
                            maharaj = maharajList.random().image
                            animationPlayed = true
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                        contentDescription = "Right arrow key"
                    )
                }
            }
            Spacer(modifier = Modifier.height(space.dp))
            Spacer(modifier = Modifier.height(20.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 70.dp)
                .size(animateSize.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .rotate(animateRotation)
            ) {

                val width = size.width
                val height = size.height
                circleCenter = Offset(x = width / 2f, y = height / 2f)

                val totalValue = list.sumOf { it.value }
                val anglePerValue = 360f / totalValue
                var currentStartAngle = 0f

                list.forEach { item ->
                    val angleToDraw = item.value * anglePerValue
                    scale(0.9f) {
                        drawArc(
                            color = item.color,
                            startAngle = currentStartAngle,
                            sweepAngle = angleToDraw,
                            useCenter = true,
                            size = Size(
                                width = radius * 2f,
                                height = radius * 2f
                            ),
                            topLeft = Offset(
                                (width - radius * 2f) / 2f,
                                (height - radius * 2f) / 2f
                            )
                        )
                        currentStartAngle += angleToDraw
                    }
                    var rotateAngle = currentStartAngle - angleToDraw / 2f - 90f
                    var factor = 1f
                    if (rotateAngle > 90f) {
                        rotateAngle = (rotateAngle + 180) % 360
                        factor = -0.92f
                    }

                    val percentage = getCeilOrFloorValue(item.value / totalValue.toFloat() * 100)

                    drawContext.canvas.nativeCanvas.apply {
                        if (percentage > 3) {
                            rotate(rotateAngle) {
                                drawText(
                                    "$percentage %",
                                    circleCenter.x,
                                    circleCenter.y + (radius - (radius - innerRadius) / 2f) * factor,
                                    android.graphics.Paint().apply {
                                        textSize = 15.sp.toPx()
                                        textAlign = android.graphics.Paint.Align.CENTER
                                        color = Color.White.toArgb()
                                    }
                                )
                            }
                        }
                    }
                }

                drawCircle(
                    color = Color.White.copy(0.4f),
                    radius = innerRadius + transparentWidth / 2,
                )

            }

            Image(
                painter = painterResource(id = maharaj),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(animateSize.dp)
                    .clickable { showImageDialog = true }
                    .border(5.dp, Color.White, shape = CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(spaceBetween))
        Spacer(modifier = Modifier.height(space.dp))

        if (showImageDialog) {
            ImageDialog(dismissClicked = { showImageDialog = false }, image = maharaj)
        }

    }
}

fun getCeilOrFloorValue(value: Float): Int {
    val per = String.format("%.2f", value).toFloat()
    val bal = String.format("%.2f", per - per.toInt()).toFloat()
    return if (bal > 0.50) {
        (value + 1).toInt()
    } else {
        value.toInt()
    }
}

@Composable
fun SmallPieChart(pieChartList: List<PieChartInput>) {

    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(modifier = Modifier.size(50.dp)) {

            val width = size.width
            val height = size.height
            circleCenter = Offset(x = width / 2f, y = height / 2f)

            val totalValue = pieChartList.sumOf { it.value }
            val anglePerValue = 360f / totalValue
            var currentStartAngle = 0f

            pieChartList.forEach { item ->
                val angleToDraw = item.value * anglePerValue
                scale(0.6f) {
                    drawArc(
                        color = item.color,
                        startAngle = currentStartAngle,
                        sweepAngle = angleToDraw,
                        useCenter = true,
                        size = Size(
                            width = 100f * 2f,
                            height = 100f * 2f
                        ),
                        topLeft = Offset(
                            (width - 100f * 2f) / 2f,
                            (height - 100f * 2f) / 2f
                        )
                    )
                    currentStartAngle += angleToDraw
                }
            }

            drawCircle(
                color = Color.White,
                radius = 30f,
            )

        }
    }
}