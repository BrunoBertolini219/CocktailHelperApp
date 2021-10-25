package br.com.brunoccbertolini.cocktailhelperapp.di

import android.content.Context
import androidx.room.Room
import br.com.brunoccbertolini.cocktailhelperapp.api.CocktailAPI
import br.com.brunoccbertolini.cocktailhelperapp.db.CocktailDao
import br.com.brunoccbertolini.cocktailhelperapp.db.CocktailDatabase
import br.com.brunoccbertolini.cocktailhelperapp.repositories.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.repositories.DefaultCocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.Constrants.Companion.BASE_URL
import br.com.brunoccbertolini.cocktailhelperapp.util.Constrants.Companion.DATABASE_NAME
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

    @Singleton
    @Provides
    fun provideCocktailDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, CocktailDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideCocktailDao(
        database: CocktailDatabase
    ) = database.getCocktailDao()

    @Singleton
    @Provides
    fun provideCocktailApi(): CocktailAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(CocktailAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideDefaultCocktailRepository(
        api: CocktailAPI,
        dao: CocktailDao
    ): CocktailRepository = DefaultCocktailRepository(dao, api)


}