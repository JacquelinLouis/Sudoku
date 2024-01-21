package com.example.sudoku

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sudoku.feature.gridlist.GridListScreen

class Navigation {

    companion object {
        @Composable
        fun AppNavHost() {
            val navController = rememberNavController()
            NavHost(
                modifier = Modifier,
                navController = navController,
                startDestination = GRID_LIST
            ) {
                composable(GRID_LIST) {
                    GridListScreen { gridMetadataId -> navController.navigate("$GRID/$gridMetadataId") }
                }
                composable(
                    route = "$GRID/{$GRID_METADATA_ID_ARG}",
                    arguments = listOf(navArgument(GRID_METADATA_ID_ARG) { type = NavType.LongType })
                ) { backStackEntry ->
                    TODO("Not implemented yet")
                }
            }
        }

        private const val GRID_LIST = "grid-list"
        private const val GRID = "grid"
        private const val GRID_METADATA_ID_ARG = "gridMetadataId"
    }

}