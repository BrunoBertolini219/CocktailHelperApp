package br.com.brunoccbertolini.cocktailhelperapp.data.local.dao

import androidx.room.*
import br.com.brunoccbertolini.cocktailhelperapp.data.local.entity.CachedDrinkEntity
import br.com.brunoccbertolini.cocktailhelperapp.data.local.entity.DrinkPreviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CocktailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(drink: DrinkPreviewEntity)

    @Query("SELECT * FROM cocktails")
    fun getAllCocktails(): Flow<List<DrinkPreviewEntity>>

    @Delete
    suspend fun deleteCocktail(drink: DrinkPreviewEntity)

    @Query("SELECT * FROM cached_drinks WHERE drinkType = :type")
    fun getCachedDrinks(type: String): Flow<List<CachedDrinkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedDrinks(drinks: List<CachedDrinkEntity>)

    @Query("DELETE FROM cached_drinks WHERE drinkType = :type")
    suspend fun clearCachedDrinks(type: String)
}
