package br.com.brunoccbertolini.cocktailhelperapp.ui.pages.detail

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.model.Drink
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import br.com.brunoccbertolini.cocktailhelperapp.ui.components.ErrorContent
import br.com.brunoccbertolini.cocktailhelperapp.ui.components.LoadingContent
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomDrinkScreen(
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: DetailViewModel = hiltViewModel()
) {
    val drinkState by viewModel.drinkState.collectAsStateWithLifecycle()
    val saveState by viewModel.drinkPreviewState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val savedMsg = stringResource(R.string.saved_message)
    val errorSavingMsg = stringResource(R.string.error_saving)

    LaunchedEffect(Unit) {
        viewModel.getRandomDrink()
    }

    LaunchedEffect(saveState) {
        val event = saveState?.getContentIfNotHandled()
        if (event != null) {
            val msg = if (event is Resource.Success) savedMsg else event.message ?: errorSavingMsg
            snackbarHostState.showSnackbar(msg)
        }
    }

    val drink = (drinkState as? Resource.Success)?.data?.drinks?.firstOrNull()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.random_drink)) },
                actions = {
                    IconButton(onClick = { viewModel.getRandomDrink() }) {
                        Icon(Icons.Filled.Refresh, contentDescription = stringResource(R.string.refresh))
                    }
                }
            )
        },
        floatingActionButton = {
            if (drink != null) {
                FloatingActionButton(onClick = {
                    viewModel.saveCocktail(
                        DrinkPreview(
                            idDrink = drink.idDrink ?: "",
                            strDrink = drink.strDrink ?: "",
                            strDrinkThumb = drink.strDrinkThumb
                        )
                    )
                }) {
                    Icon(Icons.Filled.Favorite, contentDescription = stringResource(R.string.save_to_favorites))
                }
            }
        }
    ) { innerPadding ->
        when (drinkState) {
            is Resource.Loading -> LoadingContent(modifier = Modifier.padding(innerPadding))
            is Resource.Error -> ErrorContent(
                message = (drinkState as Resource.Error).message ?: stringResource(R.string.error_occurred),
                onRetry = { viewModel.getRandomDrink() },
                modifier = Modifier.padding(innerPadding)
            )
            is Resource.Success -> {
                if (drink != null) {
                    RandomDrinkDetail(drink = drink, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
private fun RandomDrinkDetail(drink: Drink, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = drink.strDrinkThumb,
            contentDescription = drink.strDrink,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = drink.strDrink ?: "",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            val ingredients = listOf(
                drink.strIngredient1, drink.strIngredient2, drink.strIngredient3,
                drink.strIngredient4, drink.strIngredient5, drink.strIngredient6,
                drink.strIngredient7, drink.strIngredient8, drink.strIngredient9,
                drink.strIngredient10, drink.strIngredient11, drink.strIngredient12
            ).filterNotNull().filter { it.isNotBlank() }

            val measures = listOf(
                drink.strMeasure1, drink.strMeasure2, drink.strMeasure3,
                drink.strMeasure4, drink.strMeasure5, drink.strMeasure6,
                drink.strMeasure7, drink.strMeasure8, drink.strMeasure9,
                drink.strMeasure10, drink.strMeasure11, drink.strMeasure12
            )

            if (ingredients.isNotEmpty()) {
                Text(stringResource(R.string.ingredients), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                ingredients.forEachIndexed { index, ingredient ->
                    val measure = measures.getOrNull(index)?.takeIf { it.isNotBlank() } ?: ""
                    Text("• $measure $ingredient".trim())
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            drink.strInstructions?.let { instructions ->
                Text(stringResource(R.string.instructions), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(instructions, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
