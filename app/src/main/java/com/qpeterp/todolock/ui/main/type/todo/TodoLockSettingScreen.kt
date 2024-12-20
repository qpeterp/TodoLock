package com.qpeterp.todolock.ui.main.type.todo

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpeterp.todolock.common.Constant
import com.qpeterp.todolock.ui.main.theme.Colors

@Composable
fun TodoLockSettingScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 64.dp)
    ) {
        StartTodoLock(onClick = {
            // SharedPreferences 가져오기
            val pref = context.getSharedPreferences("lockState", Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putBoolean("lockState", true)
            editor.apply()
            Log.d(Constant.TAG, "TodoLockSetting : button is clicked lockState : ${pref.getBoolean("lockState", false)}")
        })
    }
}

@Composable
fun StartTodoLock(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize() // 화면 전체를 채우도록 Box 설정
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(
                text = "TodoList를 모두 완료하지 않으면\n해제되지 않습니다!",
                color = Colors.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(20.dp)
            )
            LargeFloatingActionButton(
                onClick = onClick,
                shape = CircleShape,
                containerColor = Colors.LightPrimaryColor,
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterHorizontally)

            ) {
                Icon(
                    Icons.Outlined.Lock,
                    "잠금 버튼",
                    tint = Colors.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}