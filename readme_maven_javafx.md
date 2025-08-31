# Traffic Intersection Simulator

A JavaFX-based traffic intersection simulator. This guide will help you set up Maven, build, and run the project.

---

## Prerequisites

- Java 11 or higher installed and set in your `JAVA_HOME`.
- `wget` and `tar` available on your system.
- Internet connection to download Maven.

---

## Step 1: Install Apache Maven

Use the following script to download, install, and configure Maven:

```bash
#!/bin/bash

MAVEN_VERSION="3.9.11"
INSTALL_DIR="$HOME"
MAVEN_DIR="$INSTALL_DIR/apache-maven-$MAVEN_VERSION"

# Download and extract Maven
wget -nc https://dlcdn.apache.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz -P $INSTALL_DIR
tar -xzf $INSTALL_DIR/apache-maven-$MAVEN_VERSION-bin.tar.gz -C $INSTALL_DIR

# Set environment variables
echo "export M2_HOME=$MAVEN_DIR" >> ~/.zshrc
echo "export PATH=\$M2_HOME/bin:\$PATH" >> ~/.zshrc

# Reload shell
source ~/.zshrc

# Check installation
mvn -v
```

---

## Step 2: Clone the Project

```bash
git clone <your-repo-url>
cd jraffic
```

Replace `<your-repo-url>` with the actual repository URL.

---

## Step 3: Build and Run the Project

Compile, test, package, and run your project using Maven commands:

```bash
# Compile the project
mvn compile

# Run unit tests
mvn test

# Package the project into a JAR
mvn package

# Clean and rebuild
mvn clean package

# Run the JavaFX application (requires javafx-maven-plugin in pom.xml)
mvn clean javafx:run
```

---

## Notes

- Ensure `JAVA_HOME` is correctly set to a JDK (not JRE) that matches your JavaFX version.
- The project window is set to be non-resizable and centered using the JavaFX code in `App.java`.
- Use `mvn clean javafx:run` to run the application directly from Maven.

---

This README covers installation, building, testing, and running the project step by step.

