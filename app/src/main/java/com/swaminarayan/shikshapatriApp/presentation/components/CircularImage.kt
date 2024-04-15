package com.swaminarayan.shikshapatriApp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircularImage(
    image: Int,
    size: Dp = 150.dp,
    onImageClicked: () -> Unit
) {
    Image(
        painter = painterResource(id = image),
        contentDescription = null,
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .clickable {
                onImageClicked()
            },
        contentScale = ContentScale.Crop
    )
}