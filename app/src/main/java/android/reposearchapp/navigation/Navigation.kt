package android.reposearchapp.navigation

import android.reposearchapp.composables.RepoDetailsScreen
import android.reposearchapp.composables.RepoListScreen
import android.reposearchapp.composables.SearchScreen
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun Navigation() {
    val navController = rememberNavController()

    Surface {
        NavHost(
            navController = navController,
            startDestination = Routes.Search.route,
        ){
            composable(route = Routes.Search.route) {
                SearchScreen(navController = navController)
            }

            composable(
                route = Routes.RepoList.route + "/{queryString}",
                arguments = listOf(navArgument("queryString") {type = NavType.StringType})
            ) { backStackEntry ->
                RepoListScreen(
                    navController = navController,
                    queryString = backStackEntry.arguments?.getString("queryString") ?: ""
                )
            }

            composable(
                route = Routes.RepoDetails.route + "/{ownerName}/{repoName}",
                arguments = listOf(
                    navArgument("ownerName") {type = NavType.StringType},
                    navArgument("repoName") {type = NavType.StringType}
                )
            ) { backStackEntry ->
                RepoDetailsScreen(
                    ownerName = backStackEntry.arguments?.getString("ownerName") ?: "",
                    repoName = backStackEntry.arguments?.getString("repoName") ?: ""
                )
            }
        }
    }
}