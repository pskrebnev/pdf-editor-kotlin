package org.pdfeditor.controller

import org.pdfeditor.service.PdfService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/pdf")
@CrossOrigin(origins = ["*"])
class PdfController(private val pdfService: PdfService) {

    /**
     * Removes specified pages from a PDF document.
     * Accepts a PDF file and a comma-separated string of page numbers to delete.
     * Page numbers are 1-indexed and can include ranges (e.g., "1,3,5-7").
     * 
     * @param file The PDF file to process
     * @param pages Comma-separated page numbers to delete (e.g., "1,3,5-7")
     * @return ResponseEntity containing the modified PDF as byte array
     */
    @PostMapping("/delete-pages")
    fun deletePages(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("pages") pages: String
    ): ResponseEntity<ByteArray> {
        val pagesToDelete = pages.split(",").mapNotNull { it.trim().toIntOrNull() }
        
        if (pagesToDelete.isEmpty()) {
            return ResponseEntity.badRequest().build()
        }
        
        val result = pdfService.deletePages(file, pagesToDelete)
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"modified-${file.originalFilename}\"")
            .contentType(MediaType.APPLICATION_PDF)
            .body(result)
    }

    /**
     * Combines multiple PDF files into a single document.
     * Takes an array of PDF files and merges them in the order they are provided.
     * 
     * @param files Array of PDF files to combine
     * @return ResponseEntity containing the combined PDF as byte array
     */
    @PostMapping("/combine")
    fun combinePdfs(@RequestParam("files") files: Array<MultipartFile>): ResponseEntity<ByteArray> {
        if (files.isEmpty()) {
            return ResponseEntity.badRequest().build()
        }
        
        val result = pdfService.combinePdfs(files.toList())
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"combined.pdf\"")
            .contentType(MediaType.APPLICATION_PDF)
            .body(result)
    }

    /**
     * Extracts specified pages from a PDF document into a new PDF.
     * Creates a new PDF containing only the specified pages from the source document.
     * Page numbers are 1-indexed and can include ranges (e.g., "1,3,5-7").
     * 
     * @param file The source PDF file
     * @param pages Comma-separated page numbers to extract (e.g., "1,3,5-7")
     * @return ResponseEntity containing the extracted pages as a new PDF byte array
     */
    @PostMapping("/extract-pages")
    fun extractPages(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("pages") pages: String
    ): ResponseEntity<ByteArray> {
        val pagesToExtract = pages.split(",").mapNotNull { it.trim().toIntOrNull() }
        
        if (pagesToExtract.isEmpty()) {
            return ResponseEntity.badRequest().build()
        }
        
        val result = pdfService.extractPages(file, pagesToExtract)
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"extracted-${file.originalFilename}\"")
            .contentType(MediaType.APPLICATION_PDF)
            .body(result)
    }

    /**
     * Optimizes a PDF document by applying compression to reduce file size.
     * Accepts a compression level parameter to control the trade-off between file size and quality.
     * 
     * @param file The PDF file to optimize
     * @param compressionLevel Float value between 0.1 and 1.0 (default: 0.8) - higher values preserve quality
     * @return ResponseEntity containing the optimized PDF as byte array
     */
    @PostMapping("/optimize")
    fun optimizePdf(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("compressionLevel", defaultValue = "0.8") compressionLevel: Float
    ): ResponseEntity<ByteArray> {
        val result = pdfService.optimizePdf(file, compressionLevel)
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"optimized-${file.originalFilename}\"")
            .contentType(MediaType.APPLICATION_PDF)
            .body(result)
    }

    /**
     * Retrieves metadata and information about a PDF document.
     * Returns information such as page count, title, author, subject, and file size.
     * 
     * @param file The PDF file to analyze
     * @return ResponseEntity containing a map with PDF metadata information
     */
    @PostMapping("/info")
    fun getPdfInfo(@RequestParam("file") file: MultipartFile): ResponseEntity<Map<String, Any>> {
        val info = pdfService.getPdfInfo(file)
        return ResponseEntity.ok(info)
    }
}