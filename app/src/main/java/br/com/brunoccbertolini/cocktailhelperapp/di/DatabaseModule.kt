package br.com.brunoccbertolini.cocktailhelperapp.di

import android.content.Context
import androidx.room.Room
import br.com.brunoccbertolini.cocktailhelperapp.data.local.dao.CocktailDao
import br.com.brunoccbertolini.cocktailhelperapp.data.local.database.CocktailDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCocktailDatabase(
        @ApplicationContext context: Context
    ): CocktailDatabase = Room.databaseBuilder(
        context,
        CocktailDatabase::class.java,
        CocktailDatabase.DATABASE_NAME
    ).addMigrations(
        CocktailDatabase.MIGRATION_1_2,
        CocktailDatabase.MIGRATION_2_3,
        CocktailDatabase.MIGRATION_3_4
    ).build()

    @Provides
    @Singleton
    fun provideCocktailDao(database: CocktailDatabase): CocktailDao =
        database.getCocktailDao()
}
