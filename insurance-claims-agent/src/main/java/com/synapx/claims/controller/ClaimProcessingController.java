package com.synapx.claims.controller;

import com.synapx.claims.dto.ClaimProcessingResponse;
import com.synapx.claims.service.ClaimProcessingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/claims")
@RequiredArgsConstructor
@Tag(name = "Claims Processing", description = "Endpoints for processing insurance claim documents")
public class ClaimProcessingController {

    private final ClaimProcessingService claimProcessingService;

    @PostMapping(value = "/process", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Process FNOL Document",
        description = "Upload and process a First Notice of Loss (FNOL) document. " +
                     "Extracts key fields, validates data, and determines routing.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully processed claim",
                content = @Content(schema = @Schema(implementation = ClaimProcessingResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid file or bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    public ResponseEntity<?> processClaim(@RequestParam("file") MultipartFile file) {
        log.info("Received claim processing request for file: {}", file.getOriginalFilename());
        
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("File is empty"));
            }
            
            if (!isPdfFile(file)) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Only PDF files are supported"));
            }
            
            // Process claim
            ClaimProcessingResponse response = claimProcessingService.processClaim(file);
            
            log.info("Successfully processed claim. Route: {}", response.getRecommendedRoute());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error processing claim: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Error processing claim: " + e.getMessage()));
        }
    }
    
    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Check if the service is running")
    public ResponseEntity<HealthResponse> healthCheck() {
        return ResponseEntity.ok(new HealthResponse("UP", "Claims Processing Service is running"));
    }
    
    private boolean isPdfFile(MultipartFile file) {
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();
        
        return (contentType != null && contentType.equals("application/pdf")) ||
               (filename != null && filename.toLowerCase().endsWith(".pdf"));
    }
    
    // Inner classes for responses
    private record ErrorResponse(String error) {}
    private record HealthResponse(String status, String message) {}
}
