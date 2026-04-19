package br.com.brunoccbertolini.cocktailhelperapp.db

import androidx.room.*
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import kotlinx.coroutines.flow.Flow

@Dao
interface CocktailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(drink: DrinkPreview)

    @Query("SELECT * FROM cocktails")
    fun getAllCocktail(): Flow<List<DrinkPreview>>

    @Delete
    suspend fun deleteCocktail(drink: DrinkPreview)
}
