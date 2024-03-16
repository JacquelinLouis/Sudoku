package com.example.sudoku

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sudoku.feature.grid.GridComposable
import com.example.sudoku.feature.gridlist.GridListComposable

class Navigation {

    companion object {

        private fun NavHostController.navigateOnce(route: String,
                                                   navOptions: NavOptions? = null,
                                                   navigatorExtras: Navigator.Extras? = null
        ) {
            if (currentDestination?.route != route) {
                navigate(route, navOptions, navigatorExtras)
            }
        }

        @Composable
        fun AppNavHost() {
            val navController = rememberNavController()
            NavHost(
                modifier = Modifier,
                navController = navController,
                startDestination = GRID_LIST
            ) {
                composable(GRID_LIST) {
                    GridListComposable { gridMetadataId ->
                        navController.navigateOnce("$GRID/$gridMetadataId")
                    }
                }
                composable(
                    route = "$GRID/{$GRID_METADATA_ID_ARG}",
                    arguments = listOf(navArgument(GRID_METADATA_ID_ARG) { type = NavType.LongType })
                ) { backStackEntry ->
                    GridComposable(
                        gridMetadataId = backStackEntry.arguments!!.getLong(GRID_METADATA_ID_ARG)
                    )
                }
            }
        }

        private const val GRID_LIST = "grid-list"
        private const val GRID = "grid"
        private const val GRID_METADATA_ID_ARG = "gridMetadataId"
    }

}