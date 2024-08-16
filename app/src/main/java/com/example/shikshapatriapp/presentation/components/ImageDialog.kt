package com.example.shikshapatriapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun ImageDialog(
    dismissClicked: () -> Unit,
    image: Int
) {
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp / 2

    Dialog(
        onDismissRequest = { dismissClicked() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        )
    ) {
        Card(
            elevation = CardDefaults.cardElevation(6.dp),
            shape = CircleShape,
            modifier = Modifier
                .fillMaxWidth(0.95f),
            colors = CardDefaults.cardColors(Color.Transparent)
        ) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.98f)
                        .height((screenHeight / 1.17f).dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

        }
    }
}