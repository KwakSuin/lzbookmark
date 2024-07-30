package com.example.lezhinbookmark.main.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lezhinbookmark.R
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
        containerColor = MaterialTheme.colorScheme.background,
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
        title = { Text(text = "검색", color = MaterialTheme.colorScheme.background) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            scrolledContainerColor = MaterialTheme.colorScheme.secondary
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
                    } },
                alwaysShowLabel = true,
                selected = currentRoute == item.screen,
                onClick = { navController.navigate(item.screen) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
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
            val context = LocalContext.current
            val viewModel: LZSearchViewModel = viewModel(
                factory = LZSearchViewModel.provideFactory(LZSearchRepository(context))
            )
            LZSearchScreen(viewModel)
        }

        // bookmark screen
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