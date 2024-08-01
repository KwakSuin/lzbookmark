package com.example.lezhinbookmark.main.common

import androidx.navigation.NavHostController
import com.example.lezhinbookmark.common.LZConstants
import com.example.lezhinbookmark.main.ui.ScreenRoute

/**
 * Navigate
 *
 * @param navController             NavHostController
 *
 * @author si.kwak
 */
class LZNavigate(navController: NavHostController) {
    val navigateToScreen: (String, String?) -> Unit = { _, screenId ->
        if(!screenId.isNullOrEmpty()) {
            if (screenId.equals(LZConstants.SEARCH_SCREEN_ID, ignoreCase = true)) {
                navController.navigate(ScreenRoute.SearchScreen.createRoute(screenId))
            } else {
                navController.navigate(ScreenRoute.BookMarkScreen.createRoute(screenId))
            }
        }
    }
}