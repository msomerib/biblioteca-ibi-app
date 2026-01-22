package com.bibliotecaibi.util

import android.content.Context
import android.net.Uri
import org.apache.poi.xssf.usermodel.XSSFWorkbook

object ExcelUtil {

    fun writeExcel(
        context: Context,
        uri: Uri,
        sheetName: String,
        headers: List<String>,
        rows: List<List<String>>
    ) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet(sheetName)

        var rowIndex = 0

        // Cabeçalho
        val headerRow = sheet.createRow(rowIndex++)
        headers.forEachIndexed { i, h ->
            headerRow.createCell(i).setCellValue(h)
        }

        // Linhas de dados
        for (row in rows) {
            val excelRow = sheet.createRow(rowIndex++)
            row.forEachIndexed { index, value ->
                excelRow.createCell(index).setCellValue(value)
            }
        }

        context.contentResolver.openOutputStream(uri)?.use { out ->
            workbook.write(out)
        }

        workbook.close()
    }
}
