package com.venom.drawingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.venom.drawingapp.ui.theme.DrawingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            DrawingAppTheme {
                val viewModel: DrawingView = viewModel()
                WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
                DrawScreen(viewModel)
            }
        }
    }
}

