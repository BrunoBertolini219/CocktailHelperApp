package br.com.brunoccbertolini.cocktailhelperapp.data.local

import androidx.room.TypeConverter
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.Ingredient
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Stores a drink's ingredient list as a JSON string column.
 *
 * Note: serialization is done with reified helpers inside the method bodies on purpose —
 * referencing `Ingredient.serializer()` from a class property breaks Room's KSP step,
 * because the serialization plugin generates `serializer()` after KSP analysis.
 */
class Converters {

    @TypeConverter
    fun fromIngredients(value: List<Ingredient>): String = Json.encodeToString(value)

    @TypeConverter
    fun toIngredients(value: String): List<Ingredient> = Json.decodeFromString(value)
}
