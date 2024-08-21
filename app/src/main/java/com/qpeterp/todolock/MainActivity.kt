package com.qpeterp.todolock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.qpeterp.todolock.ui.main.lock.LockScreen
import com.qpeterp.todolock.ui.main.setting.SettingScreen
import com.qpeterp.todolock.ui.main.todo.TodoScreen
import com.qpeterp.todolock.ui.theme.TodoLockTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            TodoLockTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        MyBottomNavigation(
                            containerColor = Color(ContextCompat.getColor(LocalContext.current, R.color.gray_dark)),
                            contentColor = Color(ContextCompat.getColor(LocalContext.current, R.color.gray_light)),
                            indicatorColor = Color(ContextCompat.getColor(LocalContext.current, R.color.purple_500)),
                            navController = navController
                        )
                    },
                    containerColor = Color.Black
                ) {
                    Box(modifier = Modifier.padding(it)) {
                        NavigationHost(
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MyBottomNavigation(
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color,
    indicatorColor: Color,
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val items = listOf(
        BottomNavItem.Lock,
        BottomNavItem.Todo,
        BottomNavItem.Setting
    )

    AnimatedVisibility(
        visible = items.map { it.route }.contains(currentRoute)
    ) {
        NavigationBar(
            modifier = modifier,
            contentColor = contentColor,
            containerColor = containerColor
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    label = {
                        Text(
                            text = item.label,
                            style = TextStyle(
                                fontSize = 12.sp,
                            )
                        )
                    },
                    icon = {
                        Icon(
                            item.icon,
                            contentDescription = item.label
                        )
                    },
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = indicatorColor,
                        selectedTextColor = indicatorColor,
                        unselectedIconColor = contentColor,
                        unselectedTextColor = contentColor,
                        indicatorColor = containerColor // 선택된 항목의 배경색
                    )
                )
            }
        }
    }
}

@Composable
fun NavigationHost(navController: NavController) {
    NavHost(navController as NavHostController, startDestination = BottomNavItem.Lock.route) {
        composable(BottomNavItem.Lock.route) { LockScreen() }
        composable(BottomNavItem.Todo.route) { TodoScreen() }
        composable(BottomNavItem.Setting.route) { SettingScreen() }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Lock : BottomNavItem(
        "lockSetting",
        Icons.Outlined.Lock,
        "잠금"
    )
    object Todo : BottomNavItem(
        "todo",
        Icons.AutoMirrored.Outlined.List,
        "할일작성"
    )
    object Setting : BottomNavItem(
        "setting",
        Icons.Outlined.Settings,
        "설정"
    )
}