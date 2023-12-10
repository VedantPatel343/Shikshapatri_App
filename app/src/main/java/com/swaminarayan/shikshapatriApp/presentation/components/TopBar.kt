package com.swaminarayan.shikshapatriApp.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    icon: ImageVector,
    onBtnClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    TopAppBar(
        modifier = modifier,
        title = { Text(text = title, color = MaterialTheme.colorScheme.background) },
        navigationIcon = {
            IconButton(
                onClick = {
                    onBtnClick()
                }
            ) {
                Icon(icon, contentDescription = "Nav Icon", tint = MaterialTheme.colorScheme.background)
            }
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(MaterialTheme.colorScheme.primary)
    )

}