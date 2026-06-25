package br.com.brunoccbertolini.cocktailhelperapp.presentation.util

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.MeasureSystem
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MeasureConverterTest {

    @Test
    fun `original system returns the input unchanged`() {
        assertThat(convertMeasure("1 1/2 oz", MeasureSystem.Original)).isEqualTo("1 1/2 oz")
    }

    @Test
    fun `whole oz converts to ml rounded to nearest 5`() {
        assertThat(convertMeasure("2 oz", MeasureSystem.Metric)).isEqualTo("60 ml")
    }

    @Test
    fun `mixed-number oz converts`() {
        assertThat(convertMeasure("1 1/2 oz", MeasureSystem.Metric)).isEqualTo("45 ml")
    }

    @Test
    fun `simple fraction oz converts`() {
        assertThat(convertMeasure("1/2 oz", MeasureSystem.Metric)).isEqualTo("15 ml")
    }

    @Test
    fun `cl converts to ml`() {
        assertThat(convertMeasure("4 cl", MeasureSystem.Metric)).isEqualTo("40 ml")
    }

    @Test
    fun `tsp and tbsp convert to ml`() {
        assertThat(convertMeasure("1 tsp", MeasureSystem.Metric)).isEqualTo("5 ml")
        assertThat(convertMeasure("1 tbsp", MeasureSystem.Metric)).isEqualTo("15 ml")
    }

    @Test
    fun `non-convertible measure is left unchanged`() {
        assertThat(convertMeasure("Juice of 1 lime", MeasureSystem.Metric)).isEqualTo("Juice of 1 lime")
    }

    @Test
    fun `null stays null`() {
        assertThat(convertMeasure(null, MeasureSystem.Metric)).isNull()
    }
}
