# Insurance Claims Processing Agent - Project Overview

## 📦 What's Included

This is a **complete, production-ready Spring Boot application** for processing insurance claims. Everything you need is included!

### ✅ Complete Features

1. **PDF Processing** - Extract text from FNOL documents using Apache PDFBox
2. **Field Extraction** - Pattern matching to extract structured data
3. **Data Validation** - Identify missing mandatory fields
4. **Intelligent Routing** - Route claims based on business rules
5. **REST API** - Clean endpoints for claim processing
6. **Swagger Documentation** - Interactive API docs
7. **Comprehensive Tests** - JUnit test cases included
8. **Complete Documentation** - Multiple guides for different needs

### 📂 Project Contents

```
insurance-claims-agent/
├── src/
│   ├── main/java/com/synapx/claims/
│   │   ├── InsuranceClaimsAgentApplication.java  # Main Spring Boot app
│   │   ├── config/
│   │   │   ├── JacksonConfig.java                # JSON configuration
│   │   │   └── OpenApiConfig.java                # Swagger setup
│   │   ├── controller/
│   │   │   └── ClaimProcessingController.java    # REST endpoints
│   │   ├── dto/
│   │   │   └── ClaimProcessingResponse.java      # Response DTO
│   │   ├── extractor/
│   │   │   ├── FieldExtractor.java               # Extract fields from text
│   │   │   └── PdfExtractor.java                 # Extract text from PDF
│   │   ├── model/
│   │   │   └── ClaimData.java                    # Domain model
│   │   ├── service/
│   │   │   ├── ClaimProcessingService.java       # Main business logic
│   │   │   └── ClaimRoutingService.java          # Routing logic
│   │   └── validator/
│   │       └── ClaimValidator.java               # Validation logic
│   ├── main/resources/
│   │   └── application.properties                # App configuration
│   └── test/java/com/synapx/claims/
│       └── ClaimProcessingControllerTest.java    # Integration tests
├── pom.xml                                       # Maven dependencies
├── README.md                                     # Main documentation
├── SETUP_GUIDE.md                               # Detailed setup guide
├── GITHUB_GUIDE.md                              # How to push to GitHub
├── QUICK_REFERENCE.md                           # Quick command reference
├── LICENSE                                       # MIT License
├── .gitignore                                   # Git exclusions
├── run.sh                                       # Run script (Unix/Mac)
├── run.bat                                      # Run script (Windows)
├── postman_collection.json                      # Postman API collection
└── example_response.json                        # Sample API response
```

## 🎯 Key Technologies

- **Spring Boot 3.2.1** - Modern Java framework
- **Java 17** - Latest LTS Java version
- **Apache PDFBox 3.0.1** - PDF text extraction
- **Maven** - Build and dependency management
- **Lombok** - Reduce boilerplate code
- **SpringDoc OpenAPI** - API documentation
- **JUnit 5** - Testing framework

## 🚀 Getting Started (3 Steps)

### Step 1: Prerequisites
- Install Java 17+
- Install Maven 3.6+
- Install Git

### Step 2: Build & Run
```bash
cd insurance-claims-agent
mvn clean install
mvn spring-boot:run
```

### Step 3: Test
Open browser: http://localhost:8080/swagger-ui.html

## 📊 How It Works

```
1. Upload PDF → 2. Extract Text → 3. Parse Fields → 4. Validate → 5. Route Claim
     ↓               ↓                 ↓              ↓            ↓
   API Call    Apache PDFBox    Pattern Matching   Business     Decision
                                                    Rules        + Reason
```

## 🎨 Routing Logic

The system routes claims intelligently:

1. **Missing Fields?** → Manual Review
2. **Fraud Keywords?** → Investigation
3. **Injury Claim?** → Specialist Queue
4. **Low Damage (<$25k)?** → Fast Track
5. **Otherwise** → Standard Processing

## 📖 Documentation Guide

| File | Purpose | When to Read |
|------|---------|--------------|
| `README.md` | Overview and features | Start here |
| `SETUP_GUIDE.md` | Detailed installation | Setting up project |
| `GITHUB_GUIDE.md` | Push to GitHub | Uploading to GitHub |
| `QUICK_REFERENCE.md` | Commands & tips | Quick lookup |

## 🔄 Development Workflow

### First Time Setup
```bash
# Clone or download the project
cd insurance-claims-agent

# Build
mvn clean install

# Run
mvn spring-boot:run
```

