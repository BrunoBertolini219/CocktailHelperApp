package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.random

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkDetail
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailImage
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailText
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailTextStyle
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.FavoriteButton
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.DetailSkeleton
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.DrinkInfoChips
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.EmptyState
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.IngredientsSection
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.SectionHeader
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.MeasureSystem
import br.com.brunoccbertolini.cocktailhelperapp.presentation.util.ObserveAsEvents
import br.com.brunoccbertolini.cocktailhelperapp.presentation.util.shareDrink
import br.com.brunoccbertolini.cocktailhelperapp.ui.theme.Spacing

@Composable
fun RandomDrinkScreen(
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: RandomDrinkViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val checked = remember(state.drink?.id) { mutableStateMapOf<Int, Boolean>() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is RandomDrinkEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message.asString(context))
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = Spacing.lg,
                        end = Spacing.sm,
                        top = contentPadding.calculateTopPadding() + Spacing.sm,
                        bottom = Spacing.sm
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CocktailText(
                    text = stringResource(R.string.random_drink),
                    style = CocktailTextStyle.Headline,
                    modifier = Modifier.weight(1f)
                )
                val drink = state.drink
                if (drink != null) {
                    IconButton(onClick = { shareDrink(context, drink, state.measureSystem) }) {
                        Icon(Icons.Filled.Share, contentDescription = stringResource(R.string.share_recipe))
                    }
                    FavoriteButton(
                        isFavorite = state.isFavorite,
                        onToggle = { viewModel.onAction(RandomDrinkAction.ToggleFavorite) }
                    )
                }
                IconButton(onClick = { viewModel.onAction(RandomDrinkAction.Refresh) }) {
                    Icon(Icons.Filled.Refresh, contentDescription = stringResource(R.string.shuffle))
                }
            }

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when {
                    state.isLoading -> DetailSkeleton(Modifier.fillMaxSize())

                    state.error != null -> EmptyState(
                        icon = Icons.Filled.Warning,
                        title = state.error!!.asString(),
                        actionLabel = stringResource(R.string.retry),
                        onAction = { viewModel.onAction(RandomDrinkAction.Refresh) }
                    )

                    state.drink != null -> RandomDrinkContent(
                        drink = state.drink!!,
                        measureSystem = state.measureSystem,
                        onMeasureSystemChange = { viewModel.onAction(RandomDrinkAction.SetMeasureSystem(it)) },
                        isIngredientChecked = { checked[it] == true },
                        onToggleIngredient = { checked[it] = checked[it] != true },
                        bottomInset = contentPadding.calculateBottomPadding()
                    )
                }
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = contentPadding.calculateBottomPadding())
        )
    }
}

@Composable
private fun RandomDrinkContent(
    drink: DrinkDetail,
    measureSystem: MeasureSystem,
    onMeasureSystemChange: (MeasureSystem) -> Unit,
    isIngredientChecked: (Int) -> Boolean,
    onToggleIngredient: (Int) -> Unit,
    bottomInset: androidx.compose.ui.unit.Dp
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Spacing.lg)
    ) {
        CocktailImage(
            url = drink.thumbnailUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(MaterialTheme.shapes.large)
        )
        Spacer(Modifier.height(Spacing.md))
        CocktailText(text = drink.name, style = CocktailTextStyle.Title)
        Spacer(Modifier.height(Spacing.sm))
        DrinkInfoChips(alcoholic = drink.alcoholic, category = drink.category, glass = drink.glass)
        if (drink.ingredients.isNotEmpty()) {
            Spacer(Modifier.height(Spacing.lg))
            IngredientsSection(
                ingredients = drink.ingredients,
                measureSystem = measureSystem,
                onMeasureSystemChange = onMeasureSystemChange,
                isChecked = isIngredientChecked,
                onToggleChecked = onToggleIngredient
            )
        }
        drink.instructions?.takeIf { it.isNotBlank() }?.let { instructions ->
            Spacer(Modifier.height(Spacing.xl))
            SectionHeader(title = stringResource(R.string.instructions))
            Spacer(Modifier.height(Spacing.sm))
            CocktailText(text = instructions, style = CocktailTextStyle.Body)
        }
        Spacer(Modifier.height(bottomInset + Spacing.xl))
    }
}
