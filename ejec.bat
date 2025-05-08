@echo off
REM Rutas
set "JAVA_SRC=C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\DAW-tienda\tienda\WEB-INF\classes"
set "SERVLET_API=C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\servlet-api.jar"
set "TIENDA_FOLDER=C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\DAW-tienda\tienda"
set "DEST_DIR=%CD%"  REM Directorio actual

echo Compilando archivos .java en %JAVA_SRC%...
pushd "%JAVA_SRC%"
javac -cp "%SERVLET_API%" *.java
if errorlevel 1 (
    echo Error en la compilación. Revisa los errores anteriores.
    popd
    exit /b 1
)
popd

echo Copiando carpeta 'tienda' al directorio actual (%DEST_DIR%)...
xcopy /E /I /Y "%TIENDA_FOLDER%" "%DEST_DIR%\tienda"

echo.
echo ✅ Proceso completado con éxito.
pause
