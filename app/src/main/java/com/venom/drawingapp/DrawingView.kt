package com.venom.drawingapp

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

val ColorsList: List<Color> =
    listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Magenta, Color.Cyan, Color.Black, Color.White)

data class Line(
    val start: Offset,
    val end: Offset,
    val color: Color = Color.Black,
    val strokeWidth: Dp = 5.dp
)

class DrawingView: ViewModel(){
    val lines = mutableStateListOf<Line>()
    var strokeWidth by mutableStateOf(5.dp)
    var color  by mutableStateOf(Color.Black)
}

fun captureCanvasDrawing(viewModel: DrawingView): Bitmap {
    val width = 1080  // Set the desired width
    val height = 1920 // Set the desired height
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap.asImageBitmap())

    val paint = Paint().apply {
        color = Color.White
    }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

    viewModel.lines.forEach { line ->
        val linepaint = Paint().apply {
            color = line.color
            strokeWidth = line.strokeWidth.value
            strokeCap = StrokeCap.Round
        }
        canvas.drawLine(line.start, line.end, linepaint)
    }
    return bitmap
}

fun saveBitmapToGallery(context: Context, bitmap: Bitmap): Uri? {
    val contentResolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "canvas_drawing.png")
        put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/CanvasDrawings")
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
    }

    val uri: Uri? = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let {
        var outputStream: OutputStream? = null
        try {
            outputStream = contentResolver.openOutputStream(it)
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
        } finally {
            outputStream?.close()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                contentResolver.update(uri, contentValues, null, null)
            }
        }
    }
    return uri
}

