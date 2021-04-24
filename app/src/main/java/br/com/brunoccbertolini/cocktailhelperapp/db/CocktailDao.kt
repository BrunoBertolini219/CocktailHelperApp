package br.com.brunoccbertolini.cocktailhelperapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview

@Dao
interface CocktailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(drink: DrinkPreview)

    @Query("SELECT * FROM cocktails")
    fun getAllCocktail(): LiveData<List<DrinkPreview>>

    @Delete
    suspend fun deleteCocktail(drink: DrinkPreview)
}