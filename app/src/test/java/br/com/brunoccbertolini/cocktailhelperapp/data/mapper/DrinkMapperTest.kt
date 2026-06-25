package br.com.brunoccbertolini.cocktailhelperapp.data.mapper

import br.com.brunoccbertolini.cocktailhelperapp.data.local.entity.CachedDrinkEntity
import br.com.brunoccbertolini.cocktailhelperapp.data.local.entity.DrinkPreviewEntity
import br.com.brunoccbertolini.cocktailhelperapp.data.remote.dto.DrinkDetailDto
import br.com.brunoccbertolini.cocktailhelperapp.data.remote.dto.DrinkSummaryDto
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DrinkMapperTest {

    @Test
    fun `DrinkSummaryDto maps to domain`() {
        val domain = DrinkSummaryDto(idDrink = "1", strDrink = "Mojito", strDrinkThumb = "url").toDomain()

        assertThat(domain).isEqualTo(DrinkSummary(id = "1", name = "Mojito", thumbnailUrl = "url"))
    }

    @Test
    fun `DrinkDetailDto flattens only non-blank ingredients and pairs measures`() {
        val dto = DrinkDetailDto(
            idDrink = "1",
            strDrink = "Mojito",
            strIngredient1 = "White Rum",
            strIngredient2 = "Mint",
            strIngredient3 = "   ", // blank -> skipped
            strMeasure1 = "2 oz",
            strMeasure2 = null
        )

        val detail = dto.toDomain()

        assertThat(detail.ingredients).hasSize(2)
        assertThat(detail.ingredients[0].name).isEqualTo("White Rum")
        assertThat(detail.ingredients[0].measure).isEqualTo("2 oz")
        assertThat(detail.ingredients[1].name).isEqualTo("Mint")
        assertThat(detail.ingredients[1].measure).isNull()
    }

    @Test
    fun `DrinkDetailDto falls back to empty id and name when null`() {
        val detail = DrinkDetailDto().toDomain()

        assertThat(detail.id).isEmpty()
        assertThat(detail.name).isEmpty()
        assertThat(detail.ingredients).isEmpty()
    }

    @Test
    fun `DrinkSummary round-trips through the favorite entity`() {
        val summary = DrinkSummary("1", "Mojito", "url")

        val entity = summary.toEntity()

        assertThat(entity).isEqualTo(DrinkPreviewEntity("1", "Mojito", "url"))
        assertThat(entity.toDomain()).isEqualTo(summary)
    }

    @Test
    fun `DrinkSummaryDto maps to a typed cache entity`() {
        val entity = DrinkSummaryDto("1", "Mojito", "url").toCacheEntity("alcoholic")

        assertThat(entity).isEqualTo(CachedDrinkEntity("1", "Mojito", "url", "alcoholic"))
        assertThat(entity.toDomain()).isEqualTo(DrinkSummary("1", "Mojito", "url"))
    }
}
