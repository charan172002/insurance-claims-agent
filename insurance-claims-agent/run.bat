@echo off
REM Insurance Claims Processing Agent - Run Script for Windows

echo ======================================
echo Insurance Claims Processing Agent
echo ======================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Java is not installed. Please install Java 17 or higher.
    pause
    exit /b 1
)

echo Java version check passed
echo.

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Maven is not installed. Please install Maven 3.6 or higher.
    pause
    exit /b 1
)

echo Maven check passed
echo.

REM Build the project
echo Building the project...
call mvn clean install -DskipTests

if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo Build successful!
echo.

REM Run the application
echo Starting the application...
echo Access the application at: http://localhost:8080
echo Swagger UI at: http://localhost:8080/swagger-ui.html
echo.
echo Press Ctrl+C to stop the application
echo.

call mvn spring-boot:run

pause
