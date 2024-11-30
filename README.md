![Hotel Management System](./assets/💻_Hotel_Management_System.png)

# Hotel Management System

**Hotel Management System** is a desktop application developed in Java that enables efficient management of hotel operations. It provides features such as room management, reservations, guest handling, and additional services.

---

## 🛠️ Technologies and Specifications

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Swing](https://img.shields.io/badge/Library-Swing-blue?style=flat-square)
![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20Linux%20%7C%20MacOS-lightgrey?style=flat-square)
![Version](https://img.shields.io/badge/Version-1.0-brightgreen?style=flat-square)
![Status](https://img.shields.io/badge/Status-Active-brightgreen?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)
![Contributions](https://img.shields.io/badge/Contributions-Welcome-brightgreen?style=flat-square)

---

## 📚 Table of Contents

- [About](#-about)
- [How to Install and Run the Project](#how-to-install-and-run-the-project)
- [How to Use the Project](#how-to-use-the-project)
- [License](#license)
- [Contact](#contact)

---

## 📌 About

The **Hotel Management System** is a robust and user-friendly application designed to streamline the daily operations of a hotel. Built using Java and the Swing library, this application supports the following key functionalities:

1. **User Roles**:
   - **Administrator**: Full control over hotel operations, including employee management and financial reports.
   - **Receptionist**: Handles reservations, guest check-ins/check-outs, and room assignments.
   - **Housekeeper**: Manages the cleaning schedule and updates room availability status.
   - **Guest**: Allows guests to request reservations, view booking statuses, and add services.

2. **Reservation Management**:
   - Guests can request room reservations for specific dates.
   - Reservation statuses include "Pending," "Confirmed," "Rejected," and "Cancelled."
   - Receptionists can approve or reject requests based on room availability.

3. **Room and Service Management**:
   - Tracks room status: Available, Occupied, or Under Cleaning.
   - Supports additional services like breakfast, laundry, and spa, which can be added to a guest's bill.

4. **Pricing and Reporting**:
   - Dynamic pricing based on the season and room type.
   - Generates reports on income, expenses, room usage, and housekeeping activities.

This project implements Object-Oriented Programming (OOP) principles to create a scalable and maintainable solution for managing hotel operations.

---

<a name="how-to-install-and-run-the-project"></a>
## 🛠️ How to Install and Run the Project

### Prerequisites
Before you begin, make sure you have the following installed and configured on your system:
1. **Java Development Kit (JDK)**:
   - JDK 17: Required for compatibility with core application features.
   - JDK 22: Optionally included in the setup.
   - [Download JDK](https://www.oracle.com/java/technologies/javase-downloads.html)
2. **JUnit 5**: Used for unit testing the application. This library is included in the project.
3. **XChart Library**: Used for data visualization.
4. **An Integrated Development Environment (IDE)**:
   - Preferably **IntelliJ IDEA** or **Eclipse** for easy setup.
   - [Download IntelliJ IDEA](https://www.jetbrains.com/idea/download/) | [Download Eclipse](https://www.eclipse.org/downloads/)
5. **Git** (optional): For cloning the repository.
   - [Download Git](https://git-scm.com/)

---

### Installation Steps
1. **Clone the Repository**:
   Open a terminal and run the following command to clone the repository:

   ```bash
   git clone https://github.com/MilanSazdov/hotel-management-system.git
   
Alternatively, download the repository as a ZIP file and extract it.

2. **Import the Project into IDE**:
   - Open your IDE (e.g., IntelliJ IDEA or Eclipse).
   - Select `File > Open` (in IntelliJ) or `Import > Existing Project` (in Eclipse).
   - Navigate to the folder where the repository is located and open it.

3. **Configure JDK and Dependencies**:
   - Ensure JDK 17 is selected as the default SDK for the project:
     - **In IntelliJ IDEA**: Navigate to `File > Project Structure > SDK` and select `JDK 17`.
     - **In Eclipse**: Right-click on the project, go to `Properties > Java Build Path > Libraries`, and add `JDK 17`.
   - Verify that the following libraries are added to the **Classpath**:
     - `hamcrest-core-1.3-javadoc.jar`
     - `junit-4.13.2-javadoc.jar`
     - `xchart-3.8.8.jar`

4. **Set Up Classpath**:
   - In the IDE, add the provided `.jar` files to the project:
     - **For IntelliJ IDEA**:
       - Right-click on the project folder.
       - Select `Add Framework Support > Add Library`.
       - Locate the `.jar` files (e.g., `hamcrest-core`, `JUnit`, `xchart`) and add them.
     - **For Eclipse**:
       - Right-click on the project > `Build Path > Add External Archives`.
       - Navigate to the folder containing the `.jar` files and add them.
---

### Running the Project

1. Open the `Main.java` file in your IDE.
2. Run the file:
   - **In IntelliJ IDEA**: Right-click on `Main.java` and select `Run`.
   - **In Eclipse**: Right-click on `Main.java` and select `Run As > Java Application`.
3. The application will start, and you can access the Hotel Management System's features.

---

### Potential Issues and Troubleshooting

1. **Error: `Java version not compatible`**:
   - Ensure that JDK 17 is installed and selected as the default JDK for the project.

2. **Error: `Cannot find Main class`**:
   - Check if the `src` folder is marked as a source root in the IDE.

3. **Missing Dependencies**:
   - Ensure that all `.jar` files (`hamcrest-core`, `JUnit`, `xchart`) are properly added to the project classpath.

---

### Notes

- The project uses **JUnit 5** for testing, so make sure to run test cases from the `test` directory if you want to validate the application functionality.
- The **XChart Library** is used to create charts for reporting, so ensure the `xchart-3.8.8.jar` is added correctly.



