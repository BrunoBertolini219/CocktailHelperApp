package br.com.brunoccbertolini.cocktailhelperapp.ui.pages.alcoholic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import br.com.brunoccbertolini.cocktailhelperapp.ui.components.DrinkCard
import br.com.brunoccbertolini.cocktailhelperapp.ui.components.ErrorContent
import br.com.brunoccbertolini.cocktailhelperapp.ui.components.LoadingContent
import br.com.brunoccbertolini.cocktailhelperapp.ui.pages.noalcoholic.NonAlcoholicCocktailViewModel
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailListScreen(
    onDrinkClick: (DrinkPreview) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    val alcoholicVm: AlcoholicCocktailViewModel = hiltViewModel()
    val nonAlcoholicVm: NonAlcoholicCocktailViewModel = hiltViewModel()

    val alcoholicState by alcoholicVm.cocktailAlcoholic.collectAsStateWithLifecycle()
    val nonAlcoholicState by nonAlcoholicVm.cocktailNoAlcoholic.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(stringResource(R.string.alcoholic), stringResource(R.string.non_alcoholic))

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        val currentState = if (selectedTab == 0) alcoholicState else nonAlcoholicState
        val onRefresh: () -> Unit = {
            if (selectedTab == 0) alcoholicVm.refresh() else nonAlcoholicVm.refresh()
        }

        when (currentState) {
            is Resource.Loading -> LoadingContent()
            is Resource.Error -> ErrorContent(
                message = currentState.message ?: stringResource(R.string.error_occurred),
                onRetry = onRefresh
            )
            is Resource.Success -> {
                val drinks = currentState.data?.drinks ?: emptyList()
                PullToRefreshBox(
                    isRefreshing = false,
                    onRefresh = onRefresh,
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(160.dp),
                        contentPadding = contentPadding,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(drinks, key = { it.idDrink }) { drink ->
                            DrinkCard(
                                drink = drink,
                                onClick = onDrinkClick,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
