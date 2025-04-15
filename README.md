# Tennis Tournament Management App 🎾

This is a full-stack web application designed to manage tennis tournaments, players, referees, matches, and administrative functionalities. The project is built using **Java (Spring Boot)** for the backend and **React.js** for the frontend, with a **MySQL** database.

## Features

### ✅ Common
- User registration and login
- Role-based access (Admin, Referee, Player)

### 🧑‍💼 Admin
- View and manage all users
- Export match data (CSV, JSON, TXT)
- View all tournaments and matches

### 🧑‍⚖️ Referee
- View their own schedule (assigned matches)
- Update and manage match scores

### 🎾 Player
- Register for available tournaments
- View registered and unregistered tournaments
- View matches from both categories (even if not registered)
- See scores, opponents, and match dates

---

## Technologies Used

### Backend:
- Java 17
- Spring Boot
- Spring Security
- RESTful API
- JPA (Hibernate)
- MySQL

### Frontend:
- React.js
- CSS3 (custom styling)
- React Router

### Database:
- MySQL 8.x
- Workbench for schema management

---

## How to Run Locally

### 📦 Backend
1. Clone the repo
2. Open in IntelliJ IDEA
3. Set up your database connection in `application.properties`
4. Run the project using `SpringBootApplication`

### 🌐 Frontend
1. `cd frontend/`
2. Run `npm install`
3. Start the dev server with `npm start`

---

## Database Schema

The schema consists of:

- **User**: Basic user info with roles (player, referee, admin)
- **Tournament**: Name, start/end date, registration deadline
- **Match**: Links to 2 players, one referee, tournament, and score
- Relations:
  - Many-to-many between players and tournaments
  - One-to-many from tournaments to matches

---

## Exporting Matches

Admins can export match data in:
- CSV
- JSON
- TXT

This feature uses the Strategy Pattern to support multiple export formats.

---

## Screenshots

Coming soon...

---

## License

This project is open-source and available under the [MIT License](LICENSE).

---

## Author

Developed by [Your Name]
