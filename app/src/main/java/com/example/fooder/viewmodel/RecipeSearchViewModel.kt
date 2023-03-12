package com.example.fooder.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooder.model.Hit
import com.example.fooder.model.Recipe
import com.example.fooder.model.RecipeSearchApi
import com.example.fooder.model.Response
import kotlinx.coroutines.launch

class RecipeSearchViewModel: ViewModel() {
    var query by mutableStateOf("")

    fun updateQuery(newValue: String) {
        query = newValue
    }

    var recipes = mutableStateListOf<Recipe>()
        private set

    init {
    }

    fun getRecipes() {
        viewModelScope.launch {
            var recipeSearchApi: RecipeSearchApi? = null
            try {
                recipeSearchApi = RecipeSearchApi!!.getInstance()
                recipes.clear()
                var resp: Response = recipeSearchApi.getRecipes(query)
                var hits: List<Hit>? = resp.hits
                if (hits != null) {
                    recipes.addAll(hits.map { it.recipe!! })
                }
            } catch (e: Exception) {
                Log.d("RECIPESEARCHVIEWMODEL", e.message.toString())
            }
        }
    }
}