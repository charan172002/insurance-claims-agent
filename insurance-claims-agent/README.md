# Insurance Claims Processing Agent 🏥

An autonomous Spring Boot application that processes First Notice of Loss (FNOL) insurance documents, extracts key information, validates claims, and routes them to appropriate workflows.

## 🎯 Features

- **PDF Text Extraction**: Extracts text content from FNOL PDF documents using Apache PDFBox
- **Field Extraction**: Uses regex pattern matching to extract structured data from unstructured text
- **Data Validation**: Identifies missing or inconsistent mandatory fields
- **Intelligent Routing**: Routes claims based on configurable business rules
- **RESTful API**: Clean REST endpoints for claim processing
- **Swagger Documentation**: Interactive API documentation at `/swagger-ui.html`
- **Comprehensive Logging**: Detailed logging for troubleshooting and audit trails

## 📋 Routing Rules

The system routes claims based on the following business logic:

1. **Manual Review** - If any mandatory field is missing
2. **Investigation Flag** - If description contains fraud indicators ("fraud", "inconsistent", "staged")
3. **Specialist Queue** - If claim type is "INJURY"
4. **Fast Track** - If estimated damage < $25,000
5. **Standard Processing** - Default route for all other valid claims

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.2.1**
- **Apache PDFBox 3.0.1** - PDF text extraction
- **Lombok** - Reduce boilerplate code
- **SpringDoc OpenAPI** - API documentation
- **Maven** - Build and dependency management
- **JUnit 5** - Testing framework

## 📁 Project Structure

```
insurance-claims-agent/
├── src/
│   ├── main/
│   │   ├── java/com/synapx/claims/
│   │   │   ├── config/           # Configuration classes
│   │   │   ├── controller/       # REST controllers
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── extractor/        # PDF and field extraction
│   │   │   ├── model/            # Domain models
│   │   │   ├── service/          # Business logic
│   │   │   ├── validator/        # Validation logic
│   │   │   └── InsuranceClaimsAgentApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/synapx/claims/
│           └── ClaimProcessingControllerTest.java
├── pom.xml
└── README.md
```

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Git

### Installation & Running

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/insurance-claims-agent.git
cd insurance-claims-agent
```

2. **Build the project**
```bash
mvn clean install
```

3. **Run the application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Alternative: Run with JAR

```bash
mvn clean package
java -jar target/insurance-claims-agent-1.0.0.jar
```

## 📖 API Documentation

Once the application is running, access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

### Main Endpoints

#### Process Claim
```http
POST /api/v1/claims/process
Content-Type: multipart/form-data

file: <PDF file>
```

**Response:**
```json
{
  "extractedFields": {
    "policyInformation": {
      "policyNumber": "AUTO123456",
      "policyholderName": "John Doe"
    },
    "incidentInformation": {
      "date": "01/15/2024",
      "time": "2:30 PM",
      "description": "Vehicle collision at intersection"
    },
    "assetDetails": {
      "assetType": "VEHICLE",
      "estimatedDamage": "5000"
    }
  },
  "missingFields": [],
  "recommendedRoute": "FAST_TRACK",
  "reasoning": "Estimated damage ($5000.00) is below fast-track threshold ($25000.00)",
  "metadata": {
    "processingTimestamp": "2024-01-20T10:30:45",
    "documentType": "ACORD FNOL",
    "confidenceScore": 95,
    "warnings": []
  }
}
```

#### Health Check
```http
GET /api/v1/claims/health
```

**Response:**
```json
{
  "status": "UP",
  "message": "Claims Processing Service is running"
}
```

## 🧪 Testing

Run the test suite:
```bash
mvn test
```

Run with coverage:
```bash
mvn clean test jacoco:report
```

## 📊 Extracted Fields

The system extracts the following fields from FNOL documents:

### Policy Information
- Policy Number
- Policyholder Name
- Effective Dates
- Carrier NAIC Code

### Incident Information
- Date
- Time
- Location (Street, City, State, ZIP, Country)
- Description

### Involved Parties
- Claimant Name and Contact Details
- Third Parties (if applicable)

### Asset Details
- Asset Type
- Asset ID (VIN for vehicles)
- Vehicle Details (Year, Make, Model, Plate Number)
- Damage Description
- Estimated Damage Amount

### Other Mandatory Fields
- Claim Type
- Initial Estimate
- Report Number
- Police/Fire Department Contact

## 🔧 Configuration

Edit `src/main/resources/application.properties` to customize:

```properties
# Server Port
server.port=8080

# File Upload Limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging Level
logging.level.com.synapx.claims=DEBUG
```

## 🐛 Troubleshooting

### Common Issues

1. **PDF parsing errors**: Ensure the PDF is text-based, not scanned images
2. **Field extraction issues**: The regex patterns are optimized for ACORD forms
3. **Port already in use**: Change the port in `application.properties`

### Logs

Check application logs for detailed error messages:
```bash
tail -f logs/insurance-claims-agent.log
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License.

## 👥 Authors

- **Your Name** - Initial work

## 🙏 Acknowledgments

- ACORD for insurance form standards
- Apache PDFBox for PDF processing capabilities
- Spring Boot team for the excellent framework

## 📧 Contact

For questions or support, please contact: support@synapx.com

---

**Note**: This is a demonstration project for insurance claim processing automation. In production, you would integrate with actual insurance systems and databases.
