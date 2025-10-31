# PDF Manipulaton tool

A PDF manipulation tool with web interface and REST API.

## Features

- **Delete Pages**: Remove unwanted pages from PDF documents
- **Combine PDFs**: Merge multiple PDF files into a single document
- **Extract Pages**: Extract specific pages from a PDF into a new document
- **Optimize PDFs**: Reduce file size by compressing PDF documents with configurable compression levels
- **Web Interface**: Clean web UI for easy file operations
- **REST API**: Full API endpoints for programmatic access

## Technologies Used

- **Backend**: Kotlin, Spring Boot 3.2.0
- **PDF Processing**: Apache PDFBox 3.0.1
- **Frontend**: HTML5, CSS3, JavaScript
- **Build Tool**: Gradle
- **Other Tools**: Claude.ai (as a reference tool) 

## Prerequisites

- Java 17 or higher
- Gradle (included via wrapper)

## Setup and Running

1. **Clone or download the project**
   ```bash
   cd pdf-editor-kotlin
   ```

2. **Build the application**
   ```bash
   ./gradlew build
   ```

3. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

4. **Access the application**
   - Web Interface: http://localhost:8080
   - API Base URL: http://localhost:8080/api/pdf

## API Endpoints

### Delete Pages
```
POST /api/pdf/delete-pages
Content-Type: multipart/form-data

Parameters:
- file: PDF file
- pages: Comma-separated page numbers (e.g., "1,3,5-7")
```

### Combine PDFs
```
POST /api/pdf/combine
Content-Type: multipart/form-data

Parameters:
- files: Multiple PDF files
```

### Extract Pages
```
POST /api/pdf/extract-pages
Content-Type: multipart/form-data

Parameters:
- file: PDF file
- pages: Comma-separated page numbers (e.g., "1,3,5-7")
```

### Optimize PDF
```
POST /api/pdf/optimize
Content-Type: multipart/form-data

Parameters:
- file: PDF file
- compressionLevel: Float value between 0.1 and 1.0 (default: 0.8)
```

### Get PDF Info
```
POST /api/pdf/info
Content-Type: multipart/form-data

Parameters:
- file: PDF file

Returns:
- pageCount: Number of pages
- title: Document title
- author: Document author
- subject: Document subject
- fileSize: File size in bytes
```

## Usage Examples

### Using cURL

**Delete pages 1, 3, and 5:**
```bash
curl -X POST http://localhost:8080/api/pdf/delete-pages \
  -F "file=@document.pdf" \
  -F "pages=1,3,5" \
  --output modified-document.pdf
```

**Combine multiple PDFs:**
```bash
curl -X POST http://localhost:8080/api/pdf/combine \
  -F "files=@doc1.pdf" \
  -F "files=@doc2.pdf" \
  -F "files=@doc3.pdf" \
  --output combined.pdf
```

**Extract pages 2-4:**
```bash
curl -X POST http://localhost:8080/api/pdf/extract-pages \
  -F "file=@document.pdf" \
  -F "pages=2,3,4" \
  --output extracted-pages.pdf
```

**Optimize PDF with 60% compression:**
```bash
curl -X POST http://localhost:8080/api/pdf/optimize \
  -F "file=@document.pdf" \
  -F "compressionLevel=0.6" \
  --output optimized-document.pdf
```

## Configuration

The application can be configured via `src/main/resources/application.properties`:

```properties
# Server port
server.port=8080

# File upload limits
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=500MB

# Logging
logging.level.org.pdfeditor=INFO
```

## Project Structure

```
src/
├── main/
│   ├── kotlin/
│   │   ├── Main.kt                            # Application entry point
│   │   └── org/
│   │       └── pdfeditor/
│   │           ├── controller/
│   │           │   ├── PdfController.kt       # REST API endpoints
│   │           │   └── WebController.kt       # Web interface controller
│   │           └── service/
│   │               └── PdfService.kt          # PDF processing logic
│   └── resources/
│       ├── static/
│       │   ├── css/
│       │   │   └── style.css                  # Application styles
│       │   └── js/
│       │       └── app.js                     # Frontend JavaScript
│       ├── templates/
│       │   └── index.html                     # Web interface template
│       └── application.properties             # Configuration
└── test/
    └── kotlin/                                # Test files
```

## Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Thymeleaf
- Apache PDFBox 3.0.1
- Jackson Kotlin Module
- Kotlin Reflect
- Commons FileUpload
- Commons IO

## Error Handling

The application includes comprehensive error handling:
- Invalid page numbers are ignored
- File format validation
- Size limits for uploads
- User-friendly error messages in the web interface
- Proper HTTP status codes in API responses

## Security Considerations

- File uploads are limited to PDF format
- Maximum file sizes are configured to prevent abuse
- No file persistence on server (files are processed in memory)
- CORS is enabled for development (consider restricting in production)

## Vulnerability

- Library for file uploading "commons-io:commons-io:2.10.0" is changed to "commons-io:commons-io:2.14.0"

## Testing

- Functionality of 'Delete Pages', 'Extract Pages' where generally tested

## Bugs / Issues

- The product is offered "as is". I probably won't have time to fix bugs or make changes.

## Code Documentation

All public methods in the application are thoroughly documented with KDoc comments, including:

- **Purpose**: What the method does
- **Parameters**: Description of input parameters and their expected format
- **Return values**: What the method returns
- **Usage examples**: Where applicable, especially for page number formats

### API Method Documentation

- `PdfController`: All REST endpoints are documented with parameter descriptions and response formats
- `PdfService`: Core PDF processing methods with detailed implementation notes
- `WebController`: Web interface routing documentation

## Development

To modify the application:

1. **Backend changes**: Edit files in `src/main/kotlin/org/pdfeditor/`
2. **Frontend changes**: 
   - HTML structure: Edit `src/main/resources/templates/index.html`
   - Styling: Edit `src/main/resources/static/css/style.css`
   - JavaScript functionality: Edit `src/main/resources/static/js/app.js`
3. **Configuration**: Modify `src/main/resources/application.properties`

### Code Organization

The application follows separation of concerns with:
- **Modular CSS**: Styles separated into dedicated CSS file for maintainability
- **External JavaScript**: All frontend logic in separate JS file
- **Clean HTML**: Template focused on structure without inline styles or scripts

Build and run tests:
```bash
./gradlew test
./gradlew build
```

## License

This project is open source and available under the MIT License.