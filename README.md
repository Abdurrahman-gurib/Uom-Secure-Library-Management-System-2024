# Library Management System

## Overview

The Library Management System is a Java-based application designed to manage student information, book inventory, and loan records for a library. The system includes functionalities for adding students, viewing available books, and recording book loans. Security features such as encryption, file permissions, and checksum verification are implemented to ensure data integrity and protection.

## Features

- **Student Management**: Add and store student names with encryption.
- **Book Management**: View available books from a CSV file.
- **Loan Management**: Record book loans with timestamps and encryption.
- **Security**: 
  - **Encryption**: Encrypts sensitive data such as student names and book names.
  - **File Permissions**: Restricts file access to prevent unauthorized modifications.
  - **Checksum Verification**: Ensures data integrity by checking file integrity.
  - **Error Logging**: Logs errors and informational messages for debugging.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- A Java IDE or build tool (e.g., Eclipse, IntelliJ IDEA, Maven)

## Setup

1. **Clone the Repository**

   ```bash
   git clone https://github.com/yourusername/LibraryManagementSystem.git
## Navigate to the Project Directory

```bash
cd LibraryManagementSystem

## Compile the Project
## Ensure you have the JDK installed and compile the Java files:

```bash
javac -d bin src/LibraryManagementSystem.java


```bash
java -cp bin LibraryManagementSystem





















   
