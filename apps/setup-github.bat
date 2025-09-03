@echo off
echo ========================================
echo  Mehanayim Choir App - GitHub Setup
echo ========================================
echo.

echo This script will help you upload your app to GitHub
echo so you can build the real Android APK automatically.
echo.

echo Step 1: Create a GitHub account
echo - Go to https://github.com
echo - Click "Sign up" and create an account
echo.

echo Step 2: Create a new repository
echo - Click "New repository" (green button)
echo - Name it: mehanayim-choir-app
echo - Make it PUBLIC
echo - Don't initialize with README
echo - Click "Create repository"
echo.

echo Step 3: Download GitHub Desktop
echo - Go to https://desktop.github.com
echo - Download and install GitHub Desktop
echo.

echo Step 4: Clone and upload your code
echo - Open GitHub Desktop
echo - Click "Clone a repository from the Internet"
echo - Enter your repository URL
echo - Choose a local folder
echo - Copy all your project files to that folder
echo - Commit and push to GitHub
echo.

echo Step 5: Build the APK
echo - Go to your repository on GitHub.com
echo - Click "Actions" tab
echo - Wait for the build to complete
echo - Download the APK from "Artifacts"
echo.

echo ========================================
echo  Alternative: Use Git Command Line
echo ========================================
echo.

echo If you prefer command line:
echo 1. Install Git from https://git-scm.com
echo 2. Open Command Prompt in this folder
echo 3. Run these commands:
echo.
echo    git init
echo    git add .
echo    git commit -m "Initial commit"
echo    git branch -M main
echo    git remote add origin YOUR_REPO_URL
echo    git push -u origin main
echo.

echo ========================================
echo  Need Help?
echo ========================================
echo.
echo Check BUILD_INSTRUCTIONS.md for detailed steps
echo or visit: https://docs.github.com/en/get-started
echo.

pause
