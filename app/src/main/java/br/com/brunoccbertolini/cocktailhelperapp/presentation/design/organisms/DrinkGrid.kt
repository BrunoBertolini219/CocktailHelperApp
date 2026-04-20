package br.com.brunoccbertolini.cocktailhelperapp.presentation.design.organisms

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.LoadingIndicator
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.DrinkCard
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.ErrorMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinkGrid(
    drinks: List<DrinkSummary>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    error: String?,
    onRefresh: () -> Unit,
    onDrinkClick: (DrinkSummary) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    when {
        isLoading && drinks.isEmpty() -> LoadingIndicator(modifier = modifier)
        error != null && drinks.isEmpty() -> ErrorMessage(
            message = error,
            onRetry = onRefresh,
            modifier = modifier
        )
        else -> PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = modifier.fillMaxSize()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(160.dp),
                contentPadding = contentPadding,
                modifier = Modifier.fillMaxSize()
            ) {
                items(drinks, key = { it.id }) { drink ->
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
