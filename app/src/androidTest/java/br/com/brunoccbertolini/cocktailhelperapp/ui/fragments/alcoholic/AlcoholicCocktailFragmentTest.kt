package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.alcoholic

import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.adapter.CocktailListAdapter
import org.junit.Assert.*
import org.junit.Test

class AlcoholicCocktailFragmentTest {

    @Test
    fun quandoAbrirDeveMostrarSaudacaoParaOUsuario() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_drink_title))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tv_drink_title))
            .check(ViewAssertions.matches(ViewMatchers.withText("110 in the shade")))
    }

    @Test
    fun quandoListarAsCategoriasDeveSerPossivelArrastarParaVerOFimdaLista(){
        Espresso.onView(ViewMatchers.withId(R.id.rv_cocktail_list))
            .perform(RecyclerViewActions.scrollToPosition<CocktailListAdapter.ViewHolder>(8))
    }

    @Test
    fun quandoAbrirOAppNaoDeveExibirCocktail747E5050(){
        "50/50 e 747"
        Espresso.onView(ViewMatchers.withId(R.id.rv_cocktail_list))
            .perform(RecyclerViewActions.scrollToPosition<CocktailListAdapter.ViewHolder>(2))
    }

}