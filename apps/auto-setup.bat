@echo off
echo ========================================
echo  Mehanayim Choir App - Auto Setup
echo ========================================
echo.

echo This script will help you get your Android app built quickly!
echo.

echo Step 1: Creating GitHub repository...
echo.
echo Please follow these steps:
echo.
echo 1. Go to https://github.com/new
echo 2. Repository name: mehanayim-choir-app
echo 3. Make it PUBLIC
echo 4. Don't check "Initialize with README"
echo 5. Click "Create repository"
echo.

echo Step 2: Copy the repository URL
echo After creating the repository, copy the HTTPS URL
echo (it will look like: https://github.com/YOUR_USERNAME/mehanayim-choir-app.git)
echo.

set /p REPO_URL="Paste the repository URL here: "

echo.
echo Step 3: Setting up Git and uploading files...
echo.

git init
git add .
git commit -m "Initial commit - Mehanayim Choir App"
git branch -M main
git remote add origin %REPO_URL%
git push -u origin main

echo.
echo ========================================
echo  SUCCESS! Your app is now building!
echo ========================================
echo.

echo Your repository is now live at:
echo %REPO_URL%
echo.

echo To get your APK:
echo 1. Go to your repository on GitHub
echo 2. Click "Actions" tab
echo 3. Wait for the build to complete (5-10 minutes)
echo 4. Download the APK from "Artifacts"
echo.

echo ========================================
echo  Alternative: Quick GitHub Setup
echo ========================================
echo.

echo If the above doesn't work, try this:
echo 1. Download GitHub Desktop from https://desktop.github.com
echo 2. Clone your repository
echo 3. Copy all files to the repository folder
echo 4. Commit and push
echo.

pause
