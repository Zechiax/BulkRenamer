#!/bin/bash

# Run the application
mvn clean package

# Create the dist folder
rm -rf dist
mkdir -p dist/plugins

# Copy the jar files
cp ./app/target/app-*-spring-boot.jar dist/app.jar
cp ./plugins/CounterMaskPlugin/target/CounterMaskPlugin-*-all.jar dist/plugins/CounterMaskPlugin.jar
cp ./plugins/DateMaskPlugin/target/DateMaskPlugin-*-all.jar dist/plugins/DateMaskPlugin.jar
cp ./plugins/NameMaskPlugin/target/NameMaskPlugin-*-all.jar dist/plugins/NameMaskPlugin.jar
cp ./plugins/ExtensionMaskPlugin/target/ExtensionMaskPlugin-*-all.jar dist/plugins/ExtensionMaskPlugin.jar

cd dist || exit

java -jar app.jar

cd - || exit


