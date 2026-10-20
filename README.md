# Java Projects – Advanced Java Lab

Source code and documentation for the **Advanced Java Lab** experiments.
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

## Software Needed
- **JDK 17 or newer** – every experiment
- **MySQL Server** and **MySQL Connector/J** – Experiment 3
- **Apache Maven** and **Apache Tomcat 11** – Experiment 6

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
