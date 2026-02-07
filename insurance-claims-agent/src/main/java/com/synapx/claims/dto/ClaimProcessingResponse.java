package com.synapx.claims.dto;

import com.synapx.claims.model.ClaimData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimProcessingResponse {
    
    private Map<String, Object> extractedFields;
    private List<String> missingFields;
    private String recommendedRoute;
    private String reasoning;
    
    // Additional metadata
    private ProcessingMetadata metadata;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcessingMetadata {
        private String processingTimestamp;
        private String documentType;
        private Integer confidenceScore;
        private List<String> warnings;
    }
}
