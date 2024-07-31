package com.example.lezhinbookmark.main.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lezhinbookmark.R
import com.example.lezhinbookmark.bookmark.repository.LZBookmarkRepository
import com.example.lezhinbookmark.bookmark.ui.LZBookmarkRoute
import com.example.lezhinbookmark.bookmark.viewmodel.LZBookmarkViewModel
import com.example.lezhinbookmark.common.LZConstants
import com.example.lezhinbookmark.main.common.LZNavigate
import com.example.lezhinbookmark.main.common.NavSingleton
import com.example.lezhinbookmark.search.repository.LZSearchRepository
import com.example.lezhinbookmark.search.ui.LZSearchRoute
import com.example.lezhinbookmark.search.viewmodel.LZSearchViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun LZMain(widthSizeClass: WindowWidthSizeClass,) {
    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        LZNavigate(navController)
    }

    // 폴더블 및 태블릿 대응
    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded

    Scaffold(
        bottomBar = { LZBottomAppBar(navController) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LZNavHost(
                navigateToScreen = navigationActions.navigateToScreen,
                navController = navController
            )
        }
    }
    BackOnPressed()
}

@Composable
fun LZBottomAppBar(navController: NavHostController) {
    val items = listOf(
        ScreenRoute.SearchScreen,
        ScreenRoute.BookMarkScreen
    )
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.background
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { },
                label = {
                    when (item.screen) {
                        LZConstants.SEARCH_SCREEN_ID -> Text(text = stringResource(id = R.string.search_tab_name))
                        LZConstants.BOOK_MARK_SCREEN_ID -> Text(text = stringResource(id = R.string.bookmark_tab_name))
                    }
                },
                alwaysShowLabel = true,
                selected = currentRoute == item.screen,
                onClick = {
                    navController.navigate(item.screen) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = MaterialTheme.colorScheme.surface,
                    selectedTextColor = MaterialTheme.colorScheme.surface,
                    unselectedIconColor = MaterialTheme.colorScheme.background,
                    unselectedTextColor = MaterialTheme.colorScheme.background,
                )
            )
        }
    }
}

@Composable
fun LZNavHost(
    navController: NavHostController = rememberNavController(),
    navigateToScreen: (String, String?) -> Unit
) {
    LaunchedEffect(Unit) {
        NavSingleton.navSharedFlow.onEach {
            navigateToScreen(it.screenType, it.screenId)
        }.launchIn(this)
    }

    NavHost(
        navController = navController,
        startDestination = ScreenRoute.SearchScreen.screen,
        modifier = Modifier
    ) {
        // search screen
        composable(ScreenRoute.SearchScreen.screen) {
            val viewModel: LZSearchViewModel = viewModel(
                factory = LZSearchViewModel.provideFactory(LZSearchRepository())
            )
            LZSearchRoute(viewModel)
        }

        // bookmark screen
        composable(ScreenRoute.BookMarkScreen.screen) {
            val viewModel: LZBookmarkViewModel = viewModel(
                factory = LZBookmarkViewModel.provideFactory(LZBookmarkRepository())
            )
            LZBookmarkRoute(viewModel)
        }
    }
}

@Composable
fun BackOnPressed() {
    val context = LocalContext.current
    var backPressedState by remember { mutableStateOf(true) }
    var backPressedTime = 0L

    BackHandler(enabled = backPressedState) {
        if(System.currentTimeMillis() - backPressedTime <= 400L) {
            // 앱 종료
            (context as Activity).finish()
        } else {
            backPressedState = true

            Toast.makeText(context, context.getString(R.string.back_pressed_message), Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}


sealed class ScreenRoute(val screen: String) {
    object SearchScreen: ScreenRoute(LZConstants.SEARCH_SCREEN_ID) {
        fun createRoute(screenId: String) = "SEARCH/$screenId"
    }
    object BookMarkScreen : ScreenRoute(LZConstants.BOOK_MARK_SCREEN_ID) {
        fun createRoute(screenId: String) = "BOOKMARK/$screenId"
    }
}