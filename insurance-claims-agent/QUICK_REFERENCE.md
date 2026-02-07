# Quick Reference Card

## 🚀 Quick Start Commands

```bash
# Build and run (first time)
mvn clean install
mvn spring-boot:run

# Or use the run script
./run.sh        # Linux/Mac
run.bat         # Windows
```

## 🌐 URLs

- **Application**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs
- **Health Check**: http://localhost:8080/api/v1/claims/health

## 📡 API Endpoints

### Process Claim
```bash
POST /api/v1/claims/process
Content-Type: multipart/form-data
Body: file=<PDF_FILE>
```

### Health Check
```bash
GET /api/v1/claims/health
```

## 📝 Sample cURL Commands

```bash
# Health check
curl http://localhost:8080/api/v1/claims/health

# Process claim
curl -X POST http://localhost:8080/api/v1/claims/process \
  -F "file=@path/to/claim.pdf"
```

## 🔄 Routing Rules

| Condition | Route | Priority |
|-----------|-------|----------|
| Missing mandatory fields | MANUAL_REVIEW | 1 (Highest) |
| Fraud indicators | INVESTIGATION_FLAG | 2 |
| Injury claim | SPECIALIST_QUEUE | 3 |
| Damage < $25,000 | FAST_TRACK | 4 |
| Default | STANDARD_PROCESSING | 5 (Lowest) |

## 📋 Required Fields

### Policy Information
- ✅ Policy Number
- ✅ Policyholder Name
- ⚪ Effective Dates
- ⚪ Carrier NAIC Code

### Incident Information
- ✅ Date
- ⚪ Time
- ✅ Location
- ✅ Description

### Asset Details
- ✅ Asset Type
- ⚪ Asset ID (VIN)
- ✅ Estimated Damage

### Other
- ✅ Claim Type
- ✅ Initial Estimate

✅ = Mandatory | ⚪ = Optional

## 🐛 Common Issues

### Port already in use
```bash
# Change in application.properties
server.port=8081
```

### Build fails
```bash
mvn clean install -U
# or skip tests
mvn clean install -DskipTests
```

### Java version issues
```bash
java -version  # Should be 17+
```

## 🔧 Configuration Files

- `pom.xml` - Dependencies
- `application.properties` - App config
- `.gitignore` - Git exclusions

## 📦 Project Structure

```
src/main/java/com/synapx/claims/
├── controller/      # REST endpoints
├── service/         # Business logic
├── model/          # Data models
├── dto/            # Response objects
├── extractor/      # PDF & field extraction
├── validator/      # Validation logic
└── config/         # Configuration
```

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=ClaimProcessingControllerTest

# With coverage
mvn clean test jacoco:report
```

## 📤 Push to GitHub

```bash
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/USERNAME/insurance-claims-agent.git
git push -u origin main
```

## 📚 Documentation Files

- `README.md` - Main documentation
- `SETUP_GUIDE.md` - Detailed setup instructions
- `GITHUB_GUIDE.md` - GitHub push guide
- `QUICK_REFERENCE.md` - This file

## 💡 Tips

1. **Always pull before push**: `git pull origin main`
2. **Use branches for features**: `git checkout -b feature/name`
3. **Check logs for errors**: Look in console output
4. **Test with Swagger UI**: Interactive API testing
5. **Keep dependencies updated**: `mvn versions:display-dependency-updates`

## 🆘 Support

- Check logs in console
- Visit Swagger UI for API testing
- Review SETUP_GUIDE.md for detailed help
- Create GitHub issue for bugs

---

**Made with ❤️ using Spring Boot**
