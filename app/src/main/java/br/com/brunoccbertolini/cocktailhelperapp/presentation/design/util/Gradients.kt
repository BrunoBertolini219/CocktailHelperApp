package br.com.brunoccbertolini.cocktailhelperapp.presentation.design.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Draws a bottom-anchored vertical gradient scrim on top of content (e.g. an image),
 * so overlaid text stays legible. Covers the lower [heightFraction] of the bounds.
 */
fun Modifier.bottomScrim(
    color: Color = Color.Black,
    startAlpha: Float = 0f,
    endAlpha: Float = 0.75f,
    heightFraction: Float = 0.6f
): Modifier = this.drawWithCache {
    val brush = Brush.verticalGradient(
        colors = listOf(color.copy(alpha = startAlpha), color.copy(alpha = endAlpha)),
        startY = size.height * (1f - heightFraction),
        endY = size.height
    )
    onDrawBehind { drawRect(brush) }
}
