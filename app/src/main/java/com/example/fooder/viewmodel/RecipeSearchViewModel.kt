package com.example.fooder.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooder.model.Hit
import com.example.fooder.model.Recipe
import com.example.fooder.model.RecipeSearchApi
import com.example.fooder.model.Response
import kotlinx.coroutines.launch

sealed interface RecipeSearchUiState {
    data class Success(val recipes: List<Recipe>): RecipeSearchUiState
    object Error: RecipeSearchUiState
    object Loading: RecipeSearchUiState
}

class RecipeSearchViewModel: ViewModel() {
    var query by mutableStateOf("")

    fun updateQuery(newValue: String) {
        query = newValue
    }

    var recipeSearchUiState by mutableStateOf<RecipeSearchUiState>(
        RecipeSearchUiState.Success(listOf())
    )
        private set

    fun getRecipes() {
        recipeSearchUiState = RecipeSearchUiState.Loading
        viewModelScope.launch {
            try {
                val recipeSearchApi = RecipeSearchApi.getInstance()
                val resp: Response = recipeSearchApi.getRecipes(query)
                val hits: List<Hit>? = resp.hits
                if (hits != null) {
                    recipeSearchUiState = RecipeSearchUiState.Success(hits.map { it.recipe!! })
                } else {
                    recipeSearchUiState = RecipeSearchUiState.Error
                }
            } catch (e: Exception) {
                Log.d("RECIPESEARCHVIEWMODEL", e.message.toString())
                recipeSearchUiState = RecipeSearchUiState.Error
            }
        }
    }
}