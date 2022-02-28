package com.putnamnation.directory.util

import android.os.Bundle
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions

inline val <reified T> T.TAG: String
    get() = if (T::class.isCompanion) T::class.java.enclosingClass.simpleName
    else T::class.java.simpleName


fun NavController.navigate(route: String, args: Bundle, navOptions: NavOptions) {
    val routeLink = NavDeepLinkRequest
        .Builder
        .fromUri(NavDestination.createRoute(route).toUri())
        .build()

    val deepLinkMatch = graph.matchDeepLink(routeLink)
    if (deepLinkMatch != null) {
        val destination = deepLinkMatch.destination
        val id = destination.id
        navigate(id, args, navOptions, null)
    } else {
        navigate(route, navOptions, null)
    }
}
