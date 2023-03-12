package com.example.fooder.model

import com.example.fooder.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class Recipe (
    var label: String?,
    var image: String?,
    var url: String?,
    var shareAs: String?,
    var healthLabels: List<String>?,
    var calories: Float?,
)

data class Hit (
    var recipe: Recipe?
)

data class Response (
    var hits: List<Hit>?
)

const val APP_ID = BuildConfig.APP_ID
const val APP_KEY = BuildConfig.APP_KEY
const val BASE_URL = "https://api.edamam.com/"

interface RecipeSearchApi {
    @GET("search")
    suspend fun getRecipes(
        @Query("q") query: String,
        @Query("app_key") app_key: String = APP_KEY,
        @Query("app_id") app_id: String = APP_ID,
    ): Response

    companion object {
        var recipeSearchService: RecipeSearchApi? = null

        fun getInstance(): RecipeSearchApi {
            if (recipeSearchService === null) {
                recipeSearchService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(RecipeSearchApi::class.java)
            }
            return recipeSearchService!!
        }
    }
}