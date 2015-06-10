@echo off
rem 检查基本配置信息是否正确
set "CURRENT_DIR=%~dp0"
%~d0
cd %CURRENT_DIR%
cd ..
set "PROJECT_DIR=%cd%"
set "LOGFILE=%PROJECT_DIR%\build\build.log"
echo PROJECT Paht:%PROJECT_DIR% > %LOGFILE%
if exist "%PROJECT_DIR%\pom.xml" goto choice
if not exist "%PROJECT_DIR%\pom.xml" echo %PROJECT_DIR%\pom.xml is not exist! >> %LOGFILE%
goto end

rem 选择打包类型线上正式环境或线下测试环境
:choice
set /p PACKAGE_TYPE=choose package type (test or online)[default:develop]:
if "%PACKAGE_TYPE%" == "develop" set "PACKAGE_TYPE="
if "%PACKAGE_TYPE%" == ""  goto package
if "%PACKAGE_TYPE%" == "test"    goto package
if "%PACKAGE_TYPE%" == "online"    goto package

echo ERROR : please input exact value... >> %LOGFILE%
goto end

rem 打包
:package
echo --------package for %PACKAGE_TYPE%-------- >> %LOGFILE%
mvn clean install compiler:compile  package -D project.build.package^=%PACKAGE_TYPE%  >> %LOGFILE%

rem 程序结束
:end