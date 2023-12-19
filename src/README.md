# Tenant Management
 
This project is a Tenant Management implemented in Java using the Spring Boot framework with MongoDB as the database. It provides RESTful APIs for managing user data, including CRUD operations and user authentication.
 
## Table of Contents
- [Project Structure](#project-structure)
- [Technologies Used](#technologies-used)
- [Setup and Configuration](#setup-and-configuration)
- [API Documentation](#api-documentation)
- [Security](#security)
- [Exception Handling](#exception-handling)
- [Sequence Generator Service](#sequence-generator-service)
- [Contributing](#contributing)
- [License](#license)
 
## Project Structure
The project is organized into several packages:
 
- `Management.TenantAdmin`: Main package containing the application class and configuration.
- `Management.TenantAdmin.Entity`: Package for entity classes representing the data model.
- `Management.TenantAdmin.Controller`: Package containing REST controllers for handling HTTP requests.
- `Management.TenantAdmin.Repository`: Package containing MongoDB repositories for data access.
- `Management.TenantAdmin.Config`: Package for application configuration classes.
- `Management.TenantAdmin.Exception`: Package for global exception handling.
- `Management.TenantAdmin.Service`: Package containing the main business logic and services.
- `Management.TenantAdmin.Generics`: Package for generic result classes.

## Technologies Used
- **Spring Boot:** A framework for building Java-based enterprise applications.
- **MongoDB:** A NoSQL database used for storing user data.
- **Spring Security:** For securing the application with role-based access control.
- **Lombok:** Library for reducing boilerplate code, used for generating getters, setters, and constructors.
- **Slf4j:** Simple Logging Facade for Java, used for logging.
 
## Setup and Configuration
1. Clone the repository: `git clone https://github.com/srishruthib/TenantAdmin
2. Open the project in your preferred IDE (e.g., IntelliJ, Eclipse).
3. Configure the MongoDB connection in `application.properties`.
4. Run the `TenantAdminApplication` class to start the Spring Boot application.
 
## API Documentation
The API documentation can be found in the [TenantController](src/main/java/Management/TenantAdmin/Controller/TenantController.java) class. It includes endpoints for retrieving, creating, updating, and deleting tenants.
 
## Security
The application uses Spring Security for authentication and authorization. Users are defined in-memory with roles (USER, ADMIN). APIs are secured with role-based access control.
 
## Exception Handling
Global exception handling is implemented using the `GlobalExceptionHandler` class in the `Management.TenantAdmin.Exception` package. It catches exceptions and returns appropriate HTTP responses.
 
## Sequence Generator Service
The `SequenceGeneratorService` is responsible for generating sequence numbers for user IDs. It uses MongoDB's `findAndModify` operation to atomically increment and retrieve the sequence.
 
## Contributing
Feel free to contribute to the project by opening issues or submitting pull requests. Follow the [CONTRIBUTING.md](CONTRIBUTING.md) guidelines.
 
## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

