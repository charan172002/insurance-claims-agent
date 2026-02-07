package com.synapx.claims.extractor;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
public class PdfExtractor {

    /**
     * Extract text content from PDF file
     */
    public String extractTextFromPdf(MultipartFile file) throws IOException {
        log.info("Extracting text from PDF: {}", file.getOriginalFilename());
        
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            
            log.info("Successfully extracted {} characters from PDF", text.length());
            return text;
        } catch (IOException e) {
            log.error("Error extracting text from PDF: {}", e.getMessage());
            throw new IOException("Failed to extract text from PDF: " + e.getMessage(), e);
        }
    }
    
    /**
     * Extract text from byte array
     */
    public String extractTextFromPdf(byte[] pdfBytes) throws IOException {
        log.info("Extracting text from PDF byte array");
        
        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            log.error("Error extracting text from PDF bytes: {}", e.getMessage());
            throw new IOException("Failed to extract text from PDF: " + e.getMessage(), e);
        }
    }
}
