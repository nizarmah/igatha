package com.nizarmah.igatha.ui.screen

import android.net.Uri
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.gson.Gson
import com.nizarmah.igatha.Constants
import com.nizarmah.igatha.model.Device
import com.nizarmah.igatha.ui.view.ContentView
import com.nizarmah.igatha.ui.view.DeviceDetailView
import com.nizarmah.igatha.viewmodel.ContentViewModel

@Composable
fun ContentScreen(modifier: Modifier) {
    val navController = rememberNavController()
    val viewModel: ContentViewModel = viewModel()

    val isSOSAvailable by viewModel.isSOSAvailable.collectAsState()
    val isSOSActive by viewModel.isSOSActive.collectAsState()
    val activeAlert by viewModel.activeAlert.collectAsState()
    val devices by viewModel.devices.collectAsState()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "home",
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        }
    ) {
        composable("home") {
            ContentView(
                isSOSAvailable = isSOSAvailable,
                isSOSActive = isSOSActive,
                devices = devices,
                activeAlert = activeAlert,
                onSOSClick = {
                    if (isSOSActive) {
                        viewModel.stopSOS()
                    } else {
                        viewModel.showSOSConfirmation()
                    }
                },
                onConfirmSOS = {
                    viewModel.dismissAlert()
                    viewModel.startSOS()
                },
                onDismissAlert = {
                    viewModel.dismissAlert()
                },
                onSettingsClick = {
                    navController.navigate("settings")
                },
                onDeviceClick = { device ->
                    val deviceJson = Gson().toJson(device)
                    navController.navigate("device/$deviceJson")
                }
            )
        }

        composable(
            route = "settings",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = Uri.Builder()
                        .scheme(Constants.DeepLink.SCHEME)
                        .authority(Constants.DeepLink.Settings.VALUE)
                        .build().toString()
                }
            )
        ) {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onFeedbackClick = {
                    navController.navigate("feedback")
                }
            )
        }

        composable("feedback") {
            FeedbackFormScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "device/{device}",
            arguments = listOf(
                navArgument("device") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val deviceJson = backStackEntry.arguments?.getString("device")
            val device = Gson().fromJson(deviceJson, Device::class.java)

            DeviceDetailView(
                device = device,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
