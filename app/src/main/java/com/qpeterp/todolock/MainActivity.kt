package com.qpeterp.todolock

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.qpeterp.todolock.common.Constant
import com.qpeterp.todolock.specific.lock.LiveSharedPreferences
import com.qpeterp.todolock.specific.lock.OverlayService
import com.qpeterp.todolock.ui.main.setting.SettingScreen
import com.qpeterp.todolock.ui.main.theme.Colors
import com.qpeterp.todolock.ui.main.todo.TodoScreen
import com.qpeterp.todolock.ui.main.type.main.TypeScreen
import com.qpeterp.todolock.ui.theme.TodoLockTheme

class MainActivity : ComponentActivity() {
    companion object {
        private const val REQUEST_CODE_OVERLAY_PERMISSION = 1000
    }

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
                            containerColor = Colors.GrayDark,
                            contentColor = Color.LightGray,
                            indicatorColor = Colors.LightPrimaryColor,
                            navController = navController
                        )
                    },
                    containerColor = Colors.Black
                ) {
                    Box(modifier = Modifier.padding(it)) {
                        NavigationHost(
                            navController = navController
                        )
                    }
                }
            }
        }

        val pref = getSharedPreferences("lockState", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("lockState", false)
        editor.apply()

        // 권한 체크 및 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION)
            } else {
                handleLockState()
            }
        }

        handleLockState()
    }

    private fun handleLockState() {
        val sharedPreference =
            getSharedPreferences("lockState", Context.MODE_PRIVATE)
        val liveSharedPreference = LiveSharedPreferences(sharedPreference)

        // Observer 달아주는 과정
        liveSharedPreference
            .getBoolean("lockState", false)
            .observe(this, Observer<Boolean> { result ->
                if (result) {
                    startOverlayService()
                    Log.d(Constant.TAG, "handleLockState: overlayView 키는 거 실행함.")
                } else {
                    stopOverlayService()
                    Log.d(Constant.TAG, "handleLockState: overlayView 끄는 거 실행함.")
                }
            })
    }

    private fun startOverlayService() {
        val intent = Intent(this, OverlayService::class.java)
        startService(intent)
    }

    private fun stopOverlayService() {
        val intent = Intent(this, OverlayService::class.java)
        stopService(intent)
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
    NavHost(navController as NavHostController,
        startDestination = BottomNavItem.Todo.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(BottomNavItem.Lock.route) { TypeScreen() }
        composable(BottomNavItem.Todo.route) { TodoScreen() }
        composable(BottomNavItem.Setting.route) { SettingScreen() }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object Lock : BottomNavItem(
        "lockSetting",
        Icons.Outlined.Lock,
        "잠금"
    )
    data object Todo : BottomNavItem(
        "todo",
        Icons.AutoMirrored.Outlined.List,
        "할일작성"
    )
    data object Setting : BottomNavItem(
        "setting",
        Icons.Outlined.Settings,
        "설정"
    )
}