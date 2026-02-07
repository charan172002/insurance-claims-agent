package com.synapx.claims.validator;

import com.synapx.claims.model.ClaimData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ClaimValidator {

    /**
     * Validate claim data and identify missing mandatory fields
     */
    public List<String> validateAndFindMissingFields(ClaimData claimData) {
        log.info("Validating claim data");
        List<String> missingFields = new ArrayList<>();
        
        // Validate Policy Information
        if (claimData.getPolicyInformation() != null) {
            if (isNullOrEmpty(claimData.getPolicyInformation().getPolicyNumber())) {
                missingFields.add("Policy Number");
            }
            if (isNullOrEmpty(claimData.getPolicyInformation().getPolicyholderName())) {
                missingFields.add("Policyholder Name");
            }
        } else {
            missingFields.add("Policy Information");
        }
        
        // Validate Incident Information
        if (claimData.getIncidentInformation() != null) {
            if (isNullOrEmpty(claimData.getIncidentInformation().getDate())) {
                missingFields.add("Incident Date");
            }
            if (claimData.getIncidentInformation().getLocation() == null) {
                missingFields.add("Incident Location");
            }
            if (isNullOrEmpty(claimData.getIncidentInformation().getDescription())) {
                missingFields.add("Incident Description");
            }
        } else {
            missingFields.add("Incident Information");
        }
        
        // Validate Asset Details
        if (claimData.getAssetDetails() != null) {
            if (isNullOrEmpty(claimData.getAssetDetails().getAssetType())) {
                missingFields.add("Asset Type");
            }
            if (isNullOrEmpty(claimData.getAssetDetails().getEstimatedDamage())) {
                missingFields.add("Estimated Damage");
            }
        } else {
            missingFields.add("Asset Details");
        }
        
        // Validate Other Mandatory Fields
        if (claimData.getOtherMandatoryFields() != null) {
            if (isNullOrEmpty(claimData.getOtherMandatoryFields().getClaimType())) {
                missingFields.add("Claim Type");
            }
            if (isNullOrEmpty(claimData.getOtherMandatoryFields().getInitialEstimate())) {
                missingFields.add("Initial Estimate");
            }
        } else {
            missingFields.add("Other Mandatory Fields");
        }
        
        log.info("Validation completed. Missing fields: {}", missingFields.size());
        return missingFields;
    }
    
    /**
     * Check for fraud indicators in description
     */
    public boolean hasFraudIndicators(ClaimData claimData) {
        if (claimData.getIncidentInformation() == null || 
            claimData.getIncidentInformation().getDescription() == null) {
            return false;
        }
        
        String description = claimData.getIncidentInformation().getDescription().toLowerCase();
        String[] fraudKeywords = {"fraud", "inconsistent", "staged", "suspicious", "fake"};
        
        for (String keyword : fraudKeywords) {
            if (description.contains(keyword)) {
                log.warn("Fraud indicator detected: {}", keyword);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if claim involves injury
     */
    public boolean isInjuryClaim(ClaimData claimData) {
        if (claimData.getOtherMandatoryFields() == null) {
            return false;
        }
        
        String claimType = claimData.getOtherMandatoryFields().getClaimType();
        if (claimType != null && claimType.equalsIgnoreCase("INJURY")) {
            return true;
        }
        
        // Also check description
        if (claimData.getIncidentInformation() != null && 
            claimData.getIncidentInformation().getDescription() != null) {
            String description = claimData.getIncidentInformation().getDescription().toLowerCase();
            return description.contains("injury") || description.contains("injured") || 
                   description.contains("hurt") || description.contains("medical");
        }
        
        return false;
    }
    
    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
