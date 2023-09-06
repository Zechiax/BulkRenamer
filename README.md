# Bulk Renamer

An app for easy renaming of multiple files

## How to build and run

### Prerequisites

- Java 20
- Maven

## How to build

To instantly run the app, run at the root of the project:

```bash
mvn clean javafx:run
```

## How to use

The flow of the app is as follows:

1. After opening the app, use the `Add files` button to add files to the list
1. Select the files you want to rename and confirm, this will add them to the list of files
1. Write mask for your files
1. Click `Rename` to rename the files
1. If everything went well, you will see a message about the successful renaming

## Built-in masks

- \[N] - Name of the file
- \[N#-#] - Name of the file from # to #
- \[E] - Extension of the file
- \[E#-#] - Extension of the file from # to #
- \[D], \[M], \[R] - Current date, as day, month, year
- \[C] - Counter, settings is available in the interface, you can set the starting value, step and number of digits (padding)

## Plugin interface
