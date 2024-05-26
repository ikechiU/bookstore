# BookStore Application
[![Java](https://img.shields.io/badge/Java-%230070C1%20)](https://www.java.com/en/) [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-%2334C759)](https://spring.io/projects/spring-boot) [![Maven](https://img.shields.io/badge/Maven-%23F28500)](https://maven.apache.org) [![H2 Database](https://img.shields.io/badge/H2%20Database-%2387CEEB)](https://www.h2database.com) [![Hibernate](https://img.shields.io/badge/Hibernate-%23964B00)](https://hibernate.org) [![Spring Data JPA](https://img.shields.io/badge/JPA-%23439038)](https://spring.io/projects/spring-data-jpa) [![Spring Security](https://img.shields.io/badge/Spring%20Security-%233792CB)](https://spring.io/projects/spring-security) [![Gson](https://img.shields.io/badge/Gson-%23FFC107)](https://github.com/google/gson) [![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-%23F7F7F7?labelColor=%233498DB)](https://springdoc.org) [![JWT](https://img.shields.io/badge/JWT-%2303A9F4)](https://en.wikipedia.org/wiki/JSON_Web_Token) [![Build](https://img.shields.io/badge/Passing-Build?label=Build)]()

Welcome to the BookStore application, a simple and comprehensive platform for book lovers and administrators to manage and explore books. This application features a robust authentication and authorization system to ensure a secure and personalized experience for every user.

## Features

### Public Users
- **View Books:** Public users can view all books with their synopsis.
- **Sign Up:** Create an account to access more features.

### Authenticated Users
- **Full Book Content:** After signing in, users can view the complete content of books.

### Admin Users
- **Edit Books:** Modify book details.
- **Manage Availability:** Update book availability status.
- **Delete Books:** Remove books from the system.

### Super Admin
- **User Management:** Create new users and view all users.

### Test Users

1. **Regular User**
    - **email:** `reader@bookstore.com`
    - **password:** `B00kSt0r3Df@Pwd`

2. **Admin User**
    - **email:** `admin@bookstore.com`
    - **password:** `B00kSt0r3Df@Pwd`

3. **Super Admin User**
    - **email:** `superadmin@bookstore.com`
    - **password:** `B00kSt0r3Df@Pwd`


## Getting Started

### Prerequisites
- Java 17 or higher
- Maven
- Spring Boot (3.1.1)
- H2 Database
- **[Swagger UI](http://localhost:4040/documentation/swagger-ui/index.html)**

### Installation

1. **Clone the repository:**
   - **HTTPS:**
     ```sh
     git clone https://github.com/ikechiU/bookstore.git
     ```
   - **SSH:**
     ```sh
     git clone git@github.com:ikechiU/bookstore.git
     ```

2. **Set up the database:**
   ```sh
   The application uses the H2 database for both runtime and testing, ensuring a lightweight and easy-to-set-up environment.

3. **Build the project:**
   ```sh
   mvn clean install 

4. **Run the application:**
   ```sh
   mvn spring-boot:run

5. **Run the tests:**
   ```sh
   mvn test

### Configuration
- **Profile:** The application is configured to use two active profiles 'bookstore' and 'dev'.
- **Database Configuration:** The application is configured to use the H2 database out-of-the-box. The default settings are specified in application-dev.properties and application-test.properties.

### Initial Data Loading
- **Permissions:** Permissions are preloaded at startup.
- **Default Users:** Default users are created when the application starts.
- **Dummy Books:** The system comes with dummy books uploaded during initial startup.

### API Documentation
- **Swagger Integration:** This application includes Swagger for easy API documentation and testing. Swagger provides an interactive interface where you can explore and interact with the application's endpoints.
- **Accessing Swagger UI** Once the application is running, you can access the Swagger UI at the following URL: **[http://localhost:4040/documentation/swagger-ui/index.html](http://localhost:4040/documentation/swagger-ui/index.html)**

### Contributing
- Contributions are welcome! Please fork the repository and create a pull request with your changes.

### Author
Ikechi Ucheagwu 