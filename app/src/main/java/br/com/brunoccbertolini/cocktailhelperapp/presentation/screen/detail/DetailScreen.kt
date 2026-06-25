package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.detail

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkDetail
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailImage
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailText
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailTextStyle
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.DetailSkeleton
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.DrinkInfoChips
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.EmptyState
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.IngredientsSection
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.SectionHeader
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.util.bottomScrim
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.MeasureSystem
import br.com.brunoccbertolini.cocktailhelperapp.presentation.util.ObserveAsEvents
import br.com.brunoccbertolini.cocktailhelperapp.presentation.util.shareDrink
import br.com.brunoccbertolini.cocktailhelperapp.ui.theme.Spacing

private val HeroHeight = 300.dp

@Composable
fun DetailScreen(
    onNavigateUp: () -> Unit,
    contentPadding: androidx.compose.foundation.layout.PaddingValues = androidx.compose.foundation.layout.PaddingValues(),
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val checked = remember(state.drink?.id) { mutableStateMapOf<Int, Boolean>() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is DetailEvent.NavigateUp -> onNavigateUp()
            is DetailEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message.asString(context))
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val drink = state.drink
        when {
            drink != null -> DetailContent(
                drink = drink,
                isFavorite = state.isFavorite,
                measureSystem = state.measureSystem,
                onMeasureSystemChange = { viewModel.onAction(DetailAction.SetMeasureSystem(it)) },
                isIngredientChecked = { checked[it] == true },
                onToggleIngredient = { checked[it] = checked[it] != true },
                onBack = { viewModel.onAction(DetailAction.NavigateUp) },
                onShare = { shareDrink(context, drink, state.measureSystem) },
                onToggleFavorite = { viewModel.onAction(DetailAction.ToggleFavorite) },
                contentPadding = contentPadding
            )

            state.error != null -> EmptyState(
                icon = Icons.Filled.Warning,
                title = state.error!!.asString(),
                actionLabel = stringResource(R.string.retry),
                onAction = { viewModel.onAction(DetailAction.Retry) }
            )

            else -> DetailSkeleton(Modifier.fillMaxSize())
        }

        // Back button for the loading / error states (content has its own in the hero bar).
        if (drink == null) {
            HeroCircleButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back),
                onClick = { viewModel.onAction(DetailAction.NavigateUp) },
                scrimAlpha = 0f,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = Spacing.sm, top = contentPadding.calculateTopPadding() + Spacing.sm)
            )
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
private fun DetailContent(
    drink: DrinkDetail,
    isFavorite: Boolean,
    measureSystem: MeasureSystem,
    onMeasureSystemChange: (MeasureSystem) -> Unit,
    isIngredientChecked: (Int) -> Boolean,
    onToggleIngredient: (Int) -> Unit,
    onBack: () -> Unit,
    onShare: () -> Unit,
    onToggleFavorite: () -> Unit,
    contentPadding: androidx.compose.foundation.layout.PaddingValues
) {
    val scrollState = rememberScrollState()
    val heroHeightPx = with(LocalDensity.current) { HeroHeight.toPx() }.coerceAtLeast(1f)
    val collapse by remember {
        derivedStateOf { (scrollState.value / heroHeightPx).coerceIn(0f, 1f) }
    }
    val topInset = contentPadding.calculateTopPadding()
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Parallax hero
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(HeroHeight)
                    .clipToBounds()
                    .background(Color.Black)
            ) {
                CocktailImage(
                    url = drink.thumbnailUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { translationY = scrollState.value * 0.4f }
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .bottomScrim(endAlpha = 0.85f, heightFraction = 0.7f)
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(Spacing.lg)
                        .graphicsLayer { alpha = 1f - collapse }
                ) {
                    CocktailText(
                        text = drink.name,
                        style = CocktailTextStyle.Headline,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    drink.category?.takeIf { it.isNotBlank() }?.let {
                        Spacer(Modifier.height(Spacing.xxs))
                        CocktailText(text = it, style = CocktailTextStyle.Body, color = Color.White.copy(alpha = 0.85f))
                    }
                }
            }

            // Body sheet
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-20).dp),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = surfaceColor
            ) {
                Column(modifier = Modifier.padding(Spacing.lg)) {
                    DrinkInfoChips(
                        alcoholic = drink.alcoholic,
                        category = drink.category,
                        glass = drink.glass
                    )
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
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CocktailText(
                                text = instructions,
                                style = CocktailTextStyle.Body,
                                modifier = Modifier.padding(Spacing.md)
                            )
                        }
                    }
                    Spacer(Modifier.height(contentPadding.calculateBottomPadding() + Spacing.xl))
                }
            }
        }

        // Collapsing top bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind { drawRect(surfaceColor.copy(alpha = collapse)) }
        ) {
            Spacer(Modifier.height(topInset))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = Spacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val iconTint = lerp(Color.White, onSurfaceColor, collapse)
                val scrimAlpha = 0.35f * (1f - collapse)
                HeroCircleButton(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    onClick = onBack,
                    scrimAlpha = scrimAlpha,
                    tint = iconTint
                )
                Text(
                    text = drink.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = onSurfaceColor.copy(alpha = collapse),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = Spacing.sm)
                )
                HeroCircleButton(
                    icon = Icons.Filled.Share,
                    contentDescription = stringResource(R.string.share_recipe),
                    onClick = onShare,
                    scrimAlpha = scrimAlpha,
                    tint = iconTint
                )
                Spacer(Modifier.width(Spacing.xs))
                HeroFavoriteButton(
                    isFavorite = isFavorite,
                    onToggle = onToggleFavorite,
                    scrimAlpha = scrimAlpha,
                    unfavoriteTint = iconTint
                )
            }
        }
    }
}

@Composable
private fun HeroCircleButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    scrimAlpha: Float,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = scrimAlpha))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun HeroFavoriteButton(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    scrimAlpha: Float,
    unfavoriteTint: Color,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(1f) }
    var previous by remember { mutableStateOf(isFavorite) }
    LaunchedEffect(isFavorite) {
        if (isFavorite != previous) {
            previous = isFavorite
            scale.animateTo(1.3f, tween(durationMillis = 120, easing = FastOutLinearInEasing))
            scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
        }
    }
    Box(
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = scrimAlpha))
            .clickable(onClick = onToggle),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = stringResource(
                if (isFavorite) R.string.remove_from_favorites else R.string.save_to_favorites
            ),
            tint = if (isFavorite) MaterialTheme.colorScheme.primary else unfavoriteTint,
            modifier = Modifier
                .size(24.dp)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                }
        )
    }
}
