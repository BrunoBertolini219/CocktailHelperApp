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

    @Query("SELECT * FROM cached_drinks WHERE drinkType = :type")
    fun getCachedDrinks(type: String): Flow<List<CachedDrinkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedDrinks(drinks: List<CachedDrinkEntity>)

    @Query("DELETE FROM cached_drinks WHERE drinkType = :type")
    suspend fun clearCachedDrinks(type: String)
}
