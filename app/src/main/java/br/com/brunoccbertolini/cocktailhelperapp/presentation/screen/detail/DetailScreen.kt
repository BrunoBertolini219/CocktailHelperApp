package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkDetail
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailImage
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailText
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailTextStyle
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.LoadingIndicator
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.ErrorMessage
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.IngredientRow
import br.com.brunoccbertolini.cocktailhelperapp.presentation.util.ObserveAsEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    onNavigateUp: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is DetailEvent.NavigateUp -> onNavigateUp()
            is DetailEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(state.drinkName) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onAction(DetailAction.NavigateUp) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (state.drink != null) {
                FloatingActionButton(onClick = { viewModel.onAction(DetailAction.SaveFavorite) }) {
                    Icon(Icons.Filled.Favorite, contentDescription = stringResource(R.string.save_to_favorites))
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when {
            state.isLoading -> LoadingIndicator(modifier = modifier)
            state.error != null -> ErrorMessage(
                message = state.error!!,
                onRetry = { viewModel.onAction(DetailAction.Retry) },
                modifier = modifier
            )
            state.drink != null -> DrinkDetailContent(
                drink = state.drink!!,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun DrinkDetailContent(drink: DrinkDetail, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        CocktailImage(
            url = drink.thumbnailUrl,
            contentDescription = drink.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        )
        Column(modifier = Modifier.padding(16.dp)) {
            CocktailText(text = drink.name, style = CocktailTextStyle.Title)
            Spacer(modifier = Modifier.height(8.dp))
            if (drink.ingredients.isNotEmpty()) {
                CocktailText(
                    text = stringResource(R.string.ingredients),
                    style = CocktailTextStyle.Title
                )
                Spacer(modifier = Modifier.height(4.dp))
                drink.ingredients.forEach { ingredient ->
                    IngredientRow(ingredient = ingredient)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            drink.instructions?.let { instructions ->
                CocktailText(
                    text = stringResource(R.string.instructions),
                    style = CocktailTextStyle.Title
                )
                Spacer(modifier = Modifier.height(4.dp))
                CocktailText(text = instructions, style = CocktailTextStyle.Body)
            }
        }
    }
}
