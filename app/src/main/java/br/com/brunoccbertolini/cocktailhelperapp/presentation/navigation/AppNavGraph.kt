package br.com.brunoccbertolini.cocktailhelperapp.presentation.navigation

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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.cocktaillist.CocktailListScreen
import br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.detail.DetailScreen
import br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.favorites.FavoritesScreen
import br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.random.RandomDrinkScreen
import br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.search.SearchScreen
import kotlin.reflect.KClass

private data class NavItem(
    val route: Any,
    val routeClass: KClass<*>,
    val labelResId: Int,
    val icon: ImageVector
)

private val navItems = listOf(
    NavItem(Route.CocktailList, Route.CocktailList::class, R.string.drinks, Icons.Filled.Home),
    NavItem(Route.Search, Route.Search::class, R.string.search, Icons.Filled.Search),
    NavItem(Route.Favorites, Route.Favorites::class, R.string.favorites, Icons.Filled.Favorite),
    NavItem(Route.Random, Route.Random::class, R.string.random_drink, Icons.Filled.Refresh)
)

@Suppress("UNCHECKED_CAST")
private fun NavDestination?.matchesRoute(klass: KClass<*>): Boolean =
    this?.hierarchy?.any { it.hasRoute(klass as KClass<Any>) } == true

@Composable
fun CocktailApp(windowSizeClass: WindowSizeClass) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val showNav = !currentDestination.matchesRoute(Route.Detail::class)

    fun navigate(route: Any) {
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
                                selected = currentDestination.matchesRoute(item.routeClass),
                                onClick = { navigate(item.route) },
                                icon = { Icon(item.icon, contentDescription = stringResource(item.labelResId)) },
                                label = { Text(stringResource(item.labelResId)) }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            AppNavHost(navController = navController, modifier = Modifier.padding(innerPadding))
        }
    } else {
        Row(modifier = Modifier.fillMaxSize()) {
            if (showNav) {
                NavigationRail {
                    navItems.forEach { item ->
                        NavigationRailItem(
                            selected = currentDestination.matchesRoute(item.routeClass),
                            onClick = { navigate(item.route) },
                            icon = { Icon(item.icon, contentDescription = stringResource(item.labelResId)) },
                            label = { Text(stringResource(item.labelResId)) }
                        )
                    }
                }
            }
            AppNavHost(navController = navController, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Route.CocktailList,
        modifier = modifier
    ) {
        composable<Route.CocktailList> {
            CocktailListScreen(
                onNavigateToDetail = { drink ->
                    navController.navigate(Route.Detail(drink.id, drink.name, drink.thumbnailUrl))
                }
            )
        }
        composable<Route.Search> {
            SearchScreen(
                onNavigateToDetail = { drink ->
                    navController.navigate(Route.Detail(drink.id, drink.name, drink.thumbnailUrl))
                }
            )
        }
        composable<Route.Favorites> {
            FavoritesScreen(
                onNavigateToDetail = { drink ->
                    navController.navigate(Route.Detail(drink.id, drink.name, drink.thumbnailUrl))
                }
            )
        }
        composable<Route.Random> {
            RandomDrinkScreen()
        }
        composable<Route.Detail> {
            DetailScreen(onNavigateUp = { navController.navigateUp() })
        }
    }
}
