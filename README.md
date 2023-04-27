# Unit and Integration Testing in Java Spring

This project is designed to demonstrate unit and integration testing in a Java Spring environment. It includes a simple banking application with endpoints for creating, retrieving, updating, and deleting bank accounts.

## Technologies Used
* Java
* Spring Boot
* JUnit 5
* Mockito
* H2 Database

## Getting Started

To run this project locally, you'll need to have Java and Maven installed. You can download them from the following links:

- [Java](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)

Once you've installed Java and Maven, follow these steps:

1. Clone this repository to your local machine.
2. Open a terminal or command prompt and navigate to the project directory.
3. Run the command `mvn clean install` to build the project.
4. Run the command `mvn spring-boot:run` to start the application.

## Running the Tests

This project includes unit tests and integration tests. To run the tests, navigate to the project directory in a terminal or command prompt and run the command `mvn test`.

## Endpoints

The following endpoints are available:

- `GET /bankAccounts`: Returns a list of all bank accounts.
- `GET /bankAccounts/{id}`: Returns the bank account with the specified ID.
- `POST /bankAccounts`: Creates a new bank account with the data in the request body.
- `PUT /bankAccounts/{id}`: Updates the bank account with the specified ID with the data in the request body.
- `DELETE /bankAccounts/{id}`: Deletes the bank account with the specified ID.

## Contributing

If you'd like to contribute to this project, feel free to fork the repository and submit a pull request. Before submitting a pull request, make sure that your changes are well-tested and that all tests pass.

## License

This project is licensed under the MIT License. See the `LICENSE` file for more information.
