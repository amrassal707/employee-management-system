# Employee Management System

A comprehensive Spring Boot application for managing employees and departments with security, scheduled tasks, and detailed reporting.

## Technologies Used

- Java 21
- Spring Boot 3.5.3
- Spring Security
- Spring Data JPA
- H2 Database
- Lombok
- SpringDoc OpenAPI (Swagger)
- JUnit 5 & Mockito
- BCrypt Password Encoding

## Features

- CRUD operations for both employees and departments
- Role-based access control (USER and ADMIN roles)
- Automated daily department summaries
- Data validation and error handling
- API documentation with Swagger
- In-memory H2 database with initial test data
- Comprehensive test coverage
- Optimistic locking for concurrent modifications

## Entity Relationship Diagram

```
+---------------+       +---------------+       +---------------+
|    Employee   |       | Department   |       | DailySummary |
+---------------+       +---------------+       +---------------+
| id (PK)      |       | id (PK)      |       | id (PK)      |
| firstName     |       | name (U)     |       | timestamp    |
| lastName      |   N   |              |   1   | employeeCount|
| email (U)     +-------+              +-------+ departmentId |
| dateOfBirth   |       | employees[]  |       |             |
| hireDate      |       |             |       |             |
| phoneNumber   |       |             |       |             |
| salary        |       |             |       |             |
| position      |       |             |       |             |
| departmentId  |       |             |       |             |
| version      |       |             |       |             |
+---------------+       +---------------+       +---------------+

+---------------+
|     User      |
+---------------+
| id (PK)      |
| username (U)  |
| password     |
| role         |
+---------------+
```

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on port 8080.

### Initial Test Data

On startup, the system automatically creates:

1. Users:
   - Admin user: username=`admin`, password=`admin123`
   - Regular user: username=`user`, password=`user123`

2. Departments:
   - IT
   - HR
   - Finance
   - Operations
   - Marketing

## API Documentation

### Employee Endpoints (Protected by Security)

- GET /api/employees - Get all employees (paginated) [USER, ADMIN]
- GET /api/employees/{id} - Get employee by ID [USER, ADMIN]
- POST /api/employees - Create a new employee [ADMIN]
- PUT /api/employees/{id} - Update an employee [ADMIN]
- DELETE /api/employees/{id} - Delete an employee [ADMIN]

### Department Endpoints (Protected by Security)

- GET /api/departments - Get all departments [USER, ADMIN]
- GET /api/departments/{id} - Get department by ID [USER, ADMIN]
- POST /api/departments - Create a new department [ADMIN]
- PUT /api/departments/{id} - Update a department [ADMIN]
- DELETE /api/departments/{id} - Delete a department [ADMIN]

### Accessing the APIs

1. Swagger UI: http://localhost:8080/swagger-ui.html
2. API Documentation: http://localhost:8080/api-docs

## Security Implementation

- Role-based access control using Spring Security
- Basic authentication
- BCrypt password encoding
- Protected endpoints based on user roles
- Open access to Swagger UI and H2 Console

## Test Coverage

### Unit Tests

1. EmployeeServiceTest:
   - Employee creation with validation
   - Email uniqueness check
   - Department association
   - Update operations
   - Delete operations with validation

2. DepartmentServiceTest:
   - Department creation
   - Name uniqueness validation
   - Update operations
   - Delete with employee check
   - Department retrieval

3. DepartmentSummaryService Test:
   - Daily summary generation
   - Employee count calculation
   - Timestamp recording

### Integration Tests

Covers the interaction between:
- Employee and Department entities
- Security configuration
- Scheduled tasks

## Scheduled Tasks

Daily Department Summary:
- Runs every day at 9:00 AM
- Logs employee count per department
- Stores historical data in daily_summaries table
- Accessible through logs and database

## Error Handling

Global exception handler covers:
- Entity not found exceptions
- Data integrity violations
- Validation errors
- Security access violations

## Data Validation

1. Employee Validation:
   - Required fields: firstName, lastName, email, dateOfBirth, hireDate
   - Email format and uniqueness
   - Valid phone number format
   - Positive salary value
   - Past date of birth

2. Department Validation:
   - Required name field
   - Unique department names
   - No deletion if employees exist


## Running Tests

Run all tests:
```bash
mvn test
```

Run specific test class:
```bash
mvn test -Dtest=EmployeeServiceTest
```

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## Scheduled Tasks (Cron Job)

- A scheduled task runs every day at 9:00 AM.
- It logs the total number of employees in each department to the application logs.
- It also stores a record in the `daily_summaries` table for each department, including the department, employee count, and timestamp.
- This enables historical tracking and analysis of department sizes over time.

### Example Log Output
```
2025-07-06 09:00:00 INFO  Starting daily department summary generation
2025-07-06 09:00:00 INFO  Department: IT - Total Employees: 12
2025-07-06 09:00:00 INFO  Department: HR - Total Employees: 5
2025-07-06 09:00:00 INFO  Completed daily department summary generation
```

### `daily_summaries` Table Structure
- `id`: Primary key
- `department_id`: Foreign key to Department
- `employee_count`: Number of employees in the department at the time of summary
- `timestamp`: When the summary was recorded
