# maxs-logger-java

`maxs-logger-java` is a Java utility for generating MAXS log files, supporting notification logging, attribute
validation, and file output in XML format.

## Features

- Log messages with different severity levels (`INFO`, `WARNING`, `ERROR`, etc.)
- Validate required and non-zero attributes for components and parts
- Output notifications to `.maxs` XML files
- Integration with REXS model components
- Utility methods for quantity and numeric operations

## Requirements

- Java 17+
- Maven

## Setup

1. Clone the repository.
2. Build with Maven:
   ```
   mvn clean install
   ```

## Usage

Import and use the `MaxsLogger` class to log messages

## Testing

Run unit and integration tests with:

```
mvn test
```