package com.putnamnation.directory.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.putnamnation.directory.R
import com.putnamnation.directory.ui.common.UserViewModel
import com.putnamnation.directory.ui.list.ListScreen
import com.putnamnation.directory.ui.map.MapScreen

const val LATITUDE_KEY = "latitude"
const val LONGITUDE_KEY = "longitude"

enum class NavigationScreen(val title: String, val screenRoute: String, val icon: Int) {
    DIRECTORY("Directory", "Directory", R.drawable.ic_baseline_person_24),
    MAP("Map", "Map", R.drawable.ic_baseline_map_24)
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraph(navController: NavHostController, userViewModel: UserViewModel) {
    NavHost(
        navController,
        startDestination = NavigationScreen.DIRECTORY.screenRoute,
        modifier = Modifier.padding(bottom = 56.dp)
    ) {
        composable(NavigationScreen.DIRECTORY.screenRoute) {
            EnterAnimation {
                ListScreen(userViewModel, navController)
            }
        }
        composable(NavigationScreen.MAP.screenRoute) {
            EnterAnimation {
                MapScreen(userViewModel)
            }
        }

    }
}