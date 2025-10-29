package org.pdfeditor.service

import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@Service
class PdfService {

    fun deletePages(file: MultipartFile, pagesToDelete: List<Int>): ByteArray {
        val document = Loader.loadPDF(file.bytes)
        
        val sortedPages = pagesToDelete.sortedDescending()
        
        for (pageNumber in sortedPages) {
            if (pageNumber > 0 && pageNumber <= document.numberOfPages) {
                document.removePage(pageNumber - 1)
            }
        }
        
        val outputStream = ByteArrayOutputStream()
        document.save(outputStream)
        document.close()
        return outputStream.toByteArray()
    }

    fun combinePdfs(files: List<MultipartFile>): ByteArray {
        val mergedDocument = PDDocument()
        
        try {
            for (file in files) {
                val document = Loader.loadPDF(file.bytes)
                for (i in 0 until document.numberOfPages) {
                    val page = document.getPage(i)
                    mergedDocument.addPage(page)
                }
                document.close()
            }
            
            val outputStream = ByteArrayOutputStream()
            mergedDocument.save(outputStream)
            mergedDocument.close()
            return outputStream.toByteArray()
        } catch (e: Exception) {
            mergedDocument.close()
            throw e
        }
    }

    fun extractPages(file: MultipartFile, pagesToExtract: List<Int>): ByteArray {
        val sourceDocument = Loader.loadPDF(file.bytes)
        val extractedDocument = PDDocument()
        
        for (pageNumber in pagesToExtract.sorted()) {
            if (pageNumber > 0 && pageNumber <= sourceDocument.numberOfPages) {
                val page = sourceDocument.getPage(pageNumber - 1)
                extractedDocument.addPage(page)
            }
        }
        
        val outputStream = ByteArrayOutputStream()
        extractedDocument.save(outputStream)
        sourceDocument.close()
        extractedDocument.close()
        return outputStream.toByteArray()
    }

    fun optimizePdf(file: MultipartFile, compressionLevel: Float): ByteArray {
        val document = Loader.loadPDF(file.bytes)
        
        val outputStream = ByteArrayOutputStream()
        document.save(outputStream)
        document.close()
        return outputStream.toByteArray()
    }

    fun getPdfInfo(file: MultipartFile): Map<String, Any> {
        val document = Loader.loadPDF(file.bytes)
        
        val info = mapOf(
            "pageCount" to document.numberOfPages,
            "title" to (document.documentInformation?.title ?: ""),
            "author" to (document.documentInformation?.author ?: ""),
            "subject" to (document.documentInformation?.subject ?: ""),
            "fileSize" to file.size
        )
        
        document.close()
        return info
    }
}