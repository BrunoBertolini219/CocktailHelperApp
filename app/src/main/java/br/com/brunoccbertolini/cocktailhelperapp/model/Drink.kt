package br.com.brunoccbertolini.cocktailhelperapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "drinkdetail")
data class Drink(
    @PrimaryKey(autoGenerate = false)
    var idDrink: String?,

    var dateModified: String?,
    var strAlcoholic: String?,
    var strCategory: String?,
    var strCreativeCommonsConfirmed: String?,
    var strDrink: String?,
    var strDrinkAlternate: Any?,
    var strDrinkThumb: String?,
    var strIBA: String?,
    var strImageAttribution: String?,
    var strImageSource: String?,
    var strIngredient1: String?,
    var strIngredient10: String?,
    var strIngredient11: String?,
    var strIngredient12: String?,
    var strIngredient13: Any?,
    var strIngredient14: Any?,
    var strIngredient15: Any?,
    var strIngredient2: String?,
    var strIngredient3: String?,
    var strIngredient4: String?,
    var strIngredient5: String?,
    var strIngredient6: String?,
    var strIngredient7: String?,
    var strIngredient8: String?,
    var strIngredient9: String?,
    var strInstructions: String?,
    var strInstructionsDE: String?,
    var strInstructionsES: Any?,
    var strInstructionsFR: Any?,
    var strInstructionsIT: String?,
    var strMeasure1: String?,
    @SerializedName("strInstructionsZH-HANS")
    val strInstructionsZH: Any?,
    @SerializedName("strInstructionsZH-HANT")
    val strInstructionsHANT: Any?,
    var strMeasure10: String?,
    var strMeasure11: String?,
    var strMeasure12: String?,
    var strMeasure13: String?,
    var strMeasure14: String?,
    var strMeasure15: String?,
    var strMeasure2: String?,
    var strMeasure3: String?,
    var strMeasure4: String?,
    var strMeasure5: String?,
    var strMeasure6: String?,
    var strMeasure7: String?,
    var strMeasure8: String?,
    var strMeasure9: String?,
    var strTags: String?,
    var strVideo: Any?
)