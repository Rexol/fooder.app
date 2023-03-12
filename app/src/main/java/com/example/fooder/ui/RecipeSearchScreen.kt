package com.example.fooder.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.fooder.R
import com.example.fooder.model.Recipe
import com.example.fooder.viewmodel.RecipeSearchUiState
import com.example.fooder.viewmodel.RecipeSearchViewModel

@Composable
fun RecipeSearchPage(recipeSearchViewModel: RecipeSearchViewModel = viewModel()) {
    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SearchForm(
            value = recipeSearchViewModel.query,
            onChange = { recipeSearchViewModel.updateQuery(it) },
            onClick = { recipeSearchViewModel.getRecipes() },
        )
        SearchResults(uiState = recipeSearchViewModel.recipeSearchUiState)
    }
}

@Composable
fun SearchForm(value: String, onChange: (String) -> Unit, onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            value = value,
            onValueChange = onChange,
            singleLine = true,
            label = { Text(text = stringResource(R.string.search_field_label)) },
            modifier = Modifier.fillMaxWidth(),
        )
        Button(onClick = onClick) {
            Text(
                text = stringResource(R.string.search_btn_txt),
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

@Composable
fun SearchResults(uiState: RecipeSearchUiState) {
    when (uiState) {
        is RecipeSearchUiState.Loading -> LoadingScreen()
        is RecipeSearchUiState.Success -> Recipes(recipes = uiState.recipes)
        is RecipeSearchUiState.Error -> ErrorScreen()
    }
}

@Composable
fun LoadingScreen() {
    return Text(
        text = stringResource(R.string.loading_txt),
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
fun ErrorScreen() {
    return Text(
        text = stringResource(R.string.api_call_err_msg),
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
fun Recipes(recipes: List<Recipe>) {
    Column() {
        Text(text = stringResource(R.string.found_items_count, recipes.size))
        LazyColumn {
            items(recipes) {recipe->
                RecipeTile(recipe = recipe)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RecipeTile(recipe: Recipe) {
    ElevatedCard (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 15.dp),
    ) {
        Text(
            text = recipe.label ?: "",
            style = MaterialTheme.typography.headlineSmall
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            AsyncImage(
                model = recipe.image,
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )
            Column (
            ) {
                Text (
                    text = stringResource(R.string.calories_amnt, recipe.calories ?: 0)
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    val items : List<String> = recipe.healthLabels ?: listOf()
                    items.forEach {
                        SuggestionChip(
                            onClick = {},
                            label = { Text(
                                text = it,
                                style = MaterialTheme.typography.labelSmall,
                            ) },
                        )
                    }
                }
            }
        }
    }
}
