package com.synapx.claims.extractor;

import com.synapx.claims.model.ClaimData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class FieldExtractor {

    /**
     * Extract claim data from raw text using pattern matching
     */
    public ClaimData extractFields(String text) {
        log.info("Extracting fields from text of length: {}", text.length());
        
        ClaimData claimData = ClaimData.builder()
                .policyInformation(extractPolicyInformation(text))
                .incidentInformation(extractIncidentInformation(text))
                .involvedParties(extractInvolvedParties(text))
                .assetDetails(extractAssetDetails(text))
                .otherMandatoryFields(extractOtherMandatoryFields(text))
                .build();
        
        log.info("Field extraction completed");
        return claimData;
    }
    
    private ClaimData.PolicyInformation extractPolicyInformation(String text) {
        return ClaimData.PolicyInformation.builder()
                .policyNumber(extractPattern(text, "POLICY NUMBER[:\\s]*(\\S+)", 1))
                .policyholderName(extractPattern(text, "NAME OF INSURED[^\\n]*\\n[^\\n]*\\n?\\s*([A-Za-z\\s,]+?)(?:\\s{2,}|\\n)", 1))
                .effectiveDates(extractPattern(text, "EFFECTIVE DATES?[:\\s]*([\\d/\\-]+(?:\\s*(?:to|-)\\s*[\\d/\\-]+)?)", 1))
                .carrierNaicCode(extractPattern(text, "CARRIER NAIC CODE[:\\s]*(\\S+)", 1))
                .build();
    }
    
    private ClaimData.IncidentInformation extractIncidentInformation(String text) {
        ClaimData.IncidentInformation.Location location = ClaimData.IncidentInformation.Location.builder()
                .street(extractPattern(text, "STREET[:\\s]*([^\\n]+?)(?:\\s{2,}|\\n)", 1))
                .city(extractPattern(text, "CITY[,\\s]*([A-Za-z\\s]+?)(?:,|\\s{2,})", 1))
                .state(extractPattern(text, "STATE[:\\s]*([A-Z]{2})", 1))
                .zip(extractPattern(text, "ZIP[:\\s]*(\\d{5}(?:-\\d{4})?)", 1))
                .country(extractPattern(text, "COUNTRY[:\\s]*([A-Za-z\\s]+?)(?:\\s{2,}|\\n)", 1))
                .descriptionIfNotSpecific(extractPattern(text, "DESCRIBE LOCATION[^\\n]*\\n([^\\n]+)", 1))
                .build();
        
        return ClaimData.IncidentInformation.builder()
                .date(extractPattern(text, "DATE OF LOSS[^\\d]*(\\d{1,2}[/\\-]\\d{1,2}[/\\-]\\d{2,4})", 1))
                .time(extractPattern(text, "TIME[:\\s]*(\\d{1,2}:\\d{2}\\s*(?:AM|PM)?)", 1))
                .location(location)
                .description(extractPattern(text, "DESCRIPTION OF ACCIDENT[^\\n]*\\n([^\\n]+(?:\\n[^\\n]+)?)", 1))
                .build();
    }
    
    private ClaimData.InvolvedParties extractInvolvedParties(String text) {
        ClaimData.InvolvedParties.Party.ContactDetails claimantContact = 
            ClaimData.InvolvedParties.Party.ContactDetails.builder()
                .primaryPhone(extractPattern(text, "PHONE.*?PRIMARY[^\\d]*(\\d{3}[\\-\\s]?\\d{3}[\\-\\s]?\\d{4})", 1))
                .primaryEmail(extractPattern(text, "PRIMARY E-MAIL[^\\n]*\\n?\\s*([\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,})", 1))
                .build();
        
        ClaimData.InvolvedParties.Party claimant = ClaimData.InvolvedParties.Party.builder()
                .name(extractPattern(text, "NAME OF CONTACT[^\\n]*\\n?\\s*([A-Za-z\\s,]+?)(?:\\s{2,}|\\n)", 1))
                .contactDetails(claimantContact)
                .build();
        
        return ClaimData.InvolvedParties.builder()
                .claimant(claimant)
                .thirdParties(new ArrayList<>())
                .build();
    }
    
    private ClaimData.AssetDetails extractAssetDetails(String text) {
        ClaimData.AssetDetails.VehicleDetails vehicleDetails = 
            ClaimData.AssetDetails.VehicleDetails.builder()
                .year(extractPattern(text, "YEAR[:\\s]*(\\d{4})", 1))
                .make(extractPattern(text, "MAKE[:\\s]*([A-Za-z\\s]+?)(?:\\s{2,}|VEH|\\n)", 1))
                .model(extractPattern(text, "MODEL[:\\s]*([A-Za-z0-9\\s]+?)(?:\\s{2,}|BODY|\\n)", 1))
                .bodyType(extractPattern(text, "BODY[:\\s]*([A-Za-z\\s]+?)(?:\\s{2,}|TYPE|\\n)", 1))
                .plateNumber(extractPattern(text, "PLATE NUMBER[:\\s]*(\\S+)", 1))
                .state(extractPattern(text, "PLATE NUMBER[^\\n]*STATE[:\\s]*([A-Z]{2})", 1))
                .build();
        
        String vin = extractPattern(text, "V\\.?I\\.?N\\.?[:\\s]*(\\w{17})", 1);
        
        return ClaimData.AssetDetails.builder()
                .assetType("VEHICLE")
                .assetId(vin)
                .vehicleDetails(vehicleDetails)
                .damageDescription(extractPattern(text, "DESCRIBE DAMAGE[^\\n]*\\n([^\\n]+(?:\\n[^\\n]+)?)", 1))
                .estimatedDamage(extractPattern(text, "ESTIMATE AMOUNT[:\\s]*\\$?([\\d,]+(?:\\.\\d{2})?)", 1))
                .build();
    }
    
    private ClaimData.OtherMandatoryFields extractOtherMandatoryFields(String text) {
        String claimType = determineClaimType(text);
        
        return ClaimData.OtherMandatoryFields.builder()
                .claimType(claimType)
                .initialEstimate(extractPattern(text, "ESTIMATE AMOUNT[:\\s]*\\$?([\\d,]+(?:\\.\\d{2})?)", 1))
                .reportNumber(extractPattern(text, "REPORT NUMBER[:\\s]*(\\S+)", 1))
                .policeOrFireDepartmentContacted(extractPattern(text, "POLICE OR FIRE DEPARTMENT CONTACTED[:\\s]*([YN])", 1))
                .attachments(new ArrayList<>())
                .build();
    }
    
    private String determineClaimType(String text) {
        String textLower = text.toLowerCase();
        
        if (textLower.contains("injury") || textLower.contains("injured")) {
            return "INJURY";
        } else if (textLower.contains("automobile") || textLower.contains("vehicle") || textLower.contains("collision")) {
            return "AUTOMOBILE";
        } else if (textLower.contains("property")) {
            return "PROPERTY";
        }
        
        return "GENERAL";
    }
    
    /**
     * Extract text using regex pattern
     */
    private String extractPattern(String text, String regex, int group) {
        try {
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(text);
            
            if (matcher.find()) {
                String result = matcher.group(group);
                return result != null ? result.trim() : null;
            }
        } catch (Exception e) {
            log.debug("Pattern not found: {}", regex);
        }
        
        return null;
    }
}
