package br.com.brunoccbertolini.cocktailhelperapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.brunoccbertolini.cocktailhelperapp.data.local.dao.CocktailDao
import br.com.brunoccbertolini.cocktailhelperapp.data.local.entity.CachedDrinkEntity
import br.com.brunoccbertolini.cocktailhelperapp.data.local.entity.DrinkPreviewEntity

@Database(
    entities = [DrinkPreviewEntity::class, CachedDrinkEntity::class],
    version = 2
)
abstract class CocktailDatabase : RoomDatabase() {

    abstract fun getCocktailDao(): CocktailDao

    companion object {
        const val DATABASE_NAME = "cocktail_db.db"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS `cached_drinks`
                    (`idDrink` TEXT NOT NULL, `strDrink` TEXT NOT NULL,
                     `strDrinkThumb` TEXT, `drinkType` TEXT NOT NULL,
                     PRIMARY KEY(`idDrink`))"""
                )
            }
        }

        @Volatile
        private var instance: CocktailDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CocktailDatabase::class.java,
                DATABASE_NAME
            ).addMigrations(MIGRATION_1_2).build()
    }
}
