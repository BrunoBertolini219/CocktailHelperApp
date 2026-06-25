package br.com.brunoccbertolini.cocktailhelperapp.presentation.util

import android.content.Context
import android.content.Intent
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkDetail
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.MeasureSystem

/**
 * Formats [drink] as a plain-text recipe (measures honoring [system]) and launches the
 * Android share sheet, so it can be sent via WhatsApp, messages, email, etc.
 */
fun shareDrink(context: Context, drink: DrinkDetail, system: MeasureSystem = MeasureSystem.Original) {
    val text = buildString {
        appendLine("🍸 ${drink.name}")
        drink.category?.takeIf { it.isNotBlank() }?.let { appendLine(it) }
        appendLine()
        if (drink.ingredients.isNotEmpty()) {
            appendLine("Ingredients:")
            drink.ingredients.forEach { ingredient ->
                val measure = convertMeasure(ingredient.measure, system)?.takeIf { it.isNotBlank() }
                appendLine(if (measure != null) "• $measure ${ingredient.name}" else "• ${ingredient.name}")
            }
            appendLine()
        }
        drink.instructions?.takeIf { it.isNotBlank() }?.let {
            appendLine("Instructions:")
            appendLine(it)
            appendLine()
        }
        append("Shared from Cocktail Helper")
    }

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, drink.name)
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(sendIntent, null))
}
