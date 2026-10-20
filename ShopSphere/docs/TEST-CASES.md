# ShopSphere – Test Cases

Manual functional tests. Sample data comes from `database/shopsphere.sql`.

| # | Test | Steps | Expected result |
|---|------|-------|-----------------|
| 1 | Valid registration | Sign Up with a new email and a 6+ character password | Account created, user logged in, welcome message |
| 2 | Duplicate email | Sign Up with `rahul@example.com` | "An account with this email already exists." |
| 3 | Weak password | Sign Up with password `123` | "Password must be at least 6 characters long." |
| 4 | Valid login | `rahul@example.com` / `customer123` | Session created, redirected to home, name in navbar |
| 5 | Invalid login | Wrong password | "Invalid email or password." |
| 6 | Unauthenticated cart access | Log out, open `/cart` | Redirected to login, then back to `/cart` after logging in |
| 7 | Search product | Search "java" | Java books are listed |
| 8 | Filter and sort | Category = Electronics, sort Price low to high | Only electronics, ascending final price |
| 9 | Add to cart | Add earbuds to cart | Cart badge increases, item in cart |
| 10 | Change quantity | Change quantity to 3 | Subtotal and total recalculated |
| 11 | Stock limit | Add more than the available stock (e.g. Clean Code, stock 2) | "Only 2 units of Clean Code are left." |
| 12 | Out of stock | Open Adjustable Dumbbell Set (stock 0) | Add to cart disabled, "Out of Stock" |
| 13 | Valid coupon | Subtotal ≥ Rs.999, apply `WELCOME10` | 10% discount (max Rs.500) |
| 14 | Expired coupon | Apply `OLD50` | "This coupon has expired." |
| 15 | Minimum order | Small cart, apply `FESTIVE25` | "Add items worth Rs.X more to use FESTIVE25." |
| 16 | Coupon reuse | Priya applies `FLAT200` again | "You have already used coupon FLAT200." |
| 17 | Place order | Checkout with address + UPI | Order created, stock reduced, cart emptied, transaction id shown |
| 18 | Cancel order | Cancel a PLACED order | Status CANCELLED, stock restored, UPI payment REFUNDED |
| 19 | Order privacy | Open `/order?id=<someone else's order>` | 404 page |
| 20 | Admin login | `admin@shopsphere.com` / `admin123` | Admin dashboard opens |
| 21 | Customer opens /admin | Log in as customer, open `/admin/dashboard` | 403 "Access denied" page |
| 22 | Admin updates order | Change order #2 to DELIVERED | Customer sees DELIVERED; COD payment becomes SUCCESS |
| 23 | Admin product CRUD | Add, edit and hide a product | Changes visible in store; hidden product disappears from listing |
| 24 | Block customer | Block Rahul, then try to log in as Rahul | "Your account has been blocked." |
| 25 | Logout | Click Logout | Session invalidated, protected pages need login again |
| 26 | Language | Switch to हिन्दी | Navbar and labels in Hindi; choice remembered by cookie |
| 27 | Recently viewed | View 3 products, go home | "Recently Viewed" shows them (cookie) |
| 28 | Review | Post a 4-star review | Review listed, average rating updated |
| 29 | Socket request | Run server + client, GET_PRODUCT 1 | Product details printed by the client |
| 30 | RMI call | Run RMI server + client, getStock(1) | Stock returned from the remote object |
| 31 | Swing admin | Run swing-admin, log in, edit a product | Change saved and visible in the web store |
| 32 | JNDI demo | Open `/demo/jndi` | DataSource class and product count shown |
