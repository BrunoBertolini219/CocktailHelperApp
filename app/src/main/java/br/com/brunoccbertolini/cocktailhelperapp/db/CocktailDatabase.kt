package br.com.brunoccbertolini.cocktailhelperapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview

@Database(
    entities = [DrinkPreview::class, CachedDrinkEntity::class],
    version = 2
)
abstract class CocktailDatabase : RoomDatabase() {

    abstract fun getCocktailDao(): CocktailDao

    companion object {
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
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CocktailDatabase::class.java,
                "cocktail_db.db"
            ).addMigrations(MIGRATION_1_2).build()
    }
}
