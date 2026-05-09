@echo off
setlocal enabledelayedexpansion

:: Запрос версии
set "default_version=1.0"
set /p "version=Enter version (%default_version%): "
if "%version%"=="" set "version=%default_version%"

:: Запрос директории установки
set "default_dir=%~dp0moodle-test-generator\%version%"
echo Enter installation directory.
set /p "directory=Default (%default_dir%): "
if "%directory%"=="" set "directory=%default_dir%"

set "app_url=https://github.com/Fenya2/moodle-test-generator/releases/download/%version%/MoodleTestGenerator-%version%.jar"
set "app_path=%directory%\MoodleTestGenerator-%version%.jar"
set "major_version=25"
set "jdk_version=%major_version%.0.2"
set "jdk_path=%directory%/jdk-%jdk_version%"
set "jdk_archived_path=%jdk_path%.zip"
set "javafx_path=%directory%/javafx-sdk-%jdk_version%"
set "javafx_archived_path=%javafx_path%.zip"

if not exist "%directory%" (
    echo Creating installation directory %directory%
    mkdir "%directory%"

    :: Скачивание приложения
    echo Downloading Moodle Test Generator %version% to %directory%
    curl -L "%app_url%" -o "%app_path%"

    if %errorlevel% equ 0 (
        echo Success
    ) else (
        echo Error: Failed to download app.
        pause
        exit
    )

    :: Скачивание jdk
    echo Downloading JDK %jdk_version% to %directory%
    curl -L "https://download.oracle.com/java/%major_version%/archive/jdk-%jdk_version%_windows-x64_bin.zip" -o "%jdk_archived_path%"

    if %errorlevel% equ 0 (
        echo.
        echo Success
    ) else (
        echo Error: Failed to download java.
        pause
        exit
    )

    :: Скачивание javafx
    echo Downloading Java FX %jdk_version% to %directory%
    curl -L "https://download.oracle.com/java/%major_version%/archive/javafx-%jdk_version%_windows-x64_bin-sdk.zip" -o "%javafx_archived_path%"
    if %errorlevel% equ 0 (
        echo Success
    ) else (
        echo Error: Failed to download java.
        pause
        exit
    )

    :: Распаковка java
    echo Extracting jdk
    tar -xf "%jdk_archived_path%" -C "%directory%"
    if %errorlevel% equ 0 (
        echo Success
    ) else (
        echo Error: Failed to extract java.
        pause
        exit
    )

    :: Распаковка Java FX
    echo Extracting Java FX
    tar -xf "%javafx_archived_path%" -C "%directory%"
    if %errorlevel% equ 0 (
        echo Success
    ) else (
        echo Error: Failed to extract Java FX.
        pause
        exit
    )
)

echo Starting Moodle Test Generator
%jdk_path%/bin/java.exe --module-path %javafx_path%/lib --add-modules javafx.controls,javafx.fxml,javafx.web -jar %app_path%

echo Moodle Test Generator stopped
pause