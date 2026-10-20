-- =====================================================================
--  ShopSphere - E-Commerce Management System
--  MySQL database script: schema + sample data
--
--  Run:   mysql -u root -p < shopsphere.sql
--
--  Sample logins (passwords are stored as PBKDF2 hashes):
--    Admin    : admin@shopsphere.com   / admin123
--    Customer : rahul@example.com      / customer123
--    Customer : priya@example.com      / customer123
-- =====================================================================

CREATE DATABASE IF NOT EXISTS shopsphere CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE shopsphere;

DROP TABLE IF EXISTS coupon_usage;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS wishlist;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS coupons;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS users;

-- ---------------------------------------------------------------- tables

CREATE TABLE users (
    user_id     INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(120) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    mobile      VARCHAR(15),
    role        ENUM('CUSTOMER', 'ADMIN') NOT NULL DEFAULT 'CUSTOMER',
    status      ENUM('ACTIVE', 'BLOCKED') NOT NULL DEFAULT 'ACTIVE',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE addresses (
    address_id    INT AUTO_INCREMENT PRIMARY KEY,
    user_id       INT NOT NULL,
    address_line  VARCHAR(255) NOT NULL,
    city          VARCHAR(80) NOT NULL,
    state         VARCHAR(80) NOT NULL,
    pincode       VARCHAR(10) NOT NULL,
    address_type  ENUM('HOME', 'WORK', 'OTHER') NOT NULL DEFAULT 'HOME',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE categories (
    category_id  INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(80) NOT NULL UNIQUE,
    description  VARCHAR(255),
    status       BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE products (
    product_id   INT AUTO_INCREMENT PRIMARY KEY,
    category_id  INT,
    name         VARCHAR(150) NOT NULL,
    brand        VARCHAR(80),
    description  TEXT,
    price        DECIMAL(10, 2) NOT NULL,
    discount     DECIMAL(5, 2) NOT NULL DEFAULT 0,
    stock        INT NOT NULL DEFAULT 0,
    image_url    VARCHAR(255),
    status       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE SET NULL
);

CREATE TABLE cart (
    cart_id     INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT NOT NULL,
    product_id  INT NOT NULL,
    quantity    INT NOT NULL DEFAULT 1,
    UNIQUE (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

CREATE TABLE wishlist (
    wishlist_id  INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT NOT NULL,
    product_id   INT NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

CREATE TABLE coupons (
    coupon_id         INT AUTO_INCREMENT PRIMARY KEY,
    code              VARCHAR(20) NOT NULL UNIQUE,
    discount_type     ENUM('PERCENT', 'FLAT') NOT NULL,
    discount_value    DECIMAL(10, 2) NOT NULL,
    minimum_order     DECIMAL(10, 2) NOT NULL DEFAULT 0,
    maximum_discount  DECIMAL(10, 2) NOT NULL DEFAULT 0,
    expiry_date       DATE,
    status            BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE orders (
    order_id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id           INT NOT NULL,
    address_id        INT,
    shipping_address  VARCHAR(400),
    total_amount      DECIMAL(10, 2) NOT NULL,
    discount          DECIMAL(10, 2) NOT NULL DEFAULT 0,
    payment_method    VARCHAR(20) NOT NULL,
    payment_status    ENUM('PENDING', 'SUCCESS', 'FAILED', 'REFUNDED') NOT NULL DEFAULT 'PENDING',
    order_status      ENUM('PLACED', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED') NOT NULL DEFAULT 'PLACED',
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (address_id) REFERENCES addresses(address_id) ON DELETE SET NULL
);

CREATE TABLE order_items (
    order_item_id  INT AUTO_INCREMENT PRIMARY KEY,
    order_id       INT NOT NULL,
    product_id     INT NOT NULL,
    quantity       INT NOT NULL,
    price          DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE TABLE payments (
    payment_id             INT AUTO_INCREMENT PRIMARY KEY,
    order_id               INT NOT NULL,
    payment_method         VARCHAR(20) NOT NULL,
    transaction_reference  VARCHAR(60),
    amount                 DECIMAL(10, 2) NOT NULL,
    payment_status         ENUM('PENDING', 'SUCCESS', 'FAILED', 'REFUNDED') NOT NULL DEFAULT 'PENDING',
    created_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

CREATE TABLE reviews (
    review_id    INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT NOT NULL,
    product_id   INT NOT NULL,
    rating       TINYINT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    review_text  VARCHAR(1000),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

CREATE TABLE coupon_usage (
    usage_id   INT AUTO_INCREMENT PRIMARY KEY,
    coupon_id  INT NOT NULL,
    user_id    INT NOT NULL,
    order_id   INT NOT NULL,
    used_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (coupon_id, user_id),
    FOREIGN KEY (coupon_id) REFERENCES coupons(coupon_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------- sample data

INSERT INTO users (name, email, password, mobile, role) VALUES
('Store Admin', 'admin@shopsphere.com',
 'pbkdf2$65536$fssz87C9jOU4kfcgNqZkpw==$IA+jn6bL5q1rjMAKpxfJ3B4ftj9EIdPxpdUmpnWwa8E=', '9876500000', 'ADMIN'),
('Rahul Sharma', 'rahul@example.com',
 'pbkdf2$65536$IPkKjmZvESUNWg6Jmo2X2g==$yETiytELZSrgmpkEnK7ap3admxVDF0j6cVy2GRiaxGk=', '9876511111', 'CUSTOMER'),
('Priya Verma', 'priya@example.com',
 'pbkdf2$65536$IPkKjmZvESUNWg6Jmo2X2g==$yETiytELZSrgmpkEnK7ap3admxVDF0j6cVy2GRiaxGk=', '9876522222', 'CUSTOMER');

INSERT INTO addresses (user_id, address_line, city, state, pincode, address_type) VALUES
(2, '21, Malviya Nagar, Near City Mall', 'Jaipur', 'Rajasthan', '302017', 'HOME'),
(2, 'Tech Park, Tower B, Sitapura', 'Jaipur', 'Rajasthan', '302022', 'WORK'),
(3, '45, Shastri Nagar', 'Jodhpur', 'Rajasthan', '342003', 'HOME');

INSERT INTO categories (name, description) VALUES
('Electronics',    'Mobiles, laptops, audio and accessories'),
('Fashion',        'Clothing, footwear and accessories'),
('Books',          'Programming, academic and fiction books'),
('Home & Kitchen', 'Appliances, cookware and decor'),
('Sports',         'Fitness equipment and sportswear'),
('Beauty',         'Skincare, grooming and personal care');

INSERT INTO products (category_id, name, brand, description, price, discount, stock) VALUES
(1, 'Galaxy M35 5G Smartphone (8GB, 128GB)', 'Samsung', '6.6-inch sAMOLED display, 6000 mAh battery and 50 MP triple camera.', 19999.00, 15, 25),
(1, 'Wireless Bluetooth Earbuds', 'boAt', 'Active noise cancellation, 40 hours playback and fast charging.', 2999.00, 40, 60),
(1, 'IdeaPad Slim 3 Laptop (Ryzen 5, 16GB)', 'Lenovo', '15.6-inch FHD laptop with 512 GB SSD, ideal for students.', 52990.00, 12, 8),
(1, 'Smart Watch with AMOLED Display', 'Noise', 'Heart-rate, SpO2 and sleep tracking with 7-day battery.', 4999.00, 50, 3),
(2, 'Men''s Cotton Casual Shirt', 'Peter England', 'Slim fit, 100% cotton, machine washable.', 1499.00, 30, 40),
(2, 'Women''s Running Shoes', 'Puma', 'Lightweight cushioned sole for daily runs and gym.', 3999.00, 35, 20),
(2, 'Leather Wallet', 'Woodland', 'Genuine leather wallet with RFID protection.', 999.00, 20, 4),
(3, 'Head First Java (3rd Edition)', 'O''Reilly', 'A brain-friendly guide to Java programming.', 899.00, 10, 30),
(3, 'Java: The Complete Reference', 'McGraw Hill', 'Comprehensive coverage of the Java language and libraries.', 1199.00, 15, 18),
(3, 'Clean Code', 'Pearson', 'A handbook of agile software craftsmanship.', 749.00, 5, 2),
(4, 'Non-Stick Cookware Set (3 pieces)', 'Prestige', 'Induction-friendly kadhai, fry pan and tawa.', 2499.00, 25, 15),
(4, 'Electric Kettle 1.5 L', 'Philips', 'Stainless steel kettle with auto shut-off.', 1795.00, 18, 22),
(4, 'Cotton Double Bedsheet Set', 'Bombay Dyeing', 'King-size bedsheet with 2 pillow covers.', 1299.00, 45, 35),
(5, 'Yoga Mat (6 mm)', 'Boldfit', 'Anti-slip yoga mat with carry strap.', 799.00, 30, 50),
(5, 'English Willow Cricket Bat', 'SG', 'Full-size bat for leather-ball cricket.', 6499.00, 10, 6),
(5, 'Adjustable Dumbbell Set (20 kg)', 'Kore', 'PVC dumbbells with rods for home workouts.', 2199.00, 22, 0),
(6, 'Vitamin C Face Serum', 'Minimalist', '10% Vitamin C serum for glowing skin.', 699.00, 10, 45),
(6, 'Beard Trimmer', 'Philips', 'Cordless trimmer with 20 length settings.', 1899.00, 28, 12);

INSERT INTO coupons (code, discount_type, discount_value, minimum_order, maximum_discount, expiry_date, status) VALUES
('WELCOME10', 'PERCENT', 10, 999,  500,  '2030-12-31', TRUE),
('FLAT200',   'FLAT',    200, 1499, 0,   '2030-12-31', TRUE),
('FESTIVE25', 'PERCENT', 25, 2999, 1000, '2030-12-31', TRUE),
('OLD50',     'PERCENT', 50, 0,    300,  '2024-01-31', TRUE);

-- Two completed sample orders so the order history and dashboard are not empty
INSERT INTO orders (user_id, address_id, shipping_address, total_amount, discount, payment_method,
                    payment_status, order_status, created_at) VALUES
(2, 1, '21, Malviya Nagar, Near City Mall, Jaipur, Rajasthan - 302017', 2608.50, 0, 'UPI',
 'SUCCESS', 'DELIVERED', NOW() - INTERVAL 12 DAY),
(3, 3, '45, Shastri Nagar, Jodhpur, Rajasthan - 342003', 4387.55, 200, 'COD',
 'PENDING', 'SHIPPED', NOW() - INTERVAL 2 DAY);

INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(1, 2, 1, 1799.40),
(1, 8, 1, 809.10),
(2, 6, 1, 2599.35),
(2, 13, 2, 714.45),
(2, 14, 1, 559.30);

INSERT INTO payments (order_id, payment_method, transaction_reference, amount, payment_status) VALUES
(1, 'UPI', 'TXN1729000000001', 2608.50, 'SUCCESS'),
(2, 'COD', NULL, 4387.55, 'PENDING');

INSERT INTO coupon_usage (coupon_id, user_id, order_id) VALUES (2, 3, 2);

INSERT INTO reviews (user_id, product_id, rating, review_text) VALUES
(2, 2, 5, 'Great sound quality and the battery easily lasts a week.'),
(2, 8, 4, 'Very easy to understand. Perfect for Java beginners.'),
(3, 6, 4, 'Comfortable for daily running, true to size.'),
(3, 2, 4, 'Good value for money, noise cancellation is decent.');
