package com.qpeterp.todolock.ui.main.todo

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qpeterp.todolock.common.Constant
import com.qpeterp.todolock.data.room.TodoData
import com.qpeterp.todolock.ui.main.theme.Colors
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun TodoScreen(viewModel: TodoViewModel = viewModel(factory = TodoViewModelFactory(LocalContext.current))) {
    val todoListState by viewModel.todoList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 64.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            TodoList(todoListState)
        }
    }
    CreateTodoDialog(viewModel)
}

@Composable
fun TodoList(todoList: List<TodoData>) {
    var expanded by rememberSaveable { mutableStateOf(false) }

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
            enter = expandVertically(animationSpec = tween(300)) + slideIn { IntOffset(0, 0) },
            exit = shrinkVertically(animationSpec = tween(700)) + slideOut { IntOffset(0, 0) }
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
                onCheckedChange =
                {
                    todo.isChecked = it
                    Log.d(Constant.TAG, "TodoScreen TodoItem CheckBox UUID: ${todo.uuid}")
                },
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateTodoDialog(viewModel: TodoViewModel) {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    var text by remember { mutableStateOf("") }

    ModalBottomSheetLayout(
        sheetBackgroundColor = Colors.GrayDark,
        sheetState = sheetState,
        sheetContent = {
            // 이 부분에 Bottom Sheet에 들어갈 내용을 작성합니다.
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    "할일 작성",
                    style = MaterialTheme.typography.h6,
                    color = Colors.White
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = text,
                    onValueChange = { newText ->
                        text = newText
                    },
                    label = { Text(
                        text = "할일",
                        color = Colors.LightPrimaryColor
                    ) },
                    placeholder = { Text(
                        text = "할일을 입력해주세요.",
                        color = Colors.GrayLight
                    ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Colors.White,  // 입력된 텍스트 색상
                        focusedBorderColor = Colors.LightPrimaryColor,  // 포커스 상태의 테두리 색상
                        unfocusedBorderColor = Colors.LightPrimaryColor,  // 비포커스 상태의 테두리 색상
                        cursorColor = Colors.LightPrimaryColor,  // 커서 색상
                    ),
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Colors.LightPrimaryColor,
                        contentColor = Color.White
                    ),
                    onClick = {
                    viewModel.addTodo(todo = text)
                    text = ""
                    coroutineScope.launch { sheetState.hide() }
                }) {
                    Text(
                        text = "추가",
                        color = Colors.White
                    )
                }
            }
        }
    ) {
        CreateTodoButton(onClick = {
            coroutineScope.launch { sheetState.show() }
        })
    }
}

@Composable
fun CreateTodoButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize() // 화면 전체를 채우도록 Box 설정
    ) {
        ExtendedFloatingActionButton(
            onClick = { onClick() },
            icon = { Icon(Icons.Outlined.Add, contentDescription = "할일 추가") },
            text = { Text(
                text = "할일 추가",
                fontSize = 14.sp
            )},
            contentColor = Colors.White,
            containerColor = Colors.LightPrimaryColor,
            modifier = Modifier
                .align(Alignment.BottomEnd) // 오른쪽 하단에 배치
                .padding(16.dp) // 적절한 여백 추가
        )
    }
}
