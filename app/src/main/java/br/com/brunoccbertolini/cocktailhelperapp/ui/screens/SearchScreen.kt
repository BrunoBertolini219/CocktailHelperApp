package br.com.brunoccbertolini.cocktailhelperapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import br.com.brunoccbertolini.cocktailhelperapp.ui.components.DrinkCard
import br.com.brunoccbertolini.cocktailhelperapp.ui.components.ErrorContent
import br.com.brunoccbertolini.cocktailhelperapp.ui.components.LoadingContent
import br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.search.SearchCocktailViewModel
import br.com.brunoccbertolini.cocktailhelperapp.util.Constrants.Companion.searchName
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import kotlinx.coroutines.delay

@Composable
fun SearchScreen(
    onDrinkClick: (DrinkPreview) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: SearchCocktailViewModel = hiltViewModel()
) {
    val searchState by viewModel.searchCocktail.collectAsStateWithLifecycle()
    var query by remember { mutableStateOf("") }
    var searchType by remember { mutableStateOf(searchName) }

    LaunchedEffect(query, searchType) {
        if (query.isNotBlank()) {
            delay(500)
            viewModel.searchCocktail(query, searchType)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp)) {
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search cocktails...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            FilterChip(
                selected = searchType == searchName,
                onClick = { searchType = searchName },
                label = { Text("By Name") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = searchType != searchName,
                onClick = { searchType = "ingredient" },
                label = { Text("By Ingredient") }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        when {
            query.isBlank() -> {
                Text(
                    text = "Type a name or ingredient to search",
                    modifier = Modifier.padding(top = 32.dp).align(Alignment.CenterHorizontally)
                )
            }
            searchState == null -> {}
            searchState is Resource.Loading -> LoadingContent()
            searchState is Resource.Error -> ErrorContent(
                message = (searchState as Resource.Error).message ?: "Search failed",
                onRetry = { viewModel.searchCocktail(query, searchType) }
            )
            searchState is Resource.Success -> {
                val drinks = (searchState as Resource.Success).data?.drinks ?: emptyList()
                if (drinks.isEmpty()) {
                    Text(
                        text = "No results found",
                        modifier = Modifier.padding(top = 32.dp).align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(160.dp),
                        contentPadding = contentPadding
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
