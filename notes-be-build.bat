@echo off
echo Building Docker image...
docker build -t notes-be .
if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    exit /b %ERRORLEVEL%
)
echo Build completed. Saving Docker image...
docker save -o notes-be.tar notes-be
if %ERRORLEVEL% NEQ 0 (
    echo Save failed!
    exit /b %ERRORLEVEL%
)
echo Image saved as notes-be.tar