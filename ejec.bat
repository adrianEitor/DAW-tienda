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

echo.
echo Empaquetando 'tienda' como WAR en %DEST_DIR%...

REM Crea tienda.war en DEST_DIR, tomando toda la estructura de TIENDA_FOLDER
jar -cvf "%DEST_DIR%\adrian_eitor_guillermo_arcos.war" -C "%TIENDA_FOLDER%" .

echo.
echo Proceso completado con éxito.
pause
