# Food Ordering Application - End to End Workflow & File Explanation

This document explains the step-by-step working of the Food Ordering Application across both the Backend (Spring Boot) and Frontend (React) stacks. 

## End-to-End Workflow

The application supports three roles: `ADMIN`, `OWNER`, and `USER`. Here is the general flow of operations:

1. **User/Owner Registration:** Users or Restaurant Owners register via the frontend (`/register` and `/register-owner`). Owners are saved with a `PENDING` status.
2. **Email Verification (Mocked):** During Owner registration, an email verification token is created and printed in the backend console. Visiting that link verifies the Owner's email.
3. **Admin Approval:** An `ADMIN` logs into the dashboard to review and approve `PENDING` owners. Once approved, their status becomes `APPROVED`.
4. **Restaurant Creation:** An approved `OWNER` logs into their dashboard (`/owner-dashboard`), creates a restaurant profile, and manages their food menu (Add/Update/Delete food items).
5. **Browsing and Ordering:** A regular `USER` logs in, views the list of restaurants, browses the menu for a restaurant, adds items to their cart, and places an order.
6. **Order Fulfillment:** The `OWNER` can see orders placed at their restaurant in their dashboard and manage them. The `USER` can view their past orders.

---

## Backend Structure (Spring Boot)

The Backend handles security, business logic, and database operations. Here is a breakdown of the critical directories and what each file does:

### 1. Security & Configuration (`/security`)
- **[SecurityConfig.java](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Backend/src/main/java/com/foodapp/backend/security/SecurityConfig.java)**: Configures the HTTP security (CORS, CSRF, Route Matchers). Defines which endpoints are public (e.g., `/api/auth/**`) and which require specific roles (e.g., `/api/admin/**` for `ADMIN`, `/api/owner/**` for `OWNER`).
- **[JwtUtil.java](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Backend/src/main/java/com/foodapp/backend/security/JwtUtil.java)**: A utility class to Generate, Parse, and Validate JWT tokens used for authentication.
- **[JwtAuthenticationFilter.java](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Backend/src/main/java/com/foodapp/backend/security/JwtAuthenticationFilter.java)**: Intercepts every incoming HTTP request to check for the presence of a valid JWT token in the `Authorization` header. If valid, it sets the Authentication context in Spring Security.
- **[CustomUserDetailsService.java](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Backend/src/main/java/com/foodapp/backend/security/CustomUserDetailsService.java)**: Implements Spring's `UserDetailsService`. It loads the user details from the database by email during authentication.

### 2. Controllers (`/controller`)
Controllers are the entry points for the frontend REST API.
- **[AuthController.java](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Backend/src/main/java/com/foodapp/backend/controller/AuthController.java)**: Handles `/api/auth/register`, `/api/auth/login`, and `/api/auth/verify-email`. It orchestrates authentication and token generation.
- **[AdminController.java](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Backend/src/main/java/com/foodapp/backend/controller/AdminController.java)**: Endpoints (`/api/admin/**`) for the Admin to fetch all users/owners, and approve or reject pending restaurant owners.
- **[OwnerController.java](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Backend/src/main/java/com/foodapp/backend/controller/OwnerController.java)**: Endpoints (`/api/owner/**`) for an Owner to manage their restaurant, upload menu items, and view orders placed at their restaurant.
- **[RestaurantController.java](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Backend/src/main/java/com/foodapp/backend/controller/RestaurantController.java)**: Public or User endpoints (`/api/restaurants/**`) to fetch the list of all active restaurants and their associated food menus.
- **[CartController.java](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Backend/src/main/java/com/foodapp/backend/controller/CartController.java)**: Endpoints (`/api/cart/**`) for a User to add items to their cart, view the cart, or clear it.
- **[OrderController.java](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Backend/src/main/java/com/foodapp/backend/controller/OrderController.java)**: Endpoints (`/api/orders/**`) for placing an checkout order from the cart, and retrieving a user's order history.

### 3. Services (`/service`)
Services contain the core business logic. They are called by the Controllers and communicate with Repositories.
- **[AuthService.java](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Backend/src/main/java/com/foodapp/backend/service/AuthService.java)**: Logic for checking if an email exists, hashing passwords, generating email verification tokens, and verifying them.
- **[AdminService.java](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Backend/src/main/java/com/foodapp/backend/service/AdminService.java)**: Logic to change an owner's status to `APPROVED` or `REJECTED`.
- **[OwnerService.java](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Backend/src/main/java/com/foodapp/backend/service/OwnerService.java)**: Logic for owners to add/update restaurants and food items. Verifies that the logged-in owner actually owns the restaurant before allowing modifications.
- **[RestaurantService.java](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Backend/src/main/java/com/foodapp/backend/service/RestaurantService.java)**: Logic to fetch restaurants and their menus for the end user.
- **[CartService.java](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Backend/src/main/java/com/foodapp/backend/service/CartService.java)**: Logic for managing cart state in the DB. Calculates totals based on quantities and prices of `CartItem`s.
- **[OrderService.java](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Backend/src/main/java/com/foodapp/backend/service/OrderService.java)**: Logic that transfers `CartItem`s to `OrderItem`s and creates an `Order`, then clears the user's cart.
- **[EmailService.java](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Backend/src/main/java/com/foodapp/backend/service/EmailService.java)**: Mocked service to log verification links to the console for development testing.

