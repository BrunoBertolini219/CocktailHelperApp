package br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.util.ShimmerBox
import br.com.brunoccbertolini.cocktailhelperapp.ui.theme.Spacing

@Composable
fun DrinkCardSkeleton(modifier: Modifier = Modifier) {
    Column(modifier) {
        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            shape = MaterialTheme.shapes.large
        )
        Spacer(Modifier.height(Spacing.sm))
        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(14.dp),
            shape = MaterialTheme.shapes.small
        )
    }
}

@Composable
fun DrinkGridSkeleton(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(Spacing.md),
    count: Int = 8
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        userScrollEnabled = false
    ) {
        items(count) {
            DrinkCardSkeleton(Modifier.padding(Spacing.sm))
        }
    }
}

@Composable
fun DetailSkeleton(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize()) {
        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            shape = RectangleShape
        )
        Column(Modifier.padding(Spacing.lg)) {
            ShimmerBox(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(28.dp),
                shape = MaterialTheme.shapes.small
            )
            Spacer(Modifier.height(Spacing.md))
            repeat(6) {
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp),
                    shape = MaterialTheme.shapes.small
                )
                Spacer(Modifier.height(Spacing.sm))
            }
        }
    }
}
