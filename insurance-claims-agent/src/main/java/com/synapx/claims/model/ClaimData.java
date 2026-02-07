package com.synapx.claims.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimData {
    
    // Policy Information
    private PolicyInformation policyInformation;
    
    // Incident Information
    private IncidentInformation incidentInformation;
    
    // Involved Parties
    private InvolvedParties involvedParties;
    
    // Asset Details
    private AssetDetails assetDetails;
    
    // Other Mandatory Fields
    private OtherMandatoryFields otherMandatoryFields;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PolicyInformation {
        private String policyNumber;
        private String policyholderName;
        private String effectiveDates;
        private String carrierNaicCode;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IncidentInformation {
        private String date;
        private String time;
        private Location location;
        private String description;
        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Location {
            private String street;
            private String city;
            private String state;
            private String zip;
            private String country;
            private String descriptionIfNotSpecific;
        }
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvolvedParties {
        private Party claimant;
        private List<Party> thirdParties;
        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Party {
            private String name;
            private String address;
            private ContactDetails contactDetails;
            private String relationToInsured;
            private String dateOfBirth;
            
            @Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class ContactDetails {
                private String primaryPhone;
                private String secondaryPhone;
                private String primaryEmail;
                private String secondaryEmail;
            }
        }
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssetDetails {
        private String assetType;
        private String assetId; // VIN for vehicles
        private VehicleDetails vehicleDetails;
        private String estimatedDamage;
        private String damageDescription;
        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class VehicleDetails {
            private String year;
            private String make;
            private String model;
            private String bodyType;
            private String plateNumber;
            private String state;
        }
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OtherMandatoryFields {
        private String claimType;
        private List<String> attachments;
        private String initialEstimate;
        private String reportNumber;
        private String policeOrFireDepartmentContacted;
    }
}
