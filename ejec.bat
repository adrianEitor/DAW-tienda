@echo off
REM Rutas
set "JAVA_SRC=C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\DAW-tienda\tienda\WEB-INF\classes"
set "MODELO_SRC=%JAVA_SRC%\modelo"
set "BEAN_SRC=%MODELO_SRC%\beans"
set "BD_SRC=%MODELO_SRC%\BD"
set "LIB=C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\servlet-api.jar;C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\postgresql-42.7.5.jar"
set "TIENDA_FOLDER=C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\DAW-tienda\tienda"
set "DEST_DIR=C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps"  REM Directorio actual

echo Compilando archivos .java...

REM Comillas adicionales alrededor de cada ruta para asegurar que las rutas con espacios se manejen correctamente
javac -cp "%LIB%" "%JAVA_SRC%"\*.java

echo Copiando carpeta 'tienda' al directorio actual (%DEST_DIR%)...
xcopy /E /I /Y "%TIENDA_FOLDER%" "%DEST_DIR%\tienda"

echo.
echo Proceso completado con éxito.
pause
