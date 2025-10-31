// File info display functions
function displayFileInfo(file, infoElementId) {
    const infoElement = document.getElementById(infoElementId);
    const formData = new FormData();
    formData.append('file', file);

    fetch('/api/pdf/info', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        infoElement.innerHTML = `
            <h4>File Information</h4>
            <p><strong>File:</strong> ${file.name}</p>
            <p><strong>Size:</strong> ${(file.size / (1024*1024)).toFixed(2)} MB</p>
            <p><strong>Pages:</strong> ${data.pageCount}</p>
            ${data.title ? `<p><strong>Title:</strong> ${data.title}</p>` : ''}
            ${data.author ? `<p><strong>Author:</strong> ${data.author}</p>` : ''}
        `;
        infoElement.style.display = 'block';
    })
    .catch(error => {
        console.error('Error getting file info:', error);
    });
}

// Event listeners for file inputs
document.getElementById('deleteFile').addEventListener('change', function() {
    if (this.files[0]) {
        displayFileInfo(this.files[0], 'deleteFileInfo');
    }
});

document.getElementById('extractFile').addEventListener('change', function() {
    if (this.files[0]) {
        displayFileInfo(this.files[0], 'extractFileInfo');
    }
});

document.getElementById('optimizeFile').addEventListener('change', function() {
    if (this.files[0]) {
        displayFileInfo(this.files[0], 'optimizeFileInfo');
    }
});

document.getElementById('combineFiles').addEventListener('change', function() {
    const infoElement = document.getElementById('combineFileInfo');
    if (this.files.length > 0) {
        let html = '<h4>Selected Files</h4>';
        for (let i = 0; i < this.files.length; i++) {
            html += `<p>${i + 1}. ${this.files[i].name} (${(this.files[i].size / (1024*1024)).toFixed(2)} MB)</p>`;
        }
        infoElement.innerHTML = html;
        infoElement.style.display = 'block';
    }
});

// Compression slider
document.getElementById('compressionSlider').addEventListener('input', function() {
    document.getElementById('compressionValue').textContent = Math.round(this.value * 100) + '%';
});

// Utility functions
function showLoading(loadingId) {
    document.getElementById(loadingId).style.display = 'block';
}

function hideLoading(loadingId) {
    document.getElementById(loadingId).style.display = 'none';
}

function showError(errorId, message) {
    const errorElement = document.getElementById(errorId);
    errorElement.textContent = message;
    errorElement.style.display = 'block';
    setTimeout(() => {
        errorElement.style.display = 'none';
    }, 5000);
}

function showSuccess(successId, message) {
    const successElement = document.getElementById(successId);
    successElement.textContent = message;
    successElement.style.display = 'block';
    setTimeout(() => {
        successElement.style.display = 'none';
    }, 3000);
}

function downloadFile(blob, filename) {
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);
}

// Main operation functions
function deletePages() {
    const fileInput = document.getElementById('deleteFile');
    const pagesInput = document.getElementById('deletePages');
    
    if (!fileInput.files[0]) {
        showError('deleteError', 'Please select a PDF file');
        return;
    }
    
    if (!pagesInput.value.trim()) {
        showError('deleteError', 'Please specify pages to delete');
        return;
    }

    const formData = new FormData();
    formData.append('file', fileInput.files[0]);
    formData.append('pages', pagesInput.value);

    showLoading('deleteLoading');

    fetch('/api/pdf/delete-pages', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (!response.ok) throw new Error('Failed to process PDF');
        return response.blob();
    })
    .then(blob => {
        downloadFile(blob, `modified-${fileInput.files[0].name}`);
        showSuccess('deleteSuccess', 'Pages deleted successfully!');
    })
    .catch(error => {
        showError('deleteError', 'Error: ' + error.message);
    })
    .finally(() => {
        hideLoading('deleteLoading');
    });
}

function combinePdfs() {
    const fileInput = document.getElementById('combineFiles');
    
    if (fileInput.files.length < 2) {
        showError('combineError', 'Please select at least 2 PDF files');
        return;
    }

    const formData = new FormData();
    for (let i = 0; i < fileInput.files.length; i++) {
        formData.append('files', fileInput.files[i]);
    }

    showLoading('combineLoading');

    fetch('/api/pdf/combine', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (!response.ok) throw new Error('Failed to combine PDFs');
        return response.blob();
    })
    .then(blob => {
        downloadFile(blob, 'combined.pdf');
        showSuccess('combineSuccess', 'PDFs combined successfully!');
    })
    .catch(error => {
        showError('combineError', 'Error: ' + error.message);
    })
    .finally(() => {
        hideLoading('combineLoading');
    });
}

function extractPages() {
    const fileInput = document.getElementById('extractFile');
    const pagesInput = document.getElementById('extractPages');
    
    if (!fileInput.files[0]) {
        showError('extractError', 'Please select a PDF file');
        return;
    }
    
    if (!pagesInput.value.trim()) {
        showError('extractError', 'Please specify pages to extract');
        return;
    }

    const formData = new FormData();
    formData.append('file', fileInput.files[0]);
    formData.append('pages', pagesInput.value);

    showLoading('extractLoading');

    fetch('/api/pdf/extract-pages', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (!response.ok) throw new Error('Failed to extract pages');
        return response.blob();
    })
    .then(blob => {
        downloadFile(blob, `extracted-${fileInput.files[0].name}`);
        showSuccess('extractSuccess', 'Pages extracted successfully!');
    })
    .catch(error => {
        showError('extractError', 'Error: ' + error.message);
    })
    .finally(() => {
        hideLoading('extractLoading');
    });
}

function optimizePdf() {
    const fileInput = document.getElementById('optimizeFile');
    const compressionSlider = document.getElementById('compressionSlider');
    
    if (!fileInput.files[0]) {
        showError('optimizeError', 'Please select a PDF file');
        return;
    }

    const formData = new FormData();
    formData.append('file', fileInput.files[0]);
    formData.append('compressionLevel', compressionSlider.value);

    showLoading('optimizeLoading');

    fetch('/api/pdf/optimize', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (!response.ok) throw new Error('Failed to optimize PDF');
        return response.blob();
    })
    .then(blob => {
        downloadFile(blob, `optimized-${fileInput.files[0].name}`);
        showSuccess('optimizeSuccess', 'PDF optimized successfully!');
    })
    .catch(error => {
        showError('optimizeError', 'Error: ' + error.message);
    })
    .finally(() => {
        hideLoading('optimizeLoading');
    });
}