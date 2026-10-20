# ShopSphere – Design Diagrams

GitHub renders these Mermaid diagrams automatically.

## 1. System Architecture

```mermaid
flowchart TB
    subgraph Clients
        B[Web Browser<br/>Customer / Admin]
        S[Swing Admin App]
        NC[Socket Inventory Client]
        RC[RMI Client]
    end

    subgraph Tomcat["Apache Tomcat 11 (Web Container)"]
        F[Filters<br/>Encoding, Locale, Authentication, Admin]
        C[Servlet Controllers]
        V[JSP Views<br/>EL, JSTL, fragments, tag files]
        L[Listeners]
    end

    NS[Inventory Server<br/>ServerSocket :6000]
    RS[RMI Server<br/>Registry :1099]

    subgraph Core["core module"]
        SV[Service Layer]
        D[DAO Layer]
    end

    DB[(MySQL<br/>shopsphere)]

    B -- HTTP --> F --> C --> SV
    C --> V --> B
    S --> SV
    NC -- serialized objects --> NS --> D
    RC -- remote calls --> RS --> D
    SV --> D -- JDBC --> DB
```

## 2. N-Tier Request Flow (web store)

```mermaid
flowchart LR
    A[Browser] --> B[JSP / HTML / JS] --> C[Servlet Controller] --> D[Service Layer] --> E[DAO Layer] --> F[JDBC] --> G[(MySQL)]
```

## 3. Entity Relationship Diagram

```mermaid
erDiagram
    USERS ||--o{ ADDRESSES : has
    USERS ||--o{ CART : fills
    USERS ||--o{ WISHLIST : saves
    USERS ||--o{ ORDERS : places
    USERS ||--o{ REVIEWS : writes
    CATEGORIES ||--o{ PRODUCTS : groups
    PRODUCTS ||--o{ CART : "added to"
    PRODUCTS ||--o{ WISHLIST : "saved in"
    PRODUCTS ||--o{ ORDER_ITEMS : "sold as"
    PRODUCTS ||--o{ REVIEWS : receives
    ORDERS ||--|{ ORDER_ITEMS : contains
    ORDERS ||--o{ PAYMENTS : "paid by"
    ADDRESSES |o--o{ ORDERS : "delivered to"
    COUPONS ||--o{ COUPON_USAGE : "used in"
    USERS ||--o{ COUPON_USAGE : uses
    ORDERS ||--o| COUPON_USAGE : applies

    USERS {
        int user_id PK
        string name
        string email UK
        string password "PBKDF2 hash"
        string mobile
        enum role "CUSTOMER / ADMIN"
        enum status "ACTIVE / BLOCKED"
        timestamp created_at
    }
    ADDRESSES {
        int address_id PK
        int user_id FK
        string address_line
        string city
        string state
        string pincode
        enum address_type
    }
    CATEGORIES {
        int category_id PK
        string name UK
        string description
        boolean status
    }
    PRODUCTS {
        int product_id PK
        int category_id FK
        string name
        string brand
        text description
        decimal price
        decimal discount
        int stock
        string image_url
        boolean status
        timestamp created_at
    }
    CART {
        int cart_id PK
        int user_id FK
        int product_id FK
        int quantity
    }
    WISHLIST {
        int wishlist_id PK
        int user_id FK
        int product_id FK
        timestamp created_at
    }
    ORDERS {
        int order_id PK
        int user_id FK
        int address_id FK
        string shipping_address
        decimal total_amount
        decimal discount
        string payment_method
        enum payment_status
        enum order_status
        timestamp created_at
    }
    ORDER_ITEMS {
        int order_item_id PK
        int order_id FK
        int product_id FK
        int quantity
        decimal price
    }
    PAYMENTS {
        int payment_id PK
        int order_id FK
        string payment_method
        string transaction_reference
        decimal amount
        enum payment_status
        timestamp created_at
    }
    REVIEWS {
        int review_id PK
        int user_id FK
        int product_id FK
        int rating
        string review_text
        timestamp created_at
    }
    COUPONS {
        int coupon_id PK
        string code UK
        enum discount_type
        decimal discount_value
        decimal minimum_order
        decimal maximum_discount
        date expiry_date
        boolean status
    }
    COUPON_USAGE {
        int usage_id PK
        int coupon_id FK
        int user_id FK
        int order_id FK
        timestamp used_at
    }
```

## 4. Use Cases

