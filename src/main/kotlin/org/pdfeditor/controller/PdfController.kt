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

    @PostMapping("/info")
    fun getPdfInfo(@RequestParam("file") file: MultipartFile): ResponseEntity<Map<String, Any>> {
        val info = pdfService.getPdfInfo(file)
        return ResponseEntity.ok(info)
    }
}