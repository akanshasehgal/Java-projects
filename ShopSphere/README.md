# ShopSphere – E-Commerce Management System

**ShopSphere** is a complete Java e-commerce project built for the **Advanced Java (RTU)** syllabus.
Customers shop on a modern web store, administrators manage everything from a web panel or a
Swing desktop app, and separate modules demonstrate Socket networking, RMI, serialization,
JNDI and internationalization, all working on the same MySQL database.

## Features

**Customer web store**
- Register, log in, log out, profile and password change (passwords hashed with PBKDF2)
- Product browsing with **search, category/price filters and sorting**
- Product details with ratings, reviews and related products
- **Shopping cart** (quantity, stock checks), **wishlist** (move to cart)
- Saved delivery **addresses**
- **Coupons** (percentage or flat, minimum order, maximum discount, expiry, one use per customer)
- **Checkout** with mock payment (Cash on Delivery / UPI / Card) inside a database transaction
- **Order history and tracking timeline**, cancel before shipping (stock is restored)
- "Recently viewed" products (cookie) and English / **Hindi** interface

**Admin (web panel `/admin` and Swing desktop app)**
- Dashboard: total sales, orders to process, customers, products, low-stock alerts
- Product CRUD, hide/show products, inventory and restocking
- Category CRUD
- Order management (status: Placed → Confirmed → Shipped → Delivered / Cancelled)
- Customer list with block / unblock
- Coupon management

**Syllabus modules**
- `network`: Inventory **Socket** server and client exchanging **serialized** objects
- `rmi`: remote **RMI** inventory service with the RMI registry, `Naming` and JNDI lookup
- Demo pages: JSP elements, Servlet life cycle, JSTL XML, JSTL SQL, JNDI DataSource, URL/URLConnection, i18n

## Technology Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17+ |
| Web | Jakarta Servlet 6.1 + JSP 4.0 on **Apache Tomcat 11** |
| View | JSP, EL, JSTL (core, fmt, functions, xml, sql), tag files, custom tag handler, Bootstrap 5 |
| Database | MySQL 8+ |
| Database access | JDBC with MySQL Connector/J (DAO pattern, PreparedStatement, transactions) |
| Desktop | Java Swing (MVC, pluggable look and feel) |
| Networking | `java.net` Socket / ServerSocket, Object streams |
| Distributed | Java RMI, RMI Registry, JNDI |
| Build | Maven (multi-module) |

## Architecture

```
                         SHOPSPHERE
                             |
            +----------------+-----------------+
            |                                  |
     Web Customer App                    Swing Admin App
   (Browser -> Tomcat)                    (desktop JVM)
            |                                  |
   JSP views  <-  Servlet controllers      Frames <- Controllers
            |                                  |
            +----------------+-----------------+
                             |
                       Service Layer        <- business rules, validation, totals
                             |
               +-------------+--------------+
               |                            |
              DAO                  Socket server / RMI server
               |                            |
              JDBC  ------------------------+
               |
             MySQL
```

N-tier web flow: **Browser → JSP → Servlet → Service → DAO → JDBC → MySQL**.
More diagrams (ER, use case, class, login and order sequences): [docs/DIAGRAMS.md](docs/DIAGRAMS.md)

## Project Structure

```
ShopSphere/
├── pom.xml                         parent Maven project
├── database/shopsphere.sql         schema + sample data
├── core/                           shared by every module
│   └── src/main/java/com/shopsphere/
│       ├── model/                  User, Product, Category, CartItem, Order, Coupon ... (Serializable)
│       ├── dao/                    DBConnection + one DAO per table (JDBC)
│       ├── service/                AuthService, ProductService, CartService, CheckoutService ...
│       └── util/PasswordUtil.java  PBKDF2 password hashing
├── web/                            Tomcat web application (WAR)
│   └── src/main/
│       ├── java/com/shopsphere/web/
│       │   ├── controller/         customer servlets, admin/ servlets, demo/ servlets
│       │   ├── filter/             Encoding, Locale, Authentication, Admin filters
│       │   ├── listener/           ServletContextListener, HttpSessionListener
│       │   ├── tag/PriceTag.java   custom tag handler
│       │   └── util/               recently-viewed cookie
│       ├── resources/i18n/         messages.properties, messages_hi.properties
│       └── webapp/
│           ├── WEB-INF/views/      JSP pages (+ admin/, demo/, fragments/)
│           ├── WEB-INF/tags/       productCard, productImage, rating, statusBadge tag files
│           ├── WEB-INF/web.xml     filters, error pages, JSP config, JNDI references
│           ├── META-INF/context.xml JNDI DataSource
│           ├── demo/               syllabus demo pages
│           ├── css/  js/
│           └── index.jsp
├── swing-admin/                    desktop admin (LoginFrame, AdminDashboard, ProductFrame ...)
├── network/                        InventoryServer, InventoryClient, request/response objects
├── rmi/                            InventoryService (remote), server, client
└── docs/                           diagrams, test cases, applet note, project guide
```

