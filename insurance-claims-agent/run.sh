#!/bin/bash

# Insurance Claims Processing Agent - Run Script

echo "======================================"
echo "Insurance Claims Processing Agent"
echo "======================================"
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null
then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

echo "✅ Java version check passed"

# Check if Maven is installed
if ! command -v mvn &> /dev/null
then
    echo "❌ Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
fi

echo "✅ Maven check passed"
echo ""

# Build the project
echo "🔨 Building the project..."
mvn clean install -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

echo "✅ Build successful!"
echo ""

# Run the application
echo "🚀 Starting the application..."
echo "   Access the application at: http://localhost:8080"
echo "   Swagger UI at: http://localhost:8080/swagger-ui.html"
echo ""
echo "Press Ctrl+C to stop the application"
echo ""

mvn spring-boot:run
