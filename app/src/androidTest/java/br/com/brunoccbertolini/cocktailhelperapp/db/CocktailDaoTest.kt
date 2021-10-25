package br.com.brunoccbertolini.cocktailhelperapp.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import br.com.brunoccbertolini.cocktailhelperapp.getOrAwaitValue
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class CocktailDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: CocktailDatabase
    private lateinit var dao: CocktailDao


    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CocktailDatabase::class.java
        ).allowMainThreadQueries().build()

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