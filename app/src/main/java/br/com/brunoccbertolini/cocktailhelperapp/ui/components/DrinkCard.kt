package br.com.brunoccbertolini.cocktailhelperapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import coil3.compose.AsyncImage

@Composable
fun DrinkCard(drink: DrinkPreview, onClick: (DrinkPreview) -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier.clickable { onClick(drink) }) {
        Column {
            AsyncImage(
                model = drink.strDrinkThumb,
                contentDescription = drink.strDrink,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            Text(
                text = drink.strDrink,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
