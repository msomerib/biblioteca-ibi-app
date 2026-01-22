package com.bibliotecaibi.util

import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri

object PdfUtil {

    fun writeTablePdf(
        context: Context,
        uri: Uri,
        title: String,
        headers: List<String>,
        rows: List<List<String>>
    ) {
        val pdf = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4
        val page = pdf.startPage(pageInfo)
        val canvas = page.canvas

        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 14f
        }

        var y = 40f
        canvas.drawText(title, 40f, y, paint)
        y += 25f

        // Cabeçalho
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText(headers.joinToString(" | "), 40f, y, paint)
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        y += 20f

        // conteúdo da tabela
        for (row in rows) {
            canvas.drawText(row.joinToString(" | "), 40f, y, paint)
            y += 18f
        }

        pdf.finishPage(page)

        // Gravar no arquivo
        context.contentResolver.openOutputStream(uri)?.use { out ->
            pdf.writeTo(out)
        }

        pdf.close()
    }
}
