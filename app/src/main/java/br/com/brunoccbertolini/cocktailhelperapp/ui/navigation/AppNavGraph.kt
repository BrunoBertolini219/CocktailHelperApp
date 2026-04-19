package br.com.brunoccbertolini.cocktailhelperapp.ui.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.brunoccbertolini.cocktailhelperapp.ui.screens.CocktailListScreen
import br.com.brunoccbertolini.cocktailhelperapp.ui.screens.DetailScreen
import br.com.brunoccbertolini.cocktailhelperapp.ui.screens.FavoritesScreen
import br.com.brunoccbertolini.cocktailhelperapp.ui.screens.RandomDrinkScreen
import br.com.brunoccbertolini.cocktailhelperapp.ui.screens.SearchScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

private object Routes {
    const val COCKTAIL_LIST = "cocktailList"
    const val SEARCH = "search"
    const val FAVORITES = "favorites"
    const val RANDOM = "random"
    const val DETAIL = "detail/{idDrink}/{strDrink}/{strDrinkThumb}"

    fun detail(idDrink: String, strDrink: String, strDrinkThumb: String): String {
        val encodedThumb = URLEncoder.encode(strDrinkThumb, StandardCharsets.UTF_8.name())
        val encodedName = URLEncoder.encode(strDrink, StandardCharsets.UTF_8.name())
        return "detail/$idDrink/$encodedName/$encodedThumb"
    }
}

private data class NavItem(val route: String, val label: String, val icon: ImageVector)

private val navItems = listOf(
    NavItem(Routes.COCKTAIL_LIST, "Cocktails", Icons.Filled.Home),
    NavItem(Routes.SEARCH, "Search", Icons.Filled.Search),
    NavItem(Routes.FAVORITES, "Favorites", Icons.Filled.Favorite),
    NavItem(Routes.RANDOM, "Random", Icons.Filled.Refresh)
)

@Composable
fun CocktailApp(windowSizeClass: WindowSizeClass) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val topLevelRoutes = navItems.map { it.route }
    val showNav = topLevelRoutes.any { currentRoute == it }

    fun navigate(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    if (isCompact) {
        Scaffold(
            bottomBar = {
                if (showNav) {
                    NavigationBar {
                        navItems.forEach { item ->
                            NavigationBarItem(
                                selected = currentRoute == item.route,
                                onClick = { navigate(item.route) },
                                icon = { Icon(item.icon, contentDescription = item.label) },
                                label = { Text(item.label) }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    } else {
        Row(modifier = Modifier.fillMaxSize()) {
            if (showNav) {
                NavigationRail {
                    navItems.forEach { item ->
                        NavigationRailItem(
                            selected = currentRoute == item.route,
                            onClick = { navigate(item.route) },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
            AppNavHost(navController = navController, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun AppNavHost(
    navController: androidx.navigation.NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.COCKTAIL_LIST,
        modifier = modifier
    ) {
        composable(Routes.COCKTAIL_LIST) {
            CocktailListScreen(onDrinkClick = { drink ->
                navController.navigate(
                    Routes.detail(drink.idDrink, drink.strDrink, drink.strDrinkThumb ?: "")
                )
            })
        }
        composable(Routes.SEARCH) {
            SearchScreen(onDrinkClick = { drink ->
                navController.navigate(
                    Routes.detail(drink.idDrink, drink.strDrink, drink.strDrinkThumb ?: "")
                )
            })
        }
        composable(Routes.FAVORITES) {
            FavoritesScreen(onDrinkClick = { drink ->
                navController.navigate(
                    Routes.detail(drink.idDrink, drink.strDrink, drink.strDrinkThumb ?: "")
                )
            })
        }
        composable(Routes.RANDOM) {
            RandomDrinkScreen()
        }
        composable(Routes.DETAIL) { backStackEntry ->
            val idDrink = backStackEntry.arguments?.getString("idDrink") ?: ""
            val strDrink = URLDecoder.decode(
                backStackEntry.arguments?.getString("strDrink") ?: "", StandardCharsets.UTF_8.name()
            )
            val strDrinkThumb = URLDecoder.decode(
                backStackEntry.arguments?.getString("strDrinkThumb") ?: "", StandardCharsets.UTF_8.name()
            )
            DetailScreen(
                idDrink = idDrink,
                strDrink = strDrink,
                strDrinkThumb = strDrinkThumb,
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
