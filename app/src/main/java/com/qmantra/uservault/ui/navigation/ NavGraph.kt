package com.qmantra.uservault.ui.navigation
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.qmantra.uservault.viewmodel.UserViewModel
import com.qmantra.uservault.ui.screens.AddUserScreen
import com.qmantra.uservault.ui.screens.SearchScreen
import com.qmantra.uservault.ui.screens.EditUserScreen
import com.qmantra.uservault.data.local.User
import com.qmantra.uservault.ui.screens.AboutScreen
import com.qmantra.uservault.ui.screens.BackupScreen
import com.qmantra.uservault.ui.screens.ProfileScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun NavGraph(viewModel: UserViewModel) {

    val navController = rememberNavController()

    NavHost(navController, startDestination = "search") {

        composable("search") {
            SearchScreen(viewModel, navController)
        }

        composable("add") {
            AddUserScreen(viewModel, navController)
        }

        composable("edit/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toInt() ?: 0

            //just pass id, do not load here
            EditUserScreen(
                userId = id,
                viewModel = viewModel,
                onDone = { navController.popBackStack() }
            )
        }
        composable("backup") {
            BackupScreen(viewModel)

        }
        composable("profile") {
            ProfileScreen(viewModel, navController)
        }

        composable("about") {
            AboutScreen(navController)
        }

    }

}
