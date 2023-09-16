mvn clean package

REM Create the dist folder
rmdir /s /q dist
mkdir dist
mkdir dist\plugins

REM Copy the jar files
xcopy app\target\app-*-spring-boot.jar dist\app.jar /s /i
xcopy plugins\CounterMaskPlugin\target\CounterMaskPlugin-*-all.jar dist\plugins\CounterMaskPlugin.jar /s
xcopy plugins\DateMaskPlugin\target\DateMaskPlugin-*-all.jar dist\plugins\DateMaskPlugin.jar /s
xcopy plugins\NameMaskPlugin\target\NameMaskPlugin-*-all.jar dist\plugins\NameMaskPlugin.jar /s
xcopy plugins\ExtensionMaskPlugin\target\ExtensionMaskPlugin-*-all.jar dist\plugins\ExtensionMaskPlugin.jar /s

cd dist

java -jar app.jar

cd ..
