package com.putnamnation.directory.ui.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.putnamnation.directory.model.Location
import com.putnamnation.directory.model.User
import com.putnamnation.directory.ui.common.Title
import com.putnamnation.directory.ui.common.UserView
import com.putnamnation.directory.ui.common.UserViewModel
import com.putnamnation.directory.ui.navigation.NavigationScreen
import com.putnamnation.directory.ui.theme.AppTheme


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserList(
    userList: LiveData<List<User>>,
    modifier: Modifier = Modifier,
    mapClick: (Location) -> Unit
) {
    val users = userList.observeAsState(listOf())

    LazyVerticalGrid(
        cells = GridCells.Adaptive(300.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = modifier.fillMaxWidth(),
        content = {
            items(items = users.value) { user -> UserView(user, mapClick) }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ListScreen(userViewModel: UserViewModel, navController: NavHostController) {
    Column(
        modifier = Modifier
            .testTag("ListScreen")
            .fillMaxHeight()
    ) {
        Title(title = "Directory List")
        UserList(userList = userViewModel.userList) { location ->
            navController.navigate(NavigationScreen.MAP.screenRoute) {
                navController.graph.startDestinationRoute?.let { screen_route ->
                    popUpTo(screen_route) {
                        saveState = true
                    }
                }
                launchSingleTop = true
                restoreState = true
            }
            userViewModel.updateLocation(loc = location)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    AppTheme {
        val userViewModel = UserViewModel()
        val navController = rememberNavController()

        ListScreen(userViewModel, navController)
    }
}