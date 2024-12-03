# **RideBuddy**

<img src="https://github.com/IdrisLaabidi/RideBuddy/blob/master/src/main/resources/static/photos/ridebuddy.png" alt="RideBuddy Logo" width="180" height="180">
*A user-friendly carpooling platform to connect passengers and drivers for seamless and cost-effective rides.*

## **Features**

- **User Authentication**: Register and log in securely using Spring Security.
- **Role-Based Access**:
  - **Passenger**:
    - Find nearby rides based on location.
    - Reserve available seats.
    - Rate drivers after completing rides.
  - **Conductor**:
    - Create, manage, and delete rides.
    - Visualize routes for rides.
- **Dynamic Maps Integration**:
  - View routes between departure and destination using Leaflet.js.
  - Location-based ride sorting.
- **Rating and Feedback**:
  - Provide ratings and reviews for conductors after rides.

## **Technologies Used**

### **Backend**
- **Java Spring Boot**:
  - Spring MVC for controllers.
  - Spring Data JPA for database interactions.
  - Spring Security for authentication and authorization.
- **Hibernate** for ORM.
- **REST APIs** for data interactions.
- **MySQL** for database storage.

### **Frontend**
- **Thymeleaf** for server-side rendering.
- **Bootstrap 5** for responsive UI design.
- **Leaflet.js** for map integration and route visualization.

### **Other Tools**
- **Maven** for dependency management.
- **Git** for version control.

---

## **Setup and Installation**

### **Prerequisites**
- Java 17+
- Maven 3+
- MySQL 8+
- A web browser
- An API key from [OpenRouteService](https://openrouteservice.org/) for map route integration.

### **Steps to Run**
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/ridebuddy.git
   cd ridebuddy
   ```

2. Create a database in MySQL:
   ```sql
   CREATE DATABASE ridebuddy;
   ```

3. Configure database credentials in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ridebuddy
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password
   ```

4. Add your API key for OpenRouteService:
   ```properties
   openrouteservice.api.key=your_api_key
   ```

5. Build and run the project:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

6. Access the application in your browser at `http://localhost:8080`.

---

## **Project Structure**

```
src
├── main
│   ├── java/com/fst/ridebuddy
│   │   ├── controllers       # Controllers for handling HTTP requests
│   │   ├── entities          # JPA Entities for database modeling
│   │   ├── models            # DTOs and request models
│   │   ├── repositories      # Interfaces for database operations
│   │   ├── services          # Business logic services
│   │   └── utils             # Utility classes (e.g., security configuration)
│   ├── resources
│   │   ├── static            # Static resources (CSS, JS, images)
│   │   ├── templates         # Thymeleaf templates for views
│   │   └── application.properties  # Application configuration
└── test                     # Test classes
```

---

## **Screenshots**

### **Home Page**
![Home Page](https://via.placeholder.com/800x400)

### **Nearby Rides**
![Nearby Rides](https://via.placeholder.com/800x400)

### **Ride Details**
![Ride Details](https://via.placeholder.com/800x400)

---

## **Future Enhancements**
- Real-time notifications for ride updates.
- Integration with payment gateways for seamless transactions.
- Improved map features (e.g., real-time traffic).

---
