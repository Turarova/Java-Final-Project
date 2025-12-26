# Online Shop Backend

## Overview
Simple online shop backend built with Spring Boot, JPA, MySQL, and JWT authentication.  
Features include user registration/login, role-based access (USER and ADMIN), product/category management, search & filtering, reviews, and shopping cart.

## API Endpoints

### Authentication (Public)
| Method | URL                          | Body / Params                          | Description                  |
|--------|------------------------------|----------------------------------------|------------------------------|
| POST   | `/api/auth/register`         | JSON: `{ "email", "password" }`        | Register new user (default role: USER) |
| POST   | `/api/auth/login`            | JSON: `{ "email", "password" }`        | Login → returns JWT token    |

### User Profile (Authenticated)
| Method | URL                          | Body                                   | Description                  |
|--------|------------------------------|----------------------------------------|------------------------------|
| GET    | `/api/user/profile`          | -                                      | Get current user's profile   |
| PUT    | `/api/user/profile`          | JSON: `{ "email", "photoUrl" }` (optional) | Update profile               |

### Products
| Method | URL                          | Body / Params                          | Description                  | Access       |
|--------|------------------------------|----------------------------------------|------------------------------|--------------|
| GET    | `/api/products`              | ?page=0&size=10                        | Get paged products           | Public       |
| GET    | `/api/products/{id}`         | -                                      | Get single product           | Public       |
| GET    | `/api/products/search`       | ?name=...&categoryId=...&minPrice=...&maxPrice=...&page=0&size=10 | Search & filter products     | Public       |
| POST   | `/api/products`              | JSON: `{ "name", "price", "description", "photoUrl", "categoryId" }` | Create product               | **Admin only** |
| PUT    | `/api/products/{id}`         | Same as create                         | Update product               | **Admin only** |
| DELETE | `/api/products/{id}`         | -                                      | Delete product               | **Admin only** |

### Categories
| Method | URL                          | Body                                   | Description                  | Access       |
|--------|------------------------------|----------------------------------------|------------------------------|--------------|
| GET    | `/api/products/categories`   | -                                      | Get all categories           | Public       |
| POST   | `/api/products/categories`   | JSON: `{ "name" }`                     | Create category              | **Admin only** |

### Reviews
| Method | URL                          | Body                                   | Description                  | Access       |
|--------|------------------------------|----------------------------------------|------------------------------|--------------|
| GET    | `/api/reviews/product/{productId}` | -                                      | Get reviews for product      | Public       |
| POST   | `/api/reviews`               | JSON: `{ "productId", "text", "rating" }` | Add review                   | Authenticated |

### Cart (Authenticated)
| Method | URL                          | Params                                 | Description                  | Access       |
|--------|------------------------------|----------------------------------------|------------------------------|--------------|
| GET    | `/api/cart`                  | -                                      | Get user's cart              | Authenticated |
| POST   | `/api/cart/add`              | ?productId=1&quantity=2                | Add/update item in cart      | Authenticated |
| DELETE | `/api/cart/remove`           | ?productId=1                           | Remove item from cart        | Authenticated |
| GET    | `/api/cart/total`            | -                                      | Get cart total price         | Authenticated |

## Security Notes
- Protected endpoints require header: `Authorization: Bearer <JWT-token>`
- **Admin** role (`roles = 'ADMIN'` in DB) required for creating/updating/deleting products and creating categories.
- Regular **users** can search/view products, add to cart, write reviews, and manage profile.
- **Guests** can only view/search products, categories, and reviews.

## Setup
1. Create MySQL database `onlineshop`.
2. Update `application.properties` if needed (credentials, port).
3. Run `./gradlew bootRun`.
4. Use Postman or curl to test endpoints.