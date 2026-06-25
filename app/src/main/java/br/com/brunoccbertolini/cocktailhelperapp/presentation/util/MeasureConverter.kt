package br.com.brunoccbertolini.cocktailhelperapp.presentation.util

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.MeasureSystem
import kotlin.math.roundToInt

private const val ML_PER_OZ = 29.5735
private const val ML_PER_CL = 10.0
private const val ML_PER_TBSP = 15.0
private const val ML_PER_TSP = 5.0

/**
 * Converts free-form cocktail measures (e.g. "1 1/2 oz", "4 cl", "1 tsp") to metric
 * millilitres when [system] is [MeasureSystem.Metric]. Quantities may be whole numbers,
 * decimals, simple fractions, or mixed numbers. Anything unrecognized is left unchanged.
 */
fun convertMeasure(raw: String?, system: MeasureSystem): String? {
    if (raw.isNullOrBlank() || system == MeasureSystem.Original) return raw
    var out = raw
    out = replaceUnit(out, "oz", ML_PER_OZ)
    out = replaceUnit(out, "cl", ML_PER_CL)
    out = replaceUnit(out, "tbsp", ML_PER_TBSP)
    out = replaceUnit(out, "tsp", ML_PER_TSP)
    return out
}

private fun replaceUnit(text: String, unit: String, mlPerUnit: Double): String {
    val regex = Regex("""(\d+\s+\d+/\d+|\d+/\d+|\d+\.\d+|\d+)\s*$unit\b""", RegexOption.IGNORE_CASE)
    return regex.replace(text) { match ->
        val qty = parseQuantity(match.groupValues[1])
        if (qty == null) match.value else "${formatMl(qty * mlPerUnit)} ml"
    }
}

private fun parseQuantity(value: String): Double? {
    val trimmed = value.trim()
    return when {
        trimmed.matches(Regex("""\d+\s+\d+/\d+""")) -> {
            val parts = trimmed.split(Regex("\\s+"), limit = 2)
            parts[0].toDouble() + parseFraction(parts[1])
        }
        trimmed.contains('/') -> parseFraction(trimmed)
        else -> trimmed.toDoubleOrNull()
    }
}

private fun parseFraction(fraction: String): Double {
    val parts = fraction.split('/')
    val numerator = parts.getOrNull(0)?.toDoubleOrNull() ?: return 0.0
    val denominator = parts.getOrNull(1)?.toDoubleOrNull() ?: return 0.0
    return if (denominator == 0.0) 0.0 else numerator / denominator
}

/** Rounds to the nearest 5 ml for bar-friendly numbers. */
private fun formatMl(ml: Double): String = ((ml / 5.0).roundToInt() * 5).toString()
