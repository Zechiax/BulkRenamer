call mvn clean package

REM Create the dist folder
rmdir dist /s /q
md dist\
md dist\plugins\

REM Copy the jar files
xcopy app\target\app-*-spring-boot.jar dist /s /i
xcopy plugins\CounterMaskPlugin\target\CounterMaskPlugin-*-all.jar dist\plugins /s
xcopy plugins\DateMaskPlugin\target\DateMaskPlugin-*-all.jar dist\plugins /s
xcopy plugins\NameMaskPlugin\target\NameMaskPlugin-*-all.jar dist\plugins /s
xcopy plugins\ExtensionMaskPlugin\target\ExtensionMaskPlugin-*-all.jar dist\plugins /s

cd dist

rename app-*.jar app.jar
call java -jar app.jar

cd ..
