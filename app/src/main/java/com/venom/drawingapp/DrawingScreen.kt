package com.venom.drawingapp

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawScreen(viewModel: DrawingView) {
    val context = LocalContext.current
    var showComposable by remember { mutableStateOf(false) }

    if (showComposable) {
        BasicAlertDialog(
            onDismissRequest = { showComposable = false }, modifier = Modifier
                .border(2.dp, color = Color.Black)
                .fillMaxWidth()
                .fillMaxHeight(.1F)
                .background(Color.White)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        viewModel.strokeWidth = 5.dp
                        showComposable = false
                    }, modifier = Modifier
                        .size(5.dp)
                        .background(Color.LightGray)
                        .border(2.dp, Color.Black),
                    colors = ButtonDefaults.buttonColors(viewModel.color)
                ) {}
                Button(
                    onClick = {
                        viewModel.strokeWidth = 10.dp
                        showComposable = false
                    }, modifier = Modifier
                        .size(10.dp)
                        .background(Color.LightGray)
                        .border(2.dp, Color.Black),
                    colors = ButtonDefaults.buttonColors(viewModel.color)
                ) {}
                Button(
                    onClick = {
                        viewModel.strokeWidth = 15.dp
                        showComposable = false
                    }, modifier = Modifier
                        .size(15.dp)
                        .background(Color.LightGray)
                        .border(2.dp, Color.Black),
                    colors = ButtonDefaults.buttonColors(viewModel.color)
                ) {}
                Button(
                    onClick = {
                        viewModel.strokeWidth = 20.dp
                        showComposable = false
                    }, modifier = Modifier
                        .size(20.dp)
                        .background(Color.LightGray)
                        .border(2.dp, Color.Black),
                    colors = ButtonDefaults.buttonColors(viewModel.color)
                ) {}
            }

        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "DRAWING APP", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(Color.Blue),
                modifier = Modifier.fillMaxWidth()
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.12F)
                    .border(2.dp, Color.Blue, RoundedCornerShape(0, 0, 35, 35)),
                containerColor = Color.LightGray,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        Arrangement.SpaceAround
                    ) {
                        Spacer(modifier = Modifier.size(10.dp))
                        ColorsList.forEach { x ->
                            Button(
                                onClick = { viewModel.color = x },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(x),
                                modifier = Modifier.size(25.dp)
                            ) {}
                            Spacer(modifier = Modifier.size(10.dp))
                        }

                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        Arrangement.SpaceEvenly
                    )
                    {
                        IconButton(
                            onClick = { viewModel.lines.clear() },
                            modifier = Modifier
                                .border(2.dp, Color.Black)
                                .size(30.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_cancel_24),
                                contentDescription = "Favorite",
                                Modifier.fillMaxSize(),
                                tint = Color.Black
                            )
                        }
                        IconButton(
                            onClick = {
                                if (viewModel.lines.isNotEmpty()) {
                                    viewModel.lines.removeLast()
                                } else {
                                    Toast.makeText(context, "Nothing to undo", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            },
                            modifier = Modifier
                                .border(2.dp, Color.Black)
                                .size(30.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.undo_24),
                                contentDescription = "Favorite",
                                Modifier.fillMaxSize(),
                                tint = Color.Black
                            )
                        }
                        IconButton(
                            onClick = {
                                showComposable = true
                            },
                            modifier = Modifier
                                .border(2.dp, Color.Black)
                                .size(30.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.brush_24),
                                contentDescription = "Favorite",
                                Modifier.fillMaxSize(),
                                tint = Color.Black
                            )
                        }
                        IconButton(
                            onClick = {
                                val bitmap = captureCanvasDrawing(viewModel)
                                val uri = saveBitmapToGallery(context, bitmap)
                            },
                            modifier = Modifier
                                .border(2.dp, Color.Black)
                                .size(30.dp),
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.save_24),
                                contentDescription = "Favorite",
                                Modifier.fillMaxSize(),
                                tint = Color.Black
                            )
                        }
                    }
                }

            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Canvas(modifier = Modifier
                .fillMaxHeight(.76F)
                .fillMaxWidth()
                .background(Color.White)
                .border(2.dp, Color.Blue)
                .pointerInput(true) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val line = Line(
                            color = viewModel.color,
                            start = change.position - dragAmount,
                            end = change.position,
                            strokeWidth = viewModel.strokeWidth
                        )
                        viewModel.lines.add(line)
                    }
                }) {
                viewModel.lines.forEach { line ->
                    drawLine(
                        color = line.color,
                        start = line.start,
                        end = line.end,
                        strokeWidth = line.strokeWidth.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}