```mermaid
flowchart LR
    Guest((Guest))
    Customer((Customer))
    Admin((Admin))

    Guest --> U1[Browse / search / filter products]
    Guest --> U2[View product details and reviews]
    Guest --> U3[Register / Login]
    Guest --> U13[Switch language EN / HI]

    Customer --> U4[Manage cart]
    Customer --> U5[Manage wishlist]
    Customer --> U6[Checkout with address, coupon, payment]
    Customer --> U7[Track and cancel orders]
    Customer --> U8[Write reviews]
    Customer --> U9[Edit profile and addresses]

    Admin --> A1[View dashboard]
    Admin --> A2[Product and inventory CRUD]
    Admin --> A3[Category CRUD]
    Admin --> A4[Update order status]
    Admin --> A5[Block / unblock customers]
    Admin --> A6[Manage coupons]
```

## 5. Class Diagram (core)

```mermaid
classDiagram
    direction LR
    class User {
        int userId
        String name
        String email
        String role
        isAdmin() boolean
    }
    class Product {
        int productId
        String name
        double price
        double discount
        int stock
        getFinalPrice() double
        isLowStock() boolean
    }
    class Category {
        int categoryId
        String name
        boolean status
    }
    class CartItem {
        Product product
        int quantity
        getSubtotal() double
    }
    class Order {
        int orderId
        double totalAmount
        String orderStatus
        List~OrderItem~ items
        isCancellable() boolean
    }
    class OrderItem {
        int productId
        int quantity
        double price
    }
    class Coupon {
        String code
        String discountType
        calculateDiscount(subtotal) double
    }
    class CartSummary {
        List~CartItem~ items
        double subtotal
        double discount
        double shipping
        getTotal() double
    }
    class AuthService {
        register()
        login()
        changePassword()
    }
    class ProductService {
        search()
        getActiveProduct()
        addReview()
        saveProduct()
    }
    class CartService {
        add()
        update()
        remove()
        moveToCart()
    }
    class CheckoutService {
        summarize()
        validateCoupon()
        placeOrder()
    }
    class OrderService {
        getOrdersForUser()
        updateStatus()
        cancelByCustomer()
    }
    class DBConnection {
        getConnection() Connection
    }
    class ProductDAO
    class OrderDAO
    class CartDAO

    Order "1" *-- "many" OrderItem
    CartItem --> Product
    Product --> Category
    CartSummary o-- CartItem
    CartSummary --> Coupon

    CheckoutService --> CartDAO
    CheckoutService --> OrderDAO
    CheckoutService --> ProductDAO
    ProductService --> ProductDAO
    OrderService --> OrderDAO
    ProductDAO --> DBConnection
    OrderDAO --> DBConnection
    CartDAO --> DBConnection
```

## 6. Login Sequence

```mermaid
sequenceDiagram
    actor U as User
    participant B as Browser
    participant LS as LoginServlet
    participant AS as AuthService
    participant D as UserDAO
    participant DB as MySQL

    U->>B: enter email + password
    B->>LS: POST /login
    LS->>AS: login(email, password)
    AS->>D: findByEmail(email)
    D->>DB: SELECT * FROM users WHERE email = ?
    DB-->>D: row
    D-->>AS: User
    AS->>AS: PasswordUtil.verify(password, hash)
    AS-->>LS: User (or ServiceException)
    LS->>LS: changeSessionId(), session.setAttribute("user")
    LS-->>B: redirect to /home or /admin/dashboard
```

## 7. Place-Order Sequence

```mermaid
sequenceDiagram
    actor C as Customer
    participant CS as CheckoutServlet
    participant S as CheckoutService
    participant DB as MySQL (one transaction)

    C->>CS: POST /checkout (placeOrder, addressId, paymentMethod)
    CS->>S: placeOrder(userId, addressId, coupon, method)
    S->>S: recalculate cart totals and validate coupon on the server
    S->>DB: BEGIN
    S->>DB: INSERT orders
    loop each cart item
        S->>DB: UPDATE products SET stock = stock - qty WHERE stock >= qty
        S->>DB: INSERT order_items
    end
    S->>DB: INSERT payments (mock gateway)
    S->>DB: INSERT coupon_usage
    S->>DB: DELETE FROM cart
    S->>DB: COMMIT (ROLLBACK on any failure)
    S-->>CS: orderId
    CS-->>C: redirect /order-success?id=orderId
```
