package org.pdfeditor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PdfEditorApplication

fun main(args: Array<String>) {
    runApplication<PdfEditorApplication>(*args)
}