## Setup and Running

### 1. Requirements
- JDK 17 or newer, Maven 3.9+, MySQL 8+, Apache Tomcat 11

### 2. Create the database
```bash
mysql -u root -p < database/shopsphere.sql
```
If your MySQL user/password is not `root`/`root`, change it in:
- `core/src/main/resources/db.properties` (used by all modules)
- `web/src/main/webapp/META-INF/context.xml` (JNDI DataSource for the demo pages)

### 3. Build everything
```bash
mvn clean install
```
This creates `web/target/shopsphere.war` and installs the module JARs.

### 4. Run the web store
1. Copy `web/target/shopsphere.war` to `TOMCAT_HOME/webapps/`.
2. For the **JNDI and JSTL SQL demos only**, also copy the MySQL Connector/J JAR
   (from `~/.m2/repository/com/mysql/mysql-connector-j/`) into `TOMCAT_HOME/lib/`.
3. Start Tomcat (`bin/startup.bat` or `bin/startup.sh`) and open **http://localhost:8080/shopsphere/**

### 5. Run the Swing admin app
```bash
mvn -pl swing-admin exec:java
```

### 6. Run the Socket inventory server and client (two terminals)
```bash
mvn -pl network exec:java
```
```bash
mvn -pl network exec:java -Dexec.mainClass=com.shopsphere.network.client.InventoryClient
```

### 7. Run the RMI server and client (two terminals)
```bash
mvn -pl rmi exec:java
```
```bash
mvn -pl rmi exec:java -Dexec.mainClass=com.shopsphere.rmi.client.RmiClient
```

## Sample Data

| Account | Email | Password |
|---------|-------|----------|
| Admin | `admin@shopsphere.com` | `admin123` |
| Customer | `rahul@example.com` | `customer123` |
| Customer | `priya@example.com` | `customer123` |

| Coupon | Offer |
|--------|-------|
| `WELCOME10` | 10% off (max ₹500) on orders above ₹999 |
| `FLAT200` | ₹200 off on orders above ₹1499 |
| `FESTIVE25` | 25% off (max ₹1000) on orders above ₹2999 |
| `OLD50` | expired, used to test validation |

18 products in 6 categories, including low-stock and out-of-stock items, 2 sample orders and reviews.

## Important URLs

| URL | Page |
|-----|------|
| `/shopsphere/` | Home |
| `/shopsphere/products` | Listing, search and filters |
| `/shopsphere/cart`, `/checkout`, `/orders` | Customer pages (login required) |
| `/shopsphere/admin/dashboard` | Admin panel (ADMIN role required) |
| `/shopsphere/demo/index.jsp` | All syllabus demos |

## RTU Syllabus Mapping

