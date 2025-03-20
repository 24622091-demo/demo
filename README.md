# Customer Application

This is a simple Spring Boot application that provides a greeting API.

## Project Structure

```
customer
├── src
│   ├── main
│   │   ├── java
│   │   │   └── vn
│   │   │       └── com
│   │   │           └── msb
│   │   │               ├── CustomerApplication.java
│   │   │               ├── controller
│   │   │               │   └── GreetingController.java
│   │   │               ├── model
│   │   │               │   └── Greeting.java
│   │   │               └── service
│   │   │                   └── GreetingService.java
│   │   └── resources
│   │       ├── application.properties
│   │       ├── static
│   │       └── templates
│   └── test
│       └── java
│           └── vn
│               └── com
│                   └── msb
│                       └── CustomerApplicationTests.java
├── pom.xml
└── README.md
```

## Getting Started

### Prerequisites

- JDK 21
- Maven 3.6 or higher

### Building the Application

To build the application, navigate to the project directory and run:

```
mvn clean install
```

### Running the Application

To run the application, use the following command:

```
mvn spring-boot:run
```

### Accessing the API

Once the application is running, you can access the greeting API at:

```
http://localhost:8080/greeting
```

### Testing

To run the tests, execute:

```
mvn test
```

## License

This project is licensed under the MIT License.