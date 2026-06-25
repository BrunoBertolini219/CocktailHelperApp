package br.com.brunoccbertolini.cocktailhelperapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.brunoccbertolini.cocktailhelperapp.data.local.entity.CachedDrinkDetailEntity
import br.com.brunoccbertolini.cocktailhelperapp.data.local.entity.CachedDrinkEntity
import br.com.brunoccbertolini.cocktailhelperapp.data.local.entity.DrinkPreviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CocktailDao {

    // --- Favorites (user data) ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(drink: DrinkPreviewEntity)

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<DrinkPreviewEntity>>

    @Delete
    suspend fun deleteFavorite(drink: DrinkPreviewEntity)

    // --- Drink list cache (offline-first) ---

    @Query("SELECT * FROM cached_drinks WHERE drinkType = :type")
    fun getCachedDrinks(type: String): Flow<List<CachedDrinkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedDrinks(drinks: List<CachedDrinkEntity>)

    @Query("DELETE FROM cached_drinks WHERE drinkType = :type")
    suspend fun clearCachedDrinks(type: String)

    // --- Full drink detail cache (offline-first) ---

    @Query("SELECT * FROM cached_drink_details WHERE id = :id")
    suspend fun getCachedDetail(id: String): CachedDrinkDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDetail(detail: CachedDrinkDetailEntity)
}
