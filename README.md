# Food Ordering Application

A Full-stack Food Ordering Application with an advanced Owner-Admin approval workflow.

## Tech Stack
- **Backend:** Java 17, Spring Boot 3.2, Spring Security (JWT), Spring Data JPA, MySQL
- **Frontend:** React.js (Vite), React Router, Axios, Pure CSS

## Features
- **Roles:** ADMIN, OWNER, USER.
- **Workflow:** Owner signs up -> PENDING -> Email Verification (Mocked in console) -> Admin manually approves -> Owner logs in and adds menu.
- **User Flow:** User registers -> browses approved restaurants -> adds food to cart -> places orders.

## Setup Instructions

### Database Setup
1. Install MySQL.
2. Create database `foodappdb` or let Spring Boot auto-create it.
3. The SQL schema is provided in `schema.sql` for reference, but Spring Boot will auto-generate it based on the Entities (`ddl-auto=update`).

### Backend Setup
1. `cd Backend`
2. Update `application.properties` with your MySQL credentials, and actual SMTP details if you wish to use real emails. Currently, Email Mocking is enabled directly to console.
3. Run `mvn clean install` and run the `BackendApplication.java` main class.

### Frontend Setup
1. `cd Frontend`
2. Run `npm install`
3. Run `npm run dev` to start the Vite server.
4. Access the web app at `http://localhost:5173`.

### Default Access
- **Admin Setup:** Since there is no default Admin created from UI, insert an admin manually into your database after starting the backend:
  ```sql
  INSERT INTO users (name, email, password, role, status) VALUES ('Super Admin', 'admin@foodapp.com', '$2a$10$wN2G7bBxO1Y3iXzLpHbHeO5D0NOrA0j/K/1e8K3z2w6xY1V0H0YpK', 'ADMIN', 'APPROVED');
  ```
  *(The hash corresponds to the password `password`)*

- Try registering as an Owner across the `/register-owner` UI page.
- Check the backend console to click the verification link simulating the email.
- Login as Admin at `/` to approve the owner.
- Log in as the approved Owner to submit food items.
- Log in as a normal User to add items to cart and checkout!
