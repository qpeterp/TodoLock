package com.qpeterp.todolock.ui.main.todo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpeterp.todolock.data.todo.TodoData
import com.qpeterp.todolock.ui.main.theme.Colors

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
    var expanded by rememberSaveable { mutableStateOf(false) }

    val todoList = listOf(
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(Colors.GrayDark),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "할일",
                color = Color.White,
                fontSize = 16.sp
            )
            IconButton(
                onClick = {expanded = !expanded},
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                    contentDescription = "More",
                    tint = Colors.White
                )
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(animationSpec = tween(300)) + fadeIn(),
            exit = shrinkVertically(animationSpec = tween(300)) + fadeOut()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(todoList) { index, todo ->
                    TodoItem(todo)
                }
            }
        }
    }
}

@Composable
fun TodoItem(todo: TodoData) {
    Column {
        // 각 항목을 Row로 구성
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Colors.Black)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically, // 수직으로 가운데 정렬
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = todo.todo,
                color = Color.White,
                fontSize = 14.sp,
                textDecoration = if (todo.isChecked) TextDecoration.LineThrough else TextDecoration.None
            )

            Checkbox(
                checked = todo.isChecked,
                onCheckedChange = { todo.isChecked = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Colors.GrayDark,      // 체크된 상태에서의 색상
                    uncheckedColor = Color.Gray,   // 체크되지 않은 상태에서의 색상
                    checkmarkColor = Color.White   // 체크 표시의 색상
                )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Colors.GrayDark) // 경계선 색상
        )
    }
}