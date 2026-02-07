package com.synapx.claims.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synapx.claims.dto.ClaimProcessingResponse;
import com.synapx.claims.extractor.FieldExtractor;
import com.synapx.claims.extractor.PdfExtractor;
import com.synapx.claims.model.ClaimData;
import com.synapx.claims.validator.ClaimValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClaimProcessingService {

    private final PdfExtractor pdfExtractor;
    private final FieldExtractor fieldExtractor;
    private final ClaimValidator claimValidator;
    private final ClaimRoutingService routingService;
    private final ObjectMapper objectMapper;

    /**
     * Process FNOL document and return routing decision
     */
    public ClaimProcessingResponse processClaim(MultipartFile file) throws Exception {
        log.info("Starting claim processing for file: {}", file.getOriginalFilename());
        
        // Step 1: Extract text from PDF
        String extractedText = pdfExtractor.extractTextFromPdf(file);
        
        // Step 2: Extract structured fields
        ClaimData claimData = fieldExtractor.extractFields(extractedText);
        
        // Step 3: Validate and find missing fields
        List<String> missingFields = claimValidator.validateAndFindMissingFields(claimData);
        
        // Step 4: Check for fraud indicators
        boolean hasFraudIndicators = claimValidator.hasFraudIndicators(claimData);
        
        // Step 5: Check if injury claim
        boolean isInjuryClaim = claimValidator.isInjuryClaim(claimData);
        
        // Step 6: Determine routing
        ClaimRoutingService.RoutingDecision routing = routingService.determineRouting(
            claimData, missingFields, hasFraudIndicators, isInjuryClaim
        );
        
        // Step 7: Build response
        ClaimProcessingResponse response = buildResponse(
            claimData, missingFields, routing, file.getOriginalFilename()
        );
        
        log.info("Claim processing completed. Route: {}", routing.getRoute());
        return response;
    }
    
    /**
     * Build the final response
     */
    @SuppressWarnings("unchecked")
    private ClaimProcessingResponse buildResponse(ClaimData claimData, List<String> missingFields,
                                                  ClaimRoutingService.RoutingDecision routing,
                                                  String filename) {
        
        // Convert ClaimData to Map for extractedFields
        Map<String, Object> extractedFieldsMap = objectMapper.convertValue(claimData, Map.class);
        
        // Build warnings
        List<String> warnings = new ArrayList<>();
        if (!missingFields.isEmpty()) {
            warnings.add("Some mandatory fields are missing");
        }
        
        // Build metadata
        ClaimProcessingResponse.ProcessingMetadata metadata = 
            ClaimProcessingResponse.ProcessingMetadata.builder()
                .processingTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .documentType("ACORD FNOL")
                .confidenceScore(calculateConfidenceScore(missingFields))
                .warnings(warnings)
                .build();
        
        return ClaimProcessingResponse.builder()
                .extractedFields(extractedFieldsMap)
                .missingFields(missingFields)
                .recommendedRoute(routing.getRoute())
                .reasoning(routing.getReasoning())
                .metadata(metadata)
                .build();
    }
    
    /**
     * Calculate confidence score based on extracted fields
     */
    private Integer calculateConfidenceScore(List<String> missingFields) {
        int totalFields = 15; // Approximate number of key fields
        int extractedFields = totalFields - missingFields.size();
        return (int) ((extractedFields / (double) totalFields) * 100);
    }
}
