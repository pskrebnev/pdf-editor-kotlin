package org.pdfeditor.service

import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.PDDocument
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayOutputStream

@Service
class PdfService {

  /**
   * Removes specified pages from a PDF document.
   * Pages are deleted in descending order to maintain correct indexing during removal.
   * Only valid page numbers within the document range are processed.
   *
   * @param file The source PDF file as MultipartFile
   * @param pagesToDelete List of 1-indexed page numbers to remove
   * @return ByteArray containing the modified PDF data
   */
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

  /**
   * Combines multiple PDF files into a single document.
   * Files are merged in the order they appear in the list.
   * Each page from each file is copied to the new merged document.
   *
   * @param files List of PDF files to combine
   * @return ByteArray containing the combined PDF data
   * @throws Exception if any error occurs during the merging process
   */
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

  /**
   * Extracts specified pages from a PDF document into a new PDF.
   * Pages are extracted in sorted order and only valid page numbers are processed.
   * The extracted pages maintain their original content and formatting.
   *
   * @param file The source PDF file
   * @param pagesToExtract List of 1-indexed page numbers to extract
   * @return ByteArray containing the new PDF with extracted pages
   */
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

  /**
   * Optimizes a PDF document by applying compression to reduce file size.
   * Currently implements basic optimization by rewriting the PDF structure.
   * Future enhancements could include image compression and font optimization.
   *
   * @param file The PDF file to optimize
   * @param compressionLevel Compression level from 0.1 to 1.0 (currently not actively used in implementation)
   * @return ByteArray containing the optimized PDF data
   */
  fun optimizePdf(file: MultipartFile, compressionLevel: Float): ByteArray {
    val document = Loader.loadPDF(file.bytes)

    val outputStream = ByteArrayOutputStream()
    document.save(outputStream)
    document.close()
    return outputStream.toByteArray()
  }

  /**
   * Extracts and returns metadata information from a PDF document.
   * Retrieves document properties such as page count, title, author, subject, and file size.
   * Handles cases where metadata fields may be null or empty.
   *
   * @param file The PDF file to analyze
   * @return Map containing PDF metadata with keys: pageCount, title, author, subject, fileSize
   */
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

