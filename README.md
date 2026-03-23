# Library Management System

This project is a backend-driven Library Management System developed using Spring Boot. It is designed to manage books, users, and memberships within a library, with role-based access for administrators and librarians.

## Features

### Admin

* Verify student accounts
* Manage user roles
* View system data and activity

### Librarian

* Add, update, and remove books
* Manage memberships
* Issue memberships to students
* Send notifications
* Verify students

## Tech Stack

* Backend: Spring Boot
* Frontend: HTML, CSS, JavaScript *(or React if applicable)*
* Database: MySQL
* Build Tool: Maven

## Project Structure

```
Library-Management-System/
│── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│── pom.xml
│── .gitignore
│── README.md
```

## Setup Instructions

1. Clone the repository:

```
git clone https://github.com/your-username/Library-Management-System.git
```

2. Configure database settings in `application.properties`.

3. Run the application:

```
mvn spring-boot:run
```


## Future Scope

* Improved user interface
* Email notification system
* Deployment support

## Author

Ankush Gulwani
