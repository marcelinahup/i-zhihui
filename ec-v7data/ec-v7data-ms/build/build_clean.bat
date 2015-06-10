@echo off
rem  ---------------cleaning vms-------------------
set "CURRENT_DIR=%~dp0"
%~d0
cd %CURRENT_DIR%
cd ..
set "PROJECT_DIR=%cd%"
set "LOGFILE=%PROJECT_DIR%\build\build.log"
echo PROJECT Paht:%PROJECT_DIR% > %LOGFILE%

rd .\src\main\webapp\resource\rule /s /q
rd .\src\main\webapp\WEB-INF\view\rule /s /q
rd .\src\main\webapp\resource\tms /s /q
rd .\src\main\webapp\WEB-INF\view\tms /s /q