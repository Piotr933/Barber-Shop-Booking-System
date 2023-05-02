# Barber-Shop-Booking-System

The "Barber Shop Booking System" is a REST API project that aims to streamline the booking process for customers of a barber shop. The system will allow customers to view the available time slots for a particular barber and book appointments accordingly. The API will also allow the barber shop staff to manage the bookings and schedule appointments for customers.


## Technologies

The "Barber Shop Booking System" is a REST API project developed using Java 17 and Spring Boot 3.0.2. It uses Gradle for dependency management and an H2 database for data storage. The project incorporates Spring Actuator for monitoring and management, Spring Data JPA for database interactions, and Spring Security for authentication and authorization. It also uses Spring Validation for data validation, Lombok for reducing boilerplate code, and JUnit and Mockito for unit testing. Overall, this project demonstrates a range of powerful and popular technologies for Java back-end development.

## Tools
* IntelliJ IDA
* Postman

## Installation
```bash
https://github.com/Piotr933/Barber-Shop-Booking-System.git
```
## Author
Piotr Zawada

## Version History

* 0.1.0  - 21.03.2023
* 0.1.1  - 02.05.2023 

## What's new in 0.1.1 Version:

   * When a user attempts to register with a username that already exists, the API will now return a response status to notify the user that the username is  already taken.

  *  The Admin role now has the ability to view all bookings made by users, providing greater visibility and control over the booking process.

   * Password restrictions have been implemented for new user registrations, requiring a minimum password length of 8 characters, at least one upper and lower case letter, at least one digit, and at least one special character.

   * When a user attempts to cancel a booking that has already been canceled, the API now returns a response status and message to inform the user that the booking cannot be canceled again.

   * A new Admin Controller has been created, and all Admin-related methods have been moved from the Booking Controller to the new Admin Controller. The request mappings have also been updated to reflect these changes, providing greater clarity and organization within the API.

## License
[MIT](https://choosealicense.com/licenses/mit/)





