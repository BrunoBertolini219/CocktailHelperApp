package br.com.brunoccbertolini.cocktailhelperapp.di

import android.content.Context
import androidx.room.Room
import br.com.brunoccbertolini.cocktailhelperapp.data.local.dao.CocktailDao
import br.com.brunoccbertolini.cocktailhelperapp.data.local.database.CocktailDatabase
import br.com.brunoccbertolini.cocktailhelperapp.data.remote.api.CocktailApi
import br.com.brunoccbertolini.cocktailhelperapp.data.repository.CocktailRepositoryImpl
import br.com.brunoccbertolini.cocktailhelperapp.domain.repository.CocktailRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://www.thecocktaildb.com/"

    @Singleton
    @Provides
    fun provideCocktailDatabase(
        @ApplicationContext context: Context
    ): CocktailDatabase = Room.databaseBuilder(
        context,
        CocktailDatabase::class.java,
        CocktailDatabase.DATABASE_NAME
    ).addMigrations(CocktailDatabase.MIGRATION_1_2).build()

    @Singleton
    @Provides
    fun provideCocktailDao(database: CocktailDatabase): CocktailDao =
        database.getCocktailDao()

    @Singleton
    @Provides
    fun provideCocktailApi(): CocktailApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
        .create(CocktailApi::class.java)

    @Singleton
    @Provides
    fun provideCocktailRepository(
        api: CocktailApi,
        dao: CocktailDao
    ): CocktailRepository = CocktailRepositoryImpl(dao, api)
}
