package com.example.fooder.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fooder.R
import com.example.fooder.model.TabItem
import com.example.fooder.ui.theme.FooderTheme
import com.example.fooder.viewmodel.NavigationViewModel
import com.example.fooder.viewmodel.RecipeSearchViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FooderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun MyApp() {
    val pages = listOf(
        TabItem("Home", Icons.Filled.Home, "home"),
        TabItem("Favourites", Icons.Filled.Favorite, "favourites"),
        TabItem("Info", Icons.Filled.Info, "info"),
    )
    PageSkeleton(items = pages)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FooderTheme {
        MyApp()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageSkeleton(items: List<TabItem>) {
    val navController = rememberNavController()
    Scaffold(
        topBar = { SmallTopAppBar(title = { Text(stringResource(id = R.string.app_name) ) }) },
        content = {
            Box(modifier = Modifier.padding(it))
            {
                MyNavController(navController = navController)
            }
                  },
        bottomBar = { MyBottomNavigation(items, navController) }
    )
}

@Composable
fun MainScreen() {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        RecipeSearch()
    }
}

@Composable
fun InfoScreen() {
    Text("Info")
}

@Composable
fun FavouritesScreen() {
    Text("Favourites")
}

@Composable
fun MyBottomNavigation(items: List<TabItem>, navController: NavController, navigationViewModel: NavigationViewModel = viewModel()) {
    NavigationBar {
        items.forEachIndexed{index,item->
            NavigationBarItem(
                selected = navigationViewModel.selected == index,
                onClick = {
                    navigationViewModel.updateSelected(index)
                    navController.navigate(item.route)
                          },
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(item.label) }
            )

        }
    }
}

@Composable
fun MyNavController(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home",
    ) {
        composable("home") {
            MainScreen()
        }
        composable("favourites") {
            FavouritesScreen()
        }
        composable("info") {
            InfoScreen()
        }
    }
}

@Composable
fun RecipeSearch(recipeSearchViewModel: RecipeSearchViewModel = viewModel()) {
    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            value = recipeSearchViewModel.query,
            onValueChange = { recipeSearchViewModel.updateQuery(it) },
            singleLine = true,
            label = { Text(text = "Input ingredient") }
        )
        Button(onClick = {
            recipeSearchViewModel.getRecipes()
        }) {
            Text(text = "Find recipe")
        }
        LazyColumn {
            items(recipeSearchViewModel.recipes) {recipe->
                Text(recipe.label ?: "")
            }
        }
    }
}
