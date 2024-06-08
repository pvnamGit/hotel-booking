## MVP Hotel Booking
### Documentation:
**LINK:** [MVP Hotel Booking Documentation](https://nampv.notion.site/HRS-Group-MVP-Hotel-Booking-Nam-Phan-1fdc947369994cf0bd7d485fe78ee7da?pvs=4)
### Tech Stack:
- **Programming Language**: `Java 11`
- **Framework**: `Spring Boot (version 2.7.18), Spring Security (version 2.7.18)`
- **Database**: `PostgreSQL`
- **Architecture**: `Clean Architecture`

### Setup PostgreSQL:
#### 1. Install PostgreSQL:

Download and install PostgreSQL from the official website.
Follow the installation instructions provided for your operating system.
#### 2.Create Database:

Once PostgreSQL is installed, open pgAdmin or any other PostgreSQL client.
Create a new database named `mvp_hotel_booking` or any.
### Configure Application Properties:

Open application.properties file in your Spring Boot project.
Update the database connection properties to match your PostgreSQL configuration.
#### 1. Clone from GitHub:
To clone this project from GitHub, follow these steps:

_Clone Repository:_
```
git clone https://github.com/pvnamGit/hotel-booking
```
_Navigate to Project Directory:_
```
cd hotel-booking
```
#### 2. Environment Variables:
Before running the application, make sure to set up the required environment variables. Create a .env file in the root directory of the project and populate it with the following variables:
```
URL_PREFIX=api/v1
DB_USERNAME=
DB_PASSWORD=
DB_HOST=
DB_PORT=
DB_NAME=
JWT_SECRET=
JWT_EXPIRATION=604800000
RATE_LIMIT=1
RATE_TIMEOUT=1
TEST_DB_NAME=
TEST_DB_USER=
TEST_DB_PASSWORD=
```

#### 3. Importing Environment Variables into IDE:
To import the environment variables into your IDE:

1. Create a `.env` file in the root directory of your project.
2. Populate the `.env` file with the environment variables mentioned above.
3. Refer to your IDE documentation on how to import environment variables. In IntelliJ IDEA, for example, you can use the EnvFile plugin to load environment variables from a .env file.

> The `JWT_SECRET` must be an HMAC hash string of 256 bits; otherwise, the token generation will throw an error. I used this [website](https://www.devglan.com/online-tools/hmac-sha256-online?ref=blog.tericcabrel.com) to generate one.
### Build and Run:

Use Maven or your preferred build tool to build the project.
Run the application using the generated JAR file or by using the `mvn spring-boot:run` command.
Access the Application:

This will start the application, and you can access it at the specified URL prefix (e.g., http://localhost:8080/api/v1).

### Running Tests:
In `.env` file, add variables for profile test. For example:
```
TEST_DB_NAME=testdb
TEST_DB_USER=sa
TEST_DB_PASSWORD=password
```

To run the test cases for the application:

Open a terminal in the project directory.
Execute the following Maven command:

```mvn test```

This will run all the test cases in the project and display the results in the terminal.

### Postman Workspace:
Access to Postman's workspace to test APIs.

**Link:** [Hotel Booking Postman](https://www.postman.com/satellite-astronomer-77407967/workspace/hotel-booking/request/34897345-9e4b5226-71e0-4339-bddf-60aa65fb127c)