### Making Changes
```bash
# Make your code changes

# Test
mvn test

# Run locally
mvn spring-boot:run

# Commit to Git
git add .
git commit -m "Your changes"
git push
```

## 🧪 Testing the API

### Using Swagger UI (Easiest)
1. Start the app
2. Go to: http://localhost:8080/swagger-ui.html
3. Click "Try it out" on any endpoint
4. Upload a PDF and click "Execute"

### Using cURL
```bash
curl -X POST http://localhost:8080/api/v1/claims/process \
  -F "file=@/path/to/claim.pdf"
```

### Using Postman
1. Import `postman_collection.json`
2. Select "Process Claim" request
3. Upload PDF file
4. Send request

## 📈 Expected Response

```json
{
  "extractedFields": {
    "policyInformation": {...},
    "incidentInformation": {...},
    "assetDetails": {...}
  },
  "missingFields": ["Policy Number"],
  "recommendedRoute": "MANUAL_REVIEW",
  "reasoning": "Missing mandatory fields: Policy Number",
  "metadata": {
    "processingTimestamp": "2024-02-07T10:30:45",
    "confidenceScore": 85
  }
}
```

## 🔧 Customization Points

### 1. Routing Rules
Edit: `src/main/java/com/synapx/claims/service/ClaimRoutingService.java`
- Change threshold amounts
- Add new routing conditions
- Modify priority logic

### 2. Field Extraction Patterns
Edit: `src/main/java/com/synapx/claims/extractor/FieldExtractor.java`
- Add new regex patterns
- Support different form types
- Extract additional fields

### 3. Validation Rules
Edit: `src/main/java/com/synapx/claims/validator/ClaimValidator.java`
- Add new validation checks
- Modify mandatory field list
- Add fraud detection keywords

### 4. Configuration
Edit: `src/main/resources/application.properties`
- Change server port
- Adjust logging levels
- Modify file upload limits

## 💾 Database Integration (Future Enhancement)

Current: In-memory processing
Future: Add PostgreSQL/MySQL

```xml
<!-- Add to pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
```

## 🔐 Security (Future Enhancement)

Add Spring Security:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

## 🌍 Deployment Options

### Local Development
Already configured - runs on localhost:8080

### Docker
```bash
docker build -t insurance-claims-agent .
docker run -p 8080:8080 insurance-claims-agent
```

### Cloud Platforms
- **AWS**: Elastic Beanstalk, ECS, Lambda
- **Azure**: App Service, Container Instances
- **GCP**: App Engine, Cloud Run
- **Heroku**: Direct Git deployment

## 📚 Learning Resources

- **Spring Boot**: https://spring.io/guides
- **Maven**: https://maven.apache.org/guides/
- **PDFBox**: https://pdfbox.apache.org/
- **REST API Best Practices**: https://restfulapi.net/

## 🤝 Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature/name`
3. Make changes and test
4. Commit: `git commit -m "Add feature"`
5. Push: `git push origin feature/name`
6. Create Pull Request

## 📞 Support

Need help? Check these in order:

1. **QUICK_REFERENCE.md** - Common commands
2. **SETUP_GUIDE.md** - Detailed setup help
3. **Application logs** - Check console output
4. **Swagger UI** - Test endpoints interactively
5. **GitHub Issues** - Create an issue

## ✨ Next Steps

1. ✅ **Setup**: Follow SETUP_GUIDE.md
2. ✅ **Run**: Start the application
3. ✅ **Test**: Try Swagger UI
4. ✅ **Push**: Upload to GitHub (see GITHUB_GUIDE.md)
5. ✅ **Customize**: Modify for your needs
6. ✅ **Deploy**: Choose deployment platform

## 🎓 Assessment Compliance

This project fulfills all requirements from the assessment brief:

✅ Extracts key fields from FNOL documents
✅ Identifies missing/inconsistent fields  
✅ Classifies claims and routes to correct workflow
✅ Provides explanation for routing decisions
✅ JSON output format as specified
✅ Uses modern frameworks (Spring Boot, Maven)
✅ Includes README with approach and run steps
✅ Complete GitHub repository structure

## 📄 License

MIT License - See LICENSE file for details

---

**You now have everything you need to run, modify, and deploy this insurance claims processing system! 🚀**

For questions, refer to the documentation files or create a GitHub issue.

**Happy Coding!** 💻
