package com.qpeterp.todolock.ui.main.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.qpeterp.todolock.R
import com.qpeterp.todolock.data.todo.TodoData

@Preview(showBackground = true)
@Composable
fun TodoScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 64.dp)
    ) {
        TodoList()
    }
}

@Composable
fun TodoList() {
    val users = listOf(
        TodoData("전력으로 밥먹기", false),
        TodoData("끝내주게 잠자기", false),
        TodoData("아크로바틱하게 숨쉬기", true),
        TodoData("미성년자 탈출하기", false),
    )

    // 전체 화면을 차지하는 Row
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 타이틀을 위한 Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(Color(ContextCompat.getColor(LocalContext.current, R.color.gray_dark))),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "할일",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        // 할 일 목록을 표시하는 LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(users) { index, user ->
                UserItem(user)
            }
        }
    }
}

@Composable
fun UserItem(user: TodoData) {
    Column {
        // 각 항목을 Row로 구성
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically, // 수직으로 가운데 정렬
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = user.todo,
                color = Color.White,
                fontSize = 14.sp,
                textDecoration = if (user.isChecked) TextDecoration.LineThrough else TextDecoration.None
            )

            Checkbox(
                checked = user.isChecked,
                onCheckedChange = { user.isChecked = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(ContextCompat.getColor(LocalContext.current, R.color.gray_dark)),      // 체크된 상태에서의 색상
                    uncheckedColor = Color.Gray,   // 체크되지 않은 상태에서의 색상
                    checkmarkColor = Color.White   // 체크 표시의 색상
                )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(ContextCompat.getColor(LocalContext.current, R.color.gray_dark))) // 경계선 색상
        )
    }
}