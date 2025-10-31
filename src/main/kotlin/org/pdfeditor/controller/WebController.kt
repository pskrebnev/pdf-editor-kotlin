package org.pdfeditor.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WebController {

  /**
   * Serves the main web interface for the PDF editor application.
   * Returns the index template that provides the user interface for PDF manipulation operations.
   *
   * @return The name of the Thymeleaf template to render (index.html)
   */
  @GetMapping("/")
  fun index(): String {
    return "index"
  }
}