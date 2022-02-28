package com.putnamnation.directory.ui.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.putnamnation.directory.ui.common.Title
import com.putnamnation.directory.ui.common.UserViewModel
import com.putnamnation.directory.ui.theme.AppTheme

@Composable
fun MapScreen(userViewModel: UserViewModel) {
    Column(
        modifier = Modifier
    ) {
        val currentLocation = userViewModel.location.observeAsState()

        val cameraPositionState = CameraPositionState()
        Title(title = "MAPS")
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .testTag("MapScreen"),
            cameraPositionState = cameraPositionState,
        ) {
            currentLocation.value?.let {
                cameraPositionState.move(CameraUpdateFactory.newLatLng(it))
            }
            userViewModel.userList.value?.forEach { user ->
                user.address?.geo?.lat?.let { lat ->
                    user.address.geo.lng?.let { lng ->
                        Marker(
                            position = LatLng(lat, lng),
                            title = user.name,
                            snippet = user.username,
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    AppTheme {
        val userViewModel = UserViewModel()

        MapScreen(userViewModel)
    }
}