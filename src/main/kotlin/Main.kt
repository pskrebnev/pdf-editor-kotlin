package org.pdfeditor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PdfEditorApplication

/**
 * Main entry point for the PDF Editor Spring Boot application.
 * Starts the application server and initializes all components including web controllers,
 * services, and the embedded Tomcat server.
 * 
 * @param args Command line arguments passed to the application
 */
fun main(args: Array<String>) {
    runApplication<PdfEditorApplication>(*args)
}