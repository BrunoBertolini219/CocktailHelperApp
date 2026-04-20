package br.com.brunoccbertolini.cocktailhelperapp.data.remote.api

import br.com.brunoccbertolini.cocktailhelperapp.data.remote.dto.CocktailListDto
import br.com.brunoccbertolini.cocktailhelperapp.data.remote.dto.DrinkListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApi {

    @GET("api/json/v1/1/filter.php?a=Alcoholic")
    suspend fun getAllAlcoholicDrinks(): Response<CocktailListDto>

    @GET("api/json/v1/1/filter.php?a=Non_Alcoholic")
    suspend fun getAllNoAlcoholicDrinks(): Response<CocktailListDto>

    @GET("api/json/v1/1/lookup.php")
    suspend fun searchDrinksById(@Query("i") id: String): Response<DrinkListDto>

    @GET("api/json/v1/1/search.php")
    suspend fun searchDrinksByName(@Query("s") name: String): Response<CocktailListDto>

    @GET("api/json/v1/1/filter.php")
    suspend fun searchDrinksByIngredient(@Query("i") ingredient: String): Response<CocktailListDto>

    @GET("api/json/v1/1/random.php")
    suspend fun getRandomDrink(): Response<DrinkListDto>
}
