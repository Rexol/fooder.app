package com.example.fooder.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
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
            label = { Text(text = "Input ingredient") },
            modifier = Modifier.fillMaxWidth(),
        )
        Button(onClick = onClick) {
            Text(
                text = "Find recipe",
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

@Composable
fun SearchResults(uiState: RecipeSearchUiState) {
    when (uiState) {
        is RecipeSearchUiState.Loading -> LoadingScreen()
        is RecipeSearchUiState.Success -> Recipes(
                recipes = (uiState as RecipeSearchUiState.Success).recipes,
            )
        is RecipeSearchUiState.Error -> ErrorScreen()
    }
}

@Composable
fun LoadingScreen() {
    return Text(
        text = "Loading...",
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
fun ErrorScreen() {
    return Text(
        text = "Error has happened, while getting the data",
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
fun Recipes(recipes: List<Recipe>) {
    Column() {
        Text(text = "Found %d recipes".format(recipes.size))
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
//            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            AsyncImage(
                model = recipe.image,
                contentDescription = null,
//                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Fit,
            )
            Column (
//                modifier = Modifier.fillMaxWidth()
            ) {
                Text (
                    text = "Calories %.2f cal".format(recipe.calories ?: 0)
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
