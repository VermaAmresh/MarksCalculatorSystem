# Marks Calculator System

A robust Java-based menu-driven application for managing student records and calculating academic results.  
This system integrates with a MySQL database for storage while maintaining a local backup file (`student_info.txt`) for redundancy.  

It is designed with secure handling of credentials and provides a smooth experience for both academic record keeping and result computation.

---

## Key Features

- **Student Management:** Add, view, and modify student personal and academic information.  
- **Result Calculation:** Computes total, percentage, and grade for each student automatically.  
- **Data Synchronization:** Changes are reflected in both the database and local backup file.  
- **Menu-Driven Interface:** Simple and intuitive terminal-based user interface.  
- **Security:** Database credentials stored separately in a config file to prevent exposure.  
- **Search & Display:** Quickly retrieve student data using unique student IDs and view all names.  
- **Error Handling:** Robust exception handling ensures smooth program execution.

---

## Project Structure

- **src/**  
  Contains all the Java source code files for the Marks Calculator System.

- **lib/**  
  Holds the MySQL Connector/J `.jar` file required for database connectivity.

- **bin/**  
  Contains compiled `.class` files after building the project.

- **student_info.txt**  
  Auto-generated local backup file that stores student information in sync with the database.

- **config.properties**  
  Stores database credentials (`db.url`, `db.user`, `db.pass`).  
  **Note:** This file is ignored by Git for security reasons.

- **.gitignore**  
  Lists files and folders to be excluded from Git, such as sensitive files and compiled classes.


---

## Technologies Used

- **Java** – Core programming language for application logic  
- **MySQL** – Relational database for storing student records  
- **JDBC** – Java Database Connectivity for interacting with MySQL  
- **File I/O** – Local backup mechanism for persistence  
- **Scanner & Exception Handling** – User input and error management  

---

## Highlights

- Fully **menu-driven system** suitable for small educational setups.  
- **Data integrity ensured** by syncing database and local file.  
- Designed with **extensibility** in mind — easy to add new features like more subjects, reports, or analytics.  
- Clean, readable, and maintainable code with **modular methods** for each operation.  

---

## Author

**Amresh Verma**

---


