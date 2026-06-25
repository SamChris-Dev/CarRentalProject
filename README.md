# Car Rental Management System

![Car Icon](Images/coupe-car.svg)

A robust, enterprise-grade Client-Server application for managing car rentals seamlessly.

## 🚀 Features

This application allows car rental agencies to effectively manage their operations:
- **Available Cars**: Browse a catalog of all cars currently available for rent.
- **Rent Cars**: Rent out cars to clients by registering their name, phone number, and duration.
- **Manage Rentals**: View a live list of currently active rentals, tracking client details and pricing.
- **Returns**: Process car returns securely, which instantly updates vehicle availability.
- **Transaction History**: Track a comprehensive historical log of all past rental transactions.

## 🏛️ Architecture

The project has been engineered following a strict **Client-Server Architecture** utilizing robust separation of concerns and design patterns:

- **Client Layer (JavaFX)**: A rich desktop GUI application built with JavaFX. The client operates exclusively by consuming REST endpoints and contains zero database coupling.
- **Server Layer (Spark Java API)**: A lightweight, high-performance RESTful API backend built with the Spark web framework. It handles all business logic, routing, and validation.
- **Data Access Layer (DAO Pattern)**: The server communicates with an Oracle Database via JDBC using dedicated Data Access Objects (`CarDao`, `RentalDao`, `HistoryDao`). This strictly isolates SQL operations from business logic.
- **Shared Data Transfer Objects (DTO)**: Both client and server exchange data seamlessly using structured DTO classes (`Car`, `Rental`, `HistoryRecord`) serialized over HTTP via Google Gson.

## 📸 Screenshots

### 1. Main Menu / Available Cars
![Available Cars](Images/image_1.png)

### 2. Active Rentals
![Active Rentals](Images/image_2.png)

### 3. Rental History
![Rental History](Images/image_3.png)

## 🛠️ How to Build and Run

### Prerequisites
- **Java JDK 26** (or JDK 25+ compatible environment)
- **Maven** (A Maven wrapper is included)
- **Oracle Database XE** (Configured locally with user `C##car_rental` and password `car123`)

### 1. Build the Project
Compile the project and resolve all necessary Maven dependencies from the root directory:
```bash
.\mvnw clean compile
```

### 2. Run the Backend Server
Start the REST API server to listen for client requests. (Make sure you have set `JAVA_HOME` in this terminal window if it's newly opened!):
```bash
.\mvnw exec:java@server
```
The server will start and bind locally to `http://localhost:8080`.

### 3. Run the Client Application
Open a **new terminal window** (leave the server running) and launch the JavaFX Client:
```bash
.\mvnw javafx:run
```
