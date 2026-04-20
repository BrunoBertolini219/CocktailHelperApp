package br.com.brunoccbertolini.cocktailhelperapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import br.com.brunoccbertolini.cocktailhelperapp.presentation.navigation.CocktailApp
import br.com.brunoccbertolini.cocktailhelperapp.ui.theme.CocktailHelperAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CocktailHelperAppTheme {
                CocktailApp(windowSizeClass = calculateWindowSizeClass(this))
            }
        }
    }
}
