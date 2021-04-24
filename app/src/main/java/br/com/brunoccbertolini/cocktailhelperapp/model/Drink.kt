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
    var strMeasure10: Any?,
    var strMeasure11: Any?,
    var strMeasure12: Any?,
    var strMeasure13: Any?,
    var strMeasure14: Any?,
    var strMeasure15: Any?,
    var strMeasure2: String?,
    var strMeasure3: String?,
    var strMeasure4: String?,
    var strMeasure5: Any?,
    var strMeasure6: String?,
    var strMeasure7: String?,
    var strMeasure8: Any?,
    var strMeasure9: Any?,
    var strTags: String?,
    var strVideo: Any?
) {

}