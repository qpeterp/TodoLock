package com.qpeterp.todolock.ui.main.lock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qpeterp.todolock.ui.main.theme.Colors

@Preview(showBackground = true)
@Composable
fun LockScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.Black)
            .padding(top = 64.dp)
    ) {
        Text(
            text = "잠금 설정하는 곳입니다. ^^7",
            color = Colors.White
        )
    }
}