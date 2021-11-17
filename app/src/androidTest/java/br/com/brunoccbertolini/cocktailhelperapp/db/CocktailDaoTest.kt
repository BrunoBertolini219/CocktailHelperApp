package br.com.brunoccbertolini.cocktailhelperapp.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import br.com.brunoccbertolini.cocktailhelperapp.getOrAwaitValue
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class CocktailDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: CocktailDatabase
    private lateinit var dao: CocktailDao


    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.getCocktailDao()
    }


    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertCocktail() = runBlockingTest {
        val drink = DrinkPreview("1", "Pitu", "url")
        dao.upsert(drink)

        val allDrinks = dao.getAllCocktail().getOrAwaitValue()

        assertTrue(allDrinks.contains(drink))
    }

    @Test
    fun deleteCocktail() = runBlockingTest {
        val drink = DrinkPreview("1", "Pitu", "url")
        dao.upsert(drink)
        dao.deleteCocktail(drink)

        val allDrinks = dao.getAllCocktail().getOrAwaitValue()

        assertFalse(allDrinks.contains(drink))
    }
}