package yk.example.pamproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import yk.example.pamproject.navigation.AppNavHost
import yk.example.pamproject.ui.theme.PamprojectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PamprojectTheme {
                Surface(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
                    AppNavHost()
                }
            }
        }
    }
}
