package com.qpeterp.todolock.ui.main.ui.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.qpeterp.todolock.R

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
//    LazyColumn(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.spacedBy(8.dp),
//        content = {
//            itemsIndexed(users) { index, user ->
//                CustomUserProfile(
//                    id = user.id,
//                    name = user.name,
//                    age = user.age,
//                    nickName = user.nickName,
//                    hobby = user.hobby
//                )
//            }
//        })
}

@Composable
fun TodoList() {
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
}
