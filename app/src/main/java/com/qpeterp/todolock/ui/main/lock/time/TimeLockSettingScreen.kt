package com.qpeterp.todolock.ui.main.lock.time

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.qpeterp.todolock.ui.main.theme.Colors

@Composable
fun TimeSettingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 64.dp)
        ) {
            Text(text = "특정 시간 지나면 잠금 풀리게 하는 곳", color = Colors.White)
        }
}