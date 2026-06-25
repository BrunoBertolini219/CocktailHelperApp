package br.com.brunoccbertolini.cocktailhelperapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.brunoccbertolini.cocktailhelperapp.data.local.Converters
import br.com.brunoccbertolini.cocktailhelperapp.data.local.dao.CocktailDao
import br.com.brunoccbertolini.cocktailhelperapp.data.local.entity.CachedDrinkDetailEntity
import br.com.brunoccbertolini.cocktailhelperapp.data.local.entity.CachedDrinkEntity
import br.com.brunoccbertolini.cocktailhelperapp.data.local.entity.DrinkPreviewEntity

@Database(
    entities = [DrinkPreviewEntity::class, CachedDrinkEntity::class, CachedDrinkDetailEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
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

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Rename the favorites table to a clearer name and index the cache lookup column.
                db.execSQL("ALTER TABLE `cocktails` RENAME TO `favorites`")
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_cached_drinks_drinkType` ON `cached_drinks` (`drinkType`)"
                )
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Cache full drink details for offline access.
                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS `cached_drink_details`
                    (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `category` TEXT,
                     `alcoholic` TEXT, `glass` TEXT, `instructions` TEXT,
                     `thumbnailUrl` TEXT, `ingredients` TEXT NOT NULL,
                     PRIMARY KEY(`id`))"""
                )
            }
        }
    }
}
