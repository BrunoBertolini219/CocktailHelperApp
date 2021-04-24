package br.com.brunoccbertolini.cocktailhelperapp.api

import br.com.brunoccbertolini.cocktailhelperapp.model.CocktailList
import br.com.brunoccbertolini.cocktailhelperapp.model.Drink
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkList
import br.com.brunoccbertolini.cocktailhelperapp.util.Constrants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CocktailAPI {

    @GET("api/json/v1/1/filter.php?a=Alcoholic")
    suspend fun getAllAlcoholicDrinks(
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<CocktailList>

    @GET("api/json/v1/1/filter.php?a=Non_Alcoholic")
    suspend fun getAllNoAlcoholicDrinks(
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<CocktailList>

    @GET("api/json/v1/1/lookup.php")
    suspend fun searchDrinksById(
         @Query("i")
         searchDrink: String,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<DrinkList>

    @GET("api/json/v1/1/search.php")
    suspend fun searchDrinksByName(
        @Query("s")
        searchDrink: String,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<CocktailList>

}

