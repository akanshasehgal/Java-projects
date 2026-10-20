# Java Projects – Advanced Java Lab

Source code and documentation for the **Advanced Java Lab** experiments and the **ShopSphere** project.
Every experiment has its own folder containing the Java source files, a README with run
instructions and expected output, and the lab record (`Lab-Record.pdf`).

- **Student:** Aakanksha Sehgal
- **Roll No.:** 24EGJCS001

## Experiments

| # | Title | Folder | Topics Covered |
|:-:|-------|--------|----------------|
| 1 | Student Registration Form using Swing | [Experiment-1-Student-Registration-Form](Experiment-1-Student-Registration-Form) | Swing components, event handling, form validation |
| 2 | Calculator using Swing (MVC Architecture) | [Experiment-2-MVC-Calculator](Experiment-2-MVC-Calculator) | Model, View, Controller, ActionListener |
| 3 | JDBC – Java Database Connectivity | [Experiment-3-JDBC-Database-Connectivity](Experiment-3-JDBC-Database-Connectivity) | MySQL, Connection, PreparedStatement, ResultSet |
| 4 | Server and Client (TCP Sockets) | [Experiment-4-TCP-Client-Server](Experiment-4-TCP-Client-Server) | ServerSocket, Socket, java.net |
| 5 | RMI – Remote Method Invocation | [Experiment-5-RMI-Remote-Method-Invocation](Experiment-5-RMI-Remote-Method-Invocation) | Remote interface, RMI Registry, stub |
| 6 | Java Servlet | [Experiment-6-Java-Servlet](Experiment-6-Java-Servlet) | Servlet life cycle, session, filters, listeners |

## Project – ShopSphere (E-Commerce Management System)

**Folder:** [ShopSphere](ShopSphere)

A complete online shopping system that brings the whole Advanced Java syllabus together in one application:

- **Web store** (Servlet + JSP + JSTL on Tomcat 11): registration/login, product search and filters, cart,
  wishlist, coupons, checkout with mock payment, order tracking, reviews, English/Hindi interface
- **Admin panel** (web) and **Swing desktop admin**: dashboard, products, categories, inventory, orders, customers, coupons
- **MySQL + JDBC** with a DAO and service layer (N-tier MVC architecture)
- **Socket** inventory server/client using **serialization**, and a remote **RMI** inventory service
- Demonstrations of **JNDI**, **JSTL XML/SQL**, the **Servlet life cycle**, **JSP elements**, **tag files** and a **custom tag handler**

See the [ShopSphere README](ShopSphere/README.md) for setup steps, sample logins and the full syllabus mapping.

## Software Needed
- **JDK 17 or newer** – every experiment
- **MySQL Server** and **MySQL Connector/J** – Experiment 3
- **Apache Maven** and **Apache Tomcat 11** – Experiment 6 and the ShopSphere project

## Running an Experiment
Open a terminal in the experiment's folder, then compile and run:

```bash
javac *.java
java <ClassWithMain>
```

| Exp. | Main class / steps |
|:----:|--------------------|
| 1 | `java StudentRegistration` |
| 2 | `java Main` |
| 3 | Create the database with `database.sql`, add the driver JAR to `lib/`, then run `JdbcDemo` |
| 4 | Run `java Server` first, then `java Client` in a second terminal |
| 5 | `start rmiregistry`, then `java Server`, then `java Client` |
| 6 | `mvn package` and deploy `target/servlet.war` to Tomcat |

Detailed steps and sample output are in each folder's README.