| Topic | Where it is implemented |
|-------|-------------------------|
| Swing, Swing components | `swing-admin`: JFrame, JPanel, JLabel, JTextField, JPasswordField, JButton, JCheckBox, JRadioButton, JComboBox, JTable, JScrollPane, JSpinner, JOptionPane, JMenuBar |
| MVC architecture | Swing: `view` + `controller` packages + `core` model; Web: JSP views + servlet controllers + service/DAO |
| Pluggable look and feel | Swing dashboard → *Look & Feel* menu (switches every open window at runtime) |
| Applets | Concept note in [docs/APPLETS.md](docs/APPLETS.md) (removed from modern Java) |
| JDBC, java.sql, driver | `core/dao`: DriverManager, Connection, PreparedStatement, ResultSet, generated keys, transactions |
| java.net, client and server | `network`: ServerSocket, Socket, multi-threaded server, console client |
| Content / protocol handlers | `/demo/url`: URL, URLConnection, HttpURLConnection, URLEncoder |
| RMI architecture, registry | `rmi`: Remote interface, UnicastRemoteObject, LocateRegistry, Naming.rebind / lookup |
| Naming / JNDI | RMI client JNDI lookup; `/demo/jndi` DataSource + env-entry; `context.xml` |
| Serialization | All models implement `Serializable`; objects sent over sockets and RMI |
| Internationalization | Locale, ResourceBundle (EN/HI), `fmt:message`, locale-aware currency/date (`/demo/i18n.jsp`) |
| J2EE / N-tier architecture | Browser → JSP → Servlet → Service → DAO → JDBC → MySQL |
| HTTP, web container | Apache Tomcat 11, GET/POST, redirects, error pages, status codes |
| Servlets, life cycle | All controllers; `/demo/lifecycle` shows init / service / destroy |
| ServletConfig / ServletContext | `LifecycleServlet` init-param; context-params and attributes |
| Request / response | Forms, search, login, checkout, orders |
| Session handling, cookies | Login state, cart count, coupon in `HttpSession`; remember-email, language and recently-viewed cookies |
| Event handling | `AppListener` (ServletContextListener), `SessionListener` (HttpSessionListener) |
| Filters | EncodingFilter, LocaleFilter, AuthenticationFilter, AdminFilter |
| JSP architecture, elements | `/demo/jsp-elements.jsp`: directives, declarations, scriptlets, expressions, comments, implicit objects, `jsp:useBean`, `jsp:include` |
| Expression Language | Every page, e.g. `${product.name}`, `${sessionScope.user.name}` |
| Tag extensions / handlers | `PriceTag` (SimpleTagSupport) + `WEB-INF/shopsphere.tld` → `<ss:price>` |
| JSP fragments | `WEB-INF/views/fragments`: header, navbar, footer, sidebar, totals, admin-sidebar |
| Tag files | `WEB-INF/tags`: productCard, productImage, rating, statusBadge |
| JSTL core / fmt / functions | `c:forEach`, `c:if`, `c:choose`, `c:set`, `fmt:message`, `fmt:formatDate`, `fn:length`, `fn:escapeXml` ... |
| JSTL XML | `/demo/jstl-xml.jsp` reads the `/demo/feed.xml` servlet with XPath |
| JSTL SQL | `/demo/jstl-sql.jsp` (demo only; the store uses the DAO layer) |

## Security

- Passwords hashed with salted **PBKDF2** (65,536 iterations), never stored or sent in cookies
- Every query uses **PreparedStatement** (no SQL injection)
- **AuthenticationFilter** protects customer pages, **AdminFilter** protects `/admin/*`
- New session id after login (prevents session fixation), HttpOnly cookies
- Server-side validation; order totals are always **recalculated on the server**
- Customers can only see their own orders; user text is HTML-escaped (`c:out`, `fn:escapeXml`)

## Testing

32 manual test cases: [docs/TEST-CASES.md](docs/TEST-CASES.md).

Build status at the time of upload: the `core`, `swing-admin`, `network` and `rmi` modules were
compiled with JDK 21 and the Socket client/server exchange was run end to end. The `web` module
(servlets and JSPs) is built by `mvn clean install` and runs on Tomcat 11.

## Viva Demonstration Checklist

1. Show the Swing admin: login, product JTable, change the look and feel.
2. Show JDBC CRUD: add a product in Swing, refresh the web store.
3. Run the Socket client-server: `GET_PRODUCT`, `SEARCH_PRODUCT`.
4. Run the RMI server and client: `getStock`, `updateStock`.
5. Explain the N-tier architecture with `docs/DIAGRAMS.md`.
6. Deploy the WAR on Tomcat; explain the Servlet life cycle at `/demo/lifecycle`.
7. Show the session (login, cart) and cookies (language, recently viewed).
8. Show the filters (open `/cart` logged out, `/admin` as a customer) and listeners (active sessions).
9. Show JSP, EL, JSTL, fragments and tag files; the JSTL XML/SQL demos.
10. Explain serialization, internationalization (switch to Hindi) and JNDI.

## Limitations and Future Scope

- Payment is simulated; a real gateway (Razorpay/Stripe) could be integrated.
- Product images use URLs or generated placeholders; file upload could be added.
- CSRF tokens, e-mail verification and password reset are not implemented.
- Future: REST API, recommendations, invoice PDF, delivery-partner tracking.

## Documents

- [Project guide](docs/ShopSphere_Project_Guide.docx)
- [Diagrams](docs/DIAGRAMS.md)
- [Test cases](docs/TEST-CASES.md)
- [Applets note](docs/APPLETS.md)
