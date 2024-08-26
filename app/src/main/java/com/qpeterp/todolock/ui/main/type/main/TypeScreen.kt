package com.qpeterp.todolock.ui.main.type.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpeterp.todolock.ui.main.type.time.TimeSettingScreen
import com.qpeterp.todolock.ui.main.type.todo.TodoLockSettingScreen
import com.qpeterp.todolock.ui.main.theme.Colors

@Preview(showBackground = true)
@Composable
fun TypeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.Black)
            .padding(top = 64.dp)
    ) {
        Tabs()
    }
}

@Preview(showBackground = true)
@Composable
fun Tabs() {
    var index by remember { mutableIntStateOf(0) }
    Column(modifier = Modifier.padding(20.dp)) {
        ClickableTabs(selectedItem = 0, tabsList = listOf("시간","할일"), onClick ={ index = it })
    }

    if (index == 0) {
        TimeSettingScreen()
    } else {
        TodoLockSettingScreen()
    }
}

@Composable
fun ClickableTabs(selectedItem: Int, tabsList : List<String>,onClick: (Int) -> Unit) {
    val selectedItemIndex = remember{
        mutableIntStateOf(selectedItem)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.Black, RoundedCornerShape(70.dp))
            .border(
                border = BorderStroke(
                    1.dp,
                    Colors.GrayLight
                ), RoundedCornerShape(70.dp)
            )
            .height(IntrinsicSize.Min),
        contentAlignment = Alignment.Center
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            tabsList.forEachIndexed { index, s ->
                TabItem(isSelected = index == selectedItemIndex.intValue, text = s, Modifier.weight(0.5f)) {
                    selectedItemIndex.intValue = index
                    onClick.invoke(selectedItemIndex.intValue)
                }
            }
        }
    }
}

@Composable
fun TabItem(isSelected: Boolean, text: String, modifier: Modifier, onClick: () -> Unit) {

    val background : Color by animateColorAsState(targetValue = if (isSelected)
        Color(0xAAFAA275)
    else
        Color.Black,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
        , label = "")
    val border = if (isSelected)
        BorderStroke(
            2.dp,
            Colors.LightPrimaryColor
        )
    else
        BorderStroke(
            0.dp,
            Color.Black
        )
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(1f)
            .background(background, RoundedCornerShape(70.dp))
            .border(
                border = border, RoundedCornerShape(70.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {

                onClick.invoke()
            }
            .padding(vertical = 15.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = typography.titleSmall,
            textAlign = TextAlign.Center,
            color = Colors.White,
            fontSize = 16.sp,
            fontWeight = FontWeight(500)
        )
    }
}