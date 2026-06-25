package br.com.brunoccbertolini.cocktailhelperapp.di

import br.com.brunoccbertolini.cocktailhelperapp.data.preferences.PreferencesRepositoryImpl
import br.com.brunoccbertolini.cocktailhelperapp.data.repository.CocktailRepositoryImpl
import br.com.brunoccbertolini.cocktailhelperapp.domain.repository.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.domain.repository.PreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCocktailRepository(impl: CocktailRepositoryImpl): CocktailRepository

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(impl: PreferencesRepositoryImpl): PreferencesRepository
}
