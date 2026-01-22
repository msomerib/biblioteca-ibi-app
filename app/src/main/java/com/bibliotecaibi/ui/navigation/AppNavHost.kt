package com.bibliotecaibi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bibliotecaibi.ui.auth.LoginScreen
import com.bibliotecaibi.ui.branches.BranchSelectScreen
import com.bibliotecaibi.ui.home.HomeScreen
import com.bibliotecaibi.ui.books.BooksScreen
import com.bibliotecaibi.ui.members.MembersScreen
import com.bibliotecaibi.ui.loans.LoansScreen
import com.bibliotecaibi.ui.reports.ReportsScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) { LoginScreen(navController) }

        composable(Routes.BRANCH) { BranchSelectScreen(navController) }

        composable(Routes.HOME) { HomeScreen(navController) }

        composable(Routes.BOOKS) { BooksScreen() }

        composable(Routes.MEMBERS) { MembersScreen() }

        composable(Routes.LOANS) { LoansScreen() }

        composable(Routes.REPORTS) { ReportsScreen() }
    }
}
