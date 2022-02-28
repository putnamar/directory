package com.putnamnation.directory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.WindowMetricsCalculator
import com.putnamnation.directory.ui.common.UserViewModel
import com.putnamnation.directory.ui.navigation.BottomNav
import com.putnamnation.directory.ui.navigation.LeftNav
import com.putnamnation.directory.ui.navigation.NavigationGraph
import kotlin.math.min

enum class WindowSizeClass { COMPACT, MEDIUM, EXPANDED }


class MainActivity : ComponentActivity() {
    private val viewModel: UserViewModel by viewModels()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreenView(userViewModel = viewModel, computeWindowSizeClasses())
        }

    }

    private fun computeWindowSizeClasses(): WindowSizeClass {
        val metrics = WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(this)

        val widthDp = metrics.bounds.width() /
                resources.displayMetrics.density
        val widthWindowSizeClass = when {
            widthDp < 600f -> WindowSizeClass.COMPACT
            widthDp < 840f -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }

        val heightDp = metrics.bounds.height() /
                resources.displayMetrics.density
        val heightWindowSizeClass = when {
            heightDp < 480f -> WindowSizeClass.COMPACT
            heightDp < 900f -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }

        return WindowSizeClass.values()[min(
            heightWindowSizeClass.ordinal,
            widthWindowSizeClass.ordinal
        )]
    }
}

@Composable
fun MainScreenView(userViewModel: UserViewModel, windowSize: WindowSizeClass) {
    val navController = rememberNavController()
    if (windowSize == WindowSizeClass.COMPACT) {
        Scaffold(
            bottomBar = { BottomNav(navController = navController) }
        ) {
            NavigationGraph(navController = navController, userViewModel = userViewModel)
        }
    } else {
        Scaffold() {
            Row {
                LeftNav(navController)
                NavigationGraph(navController = navController, userViewModel = userViewModel)
            }
        }
    }
}