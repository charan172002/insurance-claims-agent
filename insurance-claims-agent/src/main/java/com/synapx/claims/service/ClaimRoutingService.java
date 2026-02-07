package com.synapx.claims.service;

import com.synapx.claims.model.ClaimData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ClaimRoutingService {

    private static final double FAST_TRACK_THRESHOLD = 25000.0;
    
    /**
     * Determine routing based on business rules
     */
    public RoutingDecision determineRouting(ClaimData claimData, List<String> missingFields, 
                                           boolean hasFraudIndicators, boolean isInjuryClaim) {
        log.info("Determining claim routing");
        
        List<String> reasons = new ArrayList<>();
        String route;
        
        // Rule 1: If any mandatory field is missing → Manual review
        if (!missingFields.isEmpty()) {
            route = "MANUAL_REVIEW";
            reasons.add("Missing mandatory fields: " + String.join(", ", missingFields));
            log.info("Routing to MANUAL_REVIEW due to missing fields");
            return new RoutingDecision(route, String.join("; ", reasons));
        }
        
        // Rule 2: If fraud indicators detected → Investigation Flag
        if (hasFraudIndicators) {
            route = "INVESTIGATION_FLAG";
            reasons.add("Fraud indicators detected in claim description");
            log.info("Routing to INVESTIGATION_FLAG due to fraud indicators");
            return new RoutingDecision(route, String.join("; ", reasons));
        }
        
        // Rule 3: If claim type = injury → Specialist Queue
        if (isInjuryClaim) {
            route = "SPECIALIST_QUEUE";
            reasons.add("Injury claim requires specialist review");
            log.info("Routing to SPECIALIST_QUEUE due to injury claim");
            return new RoutingDecision(route, String.join("; ", reasons));
        }
        
        // Rule 4: If estimated damage < $25,000 → Fast-track
        double estimatedDamage = extractEstimatedDamage(claimData);
        if (estimatedDamage > 0 && estimatedDamage < FAST_TRACK_THRESHOLD) {
            route = "FAST_TRACK";
            reasons.add(String.format("Estimated damage ($%.2f) is below fast-track threshold ($%.2f)", 
                                     estimatedDamage, FAST_TRACK_THRESHOLD));
            log.info("Routing to FAST_TRACK due to low damage estimate");
            return new RoutingDecision(route, String.join("; ", reasons));
        }
        
        // Default: Standard processing
        route = "STANDARD_PROCESSING";
        if (estimatedDamage > 0) {
            reasons.add(String.format("Estimated damage ($%.2f) exceeds fast-track threshold", estimatedDamage));
        } else {
            reasons.add("Standard claim processing - all validation checks passed");
        }
        
        log.info("Routing to STANDARD_PROCESSING");
        return new RoutingDecision(route, String.join("; ", reasons));
    }
    
    /**
     * Extract and parse estimated damage amount
     */
    private double extractEstimatedDamage(ClaimData claimData) {
        try {
            if (claimData.getAssetDetails() != null && 
                claimData.getAssetDetails().getEstimatedDamage() != null) {
                String damage = claimData.getAssetDetails().getEstimatedDamage()
                    .replaceAll("[^0-9.]", "");
                return Double.parseDouble(damage);
            }
            
            if (claimData.getOtherMandatoryFields() != null && 
                claimData.getOtherMandatoryFields().getInitialEstimate() != null) {
                String estimate = claimData.getOtherMandatoryFields().getInitialEstimate()
                    .replaceAll("[^0-9.]", "");
                return Double.parseDouble(estimate);
            }
        } catch (NumberFormatException e) {
            log.warn("Unable to parse estimated damage amount");
        }
        
        return 0.0;
    }
    
    /**
     * Inner class to hold routing decision
     */
    public static class RoutingDecision {
        private final String route;
        private final String reasoning;
        
        public RoutingDecision(String route, String reasoning) {
            this.route = route;
            this.reasoning = reasoning;
        }
        
        public String getRoute() {
            return route;
        }
        
        public String getReasoning() {
            return reasoning;
        }
    }
}
