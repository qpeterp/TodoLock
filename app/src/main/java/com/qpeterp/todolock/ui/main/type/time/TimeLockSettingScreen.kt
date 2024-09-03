package com.qpeterp.todolock.ui.main.type.time

import android.annotation.SuppressLint
import android.util.Log
import android.widget.NumberPicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.qpeterp.todolock.common.Constant
import com.qpeterp.todolock.ui.main.theme.Colors
import com.qpeterp.todolock.ui.main.theme.Colors.GrayDark

@Composable
fun TimeSettingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 64.dp)
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors =  CardDefaults.cardColors(
                    contentColor = GrayDark,
                    containerColor = GrayDark
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            top = 20.dp,
                            bottom = 20.dp,
                            start = 32.dp,
                            end = 32.dp
                        )
                        .fillMaxWidth()
                ) {
                    SetTime("시작시간", selectedTime = "7:14")
                    SetTime("종료시간", selectedTime = "8:14")
                    
                    Text(
                        text = "21분 동안",
                        color = Colors.GrayLight,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
}

@Composable
fun SetTime(
    label: String,
    selectedTime: String
) {
    val timePickerState = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Colors.White,
            fontSize = 16.sp
        )
        TextButton(onClick = {timePickerState.value = !timePickerState.value} ) {
            Text(
                text = selectedTime,
                color = Colors.LightPrimaryColor,
                fontSize = 16.sp,
            )
        }
    }
    if (timePickerState.value) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ShowTimePicker(0, 23, 6)
            ShowTimePicker(0, 60, 30)
        }
    }
}

// 사용되지 않는 비공개 API를 사용 중이라는 뜻
@SuppressLint("SoonBlockedPrivateApi", "DiscouragedPrivateApi")
@Composable
fun ShowTimePicker(
    minTime: Int,
    maxTime: Int,
    initialTime: Int
) {
    AndroidView(
        factory = { context ->
            NumberPicker(context).apply {
                minValue = minTime
                maxValue = maxTime
                value = initialTime

                setOnValueChangedListener { _, _, newVal ->
                    // 값이 변경되었을 때 처리할 내용을 여기에 작성합니다.
                    Log.d(Constant.TAG, "TimeLockSettingScreen ShowTimePicker: value : $newVal")
                }
            }
        }
    )
}