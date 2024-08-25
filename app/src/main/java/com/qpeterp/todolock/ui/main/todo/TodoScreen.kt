package com.qpeterp.todolock.ui.main.todo

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qpeterp.todolock.common.Constant
import com.qpeterp.todolock.data.room.TodoData
import com.qpeterp.todolock.ui.main.theme.Colors
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.math.roundToInt

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
            TodoList(todoListState, viewModel)
        }
    }
    CreateTodoDialog(viewModel)
}

@Composable
fun TodoList(todoList: List<TodoData>, viewModel: TodoViewModel) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var isUpdateClick = remember { mutableStateOf(false) }

    var todoToUpdate = remember {
        mutableStateOf(TodoData(uuid = UUID.fromString("df8d1de2-d5fb-43ce-ad10-09cc21aa4244"), todo = "", isChecked = false))
    }

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
                itemsIndexed(todoList) { _, todo ->
                    TodoItem(
                        todo,
                        onEdit = {
                            isUpdateClick.value = !isUpdateClick.value
                            todoToUpdate.value = todo
                        },
                        onDelete = {
                            viewModel.deleteTodo(todo)
                        }
                    )
                }
            }
        }
        
        if (isUpdateClick.value) {
            UpdateTodoDialog(
                todo = todoToUpdate.value,
                onClickCancel = { isUpdateClick.value = false },
                onClickUpdate = {
                    isUpdateClick.value = false
                    todoToUpdate.value.todo = it
                    viewModel.updateTodo(todoToUpdate.value)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TodoItem(
    todo: TodoData,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val swipeableState = rememberSwipeableState(initialValue = 0)

    val maxSwipe = 100.dp
    val maxSwipePx = with(LocalDensity.current) { maxSwipe.toPx() }

    val anchors = mapOf(0f to 0, -maxSwipePx to 1) // 0: 원래 상태, 1: 스와이프된 상태

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
            .background(Colors.Black)
    ) {
        // 배경 액션 버튼들
        Row(
            modifier = Modifier
                .matchParentSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit",
                    tint = Color.White
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }

        // 실제 내용물
        Row(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .background(Colors.Black)
                .padding(horizontal = 16.dp)
                .clickable(
                    onClick = {},
                    onClickLabel = null,
                ),
            verticalAlignment = Alignment.CenterVertically,
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
                onCheckedChange = {
                    todo.isChecked = it
                    Log.d(Constant.TAG, "TodoScreen TodoItem CheckBox UUID: ${todo.uuid}")
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = Colors.GrayDark,
                    uncheckedColor = Color.Gray,
                    checkmarkColor = Color.White
                )
            )
        }

        // 경계선
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Colors.GrayDark)
        )
    }
}

@Composable
fun UpdateTodoDialog(
    todo: TodoData,
    onClickCancel: () -> Unit,
    onClickUpdate: (text: String) -> Unit,
    ) {
    var text by remember { mutableStateOf(todo.todo) }

    Dialog(
        onDismissRequest = onClickCancel,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(2.dp, Colors.LightPrimaryColor),
            colors =  CardDefaults.cardColors(
                contentColor = Colors.Black,
                containerColor = Colors.Black
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(20.dp),
            ) {
                Text(
                    text = "할일 수정",
                    color = Colors.White,
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = text,
                    onValueChange = { newText ->
                        text = newText
                    },
                    label = { Text(
                        text = "할일 수정",
                        color = Colors.LightPrimaryColor
                    ) },
                    placeholder = { Text(
                        text = "할일을 입력해주세요.",
                        color = Colors.GrayLight
                    ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Colors.White,  // 입력된 텍스트 색상
                        focusedBorderColor = Colors.LightPrimaryColor,  // 포커스 상태의 테두리 색상
                        unfocusedBorderColor = Colors.LightPrimaryColor,  // 비포커스 상태의 테두리 색상
                        cursorColor = Colors.LightPrimaryColor,  // 커서 색상
                    ),
                )

                Spacer(modifier = Modifier.height(64.dp))
                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Colors.Black,
                        ),
                        border = BorderStroke(2.dp, Colors.LightPrimaryColor),
                        onClick ={ onClickCancel() },
                    ) {
                        Text(
                            text = "취소",
                            color = Colors.White
                        )
                    }

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Colors.LightPrimaryColor,
                            contentColor = Color.White
                        ),
                        onClick ={ onClickUpdate(text) },
                    ) {
                        Text(
                            text = "수정",
                            color = Colors.White
                        )
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateTodoDialog(viewModel: TodoViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
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
            onClick = onClick,
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