### 4. Repositories (`/repository`)
These are Spring Data JPA interfaces that handle database queries natively. For example, [UserRepository.java](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Backend/src/main/java/com/foodapp/backend/repository/UserRepository.java) finds users by email, [OrderRepository.java](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Backend/src/main/java/com/foodapp/backend/repository/OrderRepository.java) fetches orders by user ID, etc.

### 5. Entities (`/entity`) & DTOs (`/dto`)
- **Entities**: Java classes annotated with `@Entity` that map directly to MySQL tables (e.g., `User`, `Restaurant`, `FoodItem`, `Cart`, `Order`).
- **DTOs (Data Transfer Objects)**: Lightweight classes used to transfer data between the Frontend and Backend without exposing internal Entity structures (e.g., `AuthResponse`, `RegisterRequest`, `CartDto`).

---

## Frontend Structure (React + Vite)

The frontend is built with React Router to manage views. It uses `axios` to fetch data from the backend and passes JWT tokens in the headers.

### 1. Entry Point
- **[main.jsx](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Frontend/src/main.jsx)** & **[App.jsx](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Frontend/src/App.jsx)**: Initializes the React app, mounts Context/Providers, and defines the `<Routes>` for all the application pages.
- **[index.css](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Frontend/src/index.css)**: Global design settings, color variables, and typography.

### 2. Components (`/components`)
Reusable UI pieces.
- **[Navbar.jsx](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Frontend/src/components/Navbar.jsx)**: The navigation bar changes dynamically based on whether the user is logged in and what role they possess (e.g., an Admin sees an Admin Dashboard link, a User sees a Cart).

### 3. Pages (`/pages`)
The main views mapped to React Router paths.
- **[Home.jsx](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Frontend/src/pages/Home.jsx)**: The landing page displaying featured sections and general app information.
- **[Login.jsx](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Frontend/src/pages/Login.jsx) & [RegisterUser.jsx](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Frontend/src/pages/RegisterUser.jsx)**: Forms for handling user and owner authentication, capturing input, and posting it to the backend `AuthController`. They store the returned JWT in LocalStorage.
- **[AdminDashboard.jsx](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Frontend/src/pages/AdminDashboard.jsx)**: A protected route for Admins to view pending owners and click "Approve". Uses `AdminController` APIs.
- **[OwnerDashboard.jsx](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Frontend/src/pages/OwnerDashboard.jsx)**: A protected route for Owners. Forms to register their restaurant details and add menu items (`FoodItem`).
- **[Restaurants.jsx](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Frontend/src/pages/Restaurants.jsx) & [Menu.jsx](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Frontend/src/pages/Menu.jsx)**: Pages for users to see available restaurants and browse their food menus. Clicking "Add to Cart" triggers the `CartController`.
- **[Cart.jsx](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Frontend/src/pages/Cart.jsx)**: Displays what the user has added to their cart so far, calculates the total price, and includes a "Checkout" button that calls the `OrderController`.
- **[Orders.jsx](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Frontend/src/pages/Orders.jsx)**: Fetches and displays a user's past order history with their statuses.

---

### Step-by-Step Example: Placing an Order
1. The **User** clicks "Add to Cart" on a Pizza item in [Menu.jsx](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Frontend/src/pages/Menu.jsx).
2. Frontend sends an HTTP POST request to `/api/cart/add` with the `foodItemId`.
3. The request hits `JwtAuthenticationFilter`, which verifies the user's token.
4. `CartController#addToCart()` receives the request.
5. It passes data to `CartService` which finds the `User`'s `Cart`. If the food item is not in the `Cart`, it creates a `CartItem` entity. If it is, it increments the quantity.
6. The Database is updated via `CartItemRepository`. Returns success to frontend.
7. User navigates to [Cart.jsx](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Frontend/src/pages/Cart.jsx) and clicks "Checkout".
8. Frontend sends an HTTP POST to `/api/orders/checkout`.
9. `OrderController` alerts `OrderService`. The backend creates a new `Order`, converts `CartItem`s to `OrderItem`s, links them to the `Order`, and then clears the `Cart`.
10. The user is redirected to [Orders.jsx](file:///c:/Users/deepak/OneDrive/Desktop/Food%20Ordering%20Application/Frontend/src/pages/Orders.jsx) where the new order is visible.
