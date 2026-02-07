# Setup and Deployment Guide

## Quick Setup (5 minutes)

### Step 1: Install Prerequisites

1. **Install Java 17**
   ```bash
   # Check if Java is installed
   java -version
   
   # If not installed, download from:
   # https://www.oracle.com/java/technologies/downloads/#java17
   ```

2. **Install Maven**
   ```bash
   # Check if Maven is installed
   mvn -version
   
   # If not installed, download from:
   # https://maven.apache.org/download.cgi
   ```

3. **Install Git**
   ```bash
   # Check if Git is installed
   git --version
   
   # If not installed, download from:
   # https://git-scm.com/downloads
   ```

### Step 2: Clone and Build

```bash
# Clone the repository
git clone https://github.com/yourusername/insurance-claims-agent.git
cd insurance-claims-agent

# Build the project
mvn clean install

# This will:
# - Download all dependencies
# - Compile the code
# - Run tests
# - Package the application
```

### Step 3: Run the Application

```bash
# Option 1: Using Maven
mvn spring-boot:run

# Option 2: Using JAR file
java -jar target/insurance-claims-agent-1.0.0.jar
```

### Step 4: Test the Application

1. **Open your browser and go to:**
   ```
   http://localhost:8080/swagger-ui.html
   ```

2. **Test the health endpoint:**
   ```bash
   curl http://localhost:8080/api/v1/claims/health
   ```

3. **Upload a test PDF:**
   ```bash
   curl -X POST http://localhost:8080/api/v1/claims/process \
     -F "file=@path/to/your/claim.pdf"
   ```

## GitHub Setup

### Step 1: Create GitHub Repository

1. Go to https://github.com and login
2. Click "New repository"
3. Name it: `insurance-claims-agent`
4. Description: "Autonomous Insurance Claims Processing Agent"
5. Choose "Public" or "Private"
6. Don't initialize with README (we already have one)
7. Click "Create repository"

### Step 2: Push Code to GitHub

```bash
# Navigate to your project directory
cd insurance-claims-agent

# Initialize git (if not already done)
git init

# Add all files
git add .

# Commit
git commit -m "Initial commit: Insurance Claims Processing Agent"

# Add remote repository (replace with your GitHub URL)
git remote add origin https://github.com/yourusername/insurance-claims-agent.git

# Push to GitHub
git push -u origin main
```

If you get an error about 'master' vs 'main', use:
```bash
git branch -M main
git push -u origin main
```

### Step 3: Verify on GitHub

1. Go to your repository URL
2. Refresh the page
3. You should see all your files!

## Development Workflow

### Making Changes

```bash
# 1. Create a new branch
git checkout -b feature/your-feature-name

# 2. Make your changes

# 3. Test your changes
mvn test

# 4. Commit your changes
git add .
git commit -m "Description of your changes"

# 5. Push to GitHub
git push origin feature/your-feature-name

# 6. Create a Pull Request on GitHub
```

### Keeping Your Fork Updated

```bash
# Add upstream repository
git remote add upstream https://github.com/original-owner/insurance-claims-agent.git

# Fetch updates
git fetch upstream

# Merge updates
git merge upstream/main
```

## Testing with Sample Documents

### Create a Test PDF

You can use the provided ACORD form PDF in the `uploads` folder, or create test claims using online PDF editors.

### Sample cURL Commands

```bash
# Process a claim
curl -X POST http://localhost:8080/api/v1/claims/process \
  -F "file=@/path/to/ACORD-Automobile-Loss-Notice.pdf" \
  -H "Content-Type: multipart/form-data"

# Health check
curl http://localhost:8080/api/v1/claims/health
```

### Expected Response Structure

```json
{
  "extractedFields": {
    "policyInformation": {...},
    "incidentInformation": {...},
    "assetDetails": {...}
  },
  "missingFields": ["Policy Number", "Incident Date"],
  "recommendedRoute": "MANUAL_REVIEW",
  "reasoning": "Missing mandatory fields: Policy Number, Incident Date",
  "metadata": {
    "processingTimestamp": "2024-01-20T10:30:45",
    "confidenceScore": 75
  }
}
```

## Deployment Options

### Option 1: Local Development

Already covered above - runs on localhost:8080

### Option 2: Docker (Optional)

Create `Dockerfile`:
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/insurance-claims-agent-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run:
```bash
docker build -t insurance-claims-agent .
docker run -p 8080:8080 insurance-claims-agent
```

### Option 3: Cloud Deployment

#### Heroku
```bash
# Install Heroku CLI
heroku login
heroku create your-app-name
git push heroku main
```

#### AWS Elastic Beanstalk
```bash
# Install EB CLI
eb init
eb create insurance-claims-env
eb deploy
```

## Troubleshooting

### Issue: Port 8080 already in use
```bash
# Change port in application.properties
server.port=8081

# Or kill the process using port 8080
# On Windows:
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# On Mac/Linux:
lsof -ti:8080 | xargs kill -9
```

### Issue: Maven build fails
```bash
# Clean and rebuild
mvn clean install -U

# Skip tests if needed
mvn clean install -DskipTests
```

### Issue: Java version mismatch
```bash
# Check Java version
java -version

# Set JAVA_HOME
export JAVA_HOME=/path/to/java17
```

### Issue: Git push authentication fails
```bash
# Use Personal Access Token instead of password
# Generate token at: https://github.com/settings/tokens
# Use token as password when prompted
```

## Next Steps

1. **Enhance Field Extraction**: Add more regex patterns for different form types
2. **Add Database**: Store processed claims in PostgreSQL/MySQL
3. **Add Authentication**: Implement Spring Security
4. **Add ML Model**: Use AI for better field extraction
5. **Add Email Notifications**: Send notifications for routing decisions
6. **Add Dashboard**: Create a frontend for monitoring claims

## Support

For issues or questions:
1. Check the logs in `logs/` directory
2. Search existing GitHub issues
3. Create a new issue with:
   - Error message
   - Steps to reproduce
   - Environment details (OS, Java version, etc.)

---

**Happy Coding! 🚀**
