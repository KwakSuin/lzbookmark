package com.example.lezhinbookmark.main.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lezhinbookmark.bookmark.ui.LZBookmarkScreen
import com.example.lezhinbookmark.common.LZConstants
import com.example.lezhinbookmark.main.common.LZNavigate
import com.example.lezhinbookmark.main.common.NavSingleton
import com.example.lezhinbookmark.search.repository.LZSearchRepository
import com.example.lezhinbookmark.search.ui.LZSearchScreen
import com.example.lezhinbookmark.search.viewmodel.LZSearchViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
@Preview
fun LZMain() {
    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        LZNavigate(navController)
    }

    Scaffold(
        topBar = { LZTopAppBar() },
        bottomBar = { LZBottomAppBar(navController) },
        containerColor = Color.White,
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LZNavHost(
                navigateToScreen = navigationActions.navigateToScreen,
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LZTopAppBar() {
    // ToDo screen title 수정
    CenterAlignedTopAppBar(
        title = { Text(text = "검색", color = Color.White) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF6799FF),
            scrolledContainerColor = Color(0xFF6799FF)
        )
    )
}

@Composable
fun LZBottomAppBar(navController: NavHostController) {
    val items = listOf(
        ScreenRoute.SearchScreen,
        ScreenRoute.BookMarkScreen
    )
    NavigationBar(
        containerColor = Color(0xFF8BBDFF),
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { },
                label = { Text(text = item.screen) },
                alwaysShowLabel = true,
                selected = currentRoute == item.screen,
                onClick = {
                    navController.navigate(item.screen) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
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
        // ToDo 검색 화면
        composable(ScreenRoute.SearchScreen.screen) {
            val context = LocalContext.current
            val viewModel: LZSearchViewModel = viewModel(
                factory = LZSearchViewModel.provideFactory(LZSearchRepository(context))
            )
            LZSearchScreen(viewModel)
        }

        // ToDo 북마크 목록 화면
        composable(ScreenRoute.BookMarkScreen.screen) {
            LZBookmarkScreen()
        }
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