# HosDoc Auth Service - API Endpoints

The auth service works similar to employeewellness-Auth with the following endpoints:

## Base URL
```
http://localhost:8081/auth
```

## Endpoints

### 1. Register User
**POST** `/auth/register`

Register a new user.

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "password123",
  "role": "USER",
  "userId": 123
}
```

**Response:**
```json
{
  "id": 1,
  "username": "john_doe",
  "role": "USER",
  "userId": 123,
  "createdAt": "2025-01-15T10:30:00",
  "updatedAt": "2025-01-15T10:30:00"
}
```

### 2. Login
**POST** `/auth/login?username={username}&password={password}`

Authenticate user and get JWT token.

**Query Parameters:**
- `username` (required): Username
- `password` (required): Password

**Response:**
```
JWT_TOKEN_STRING
```

**Example:**
```
POST /auth/login?username=john_doe&password=password123
Response: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huX2RvZSIsInJvbGUiOiJVU0VSIiwidXNlcklkIjoxMjMsImV4cCI6MTcwNTI4MDAwMCwiaWF0IjoxNzA1MjQ0MDAwfQ...
```

### 3. Validate Token
**POST** `/auth/validate`

Validate a JWT token.

**Headers:**
```
Authorization: Bearer {token}
```

**Response:**
- `200 OK` - Token is valid
- `401 Unauthorized` - Token is invalid or expired

### 4. Get Current User
**GET** `/auth/me`

Get current authenticated user details.

**Headers:**
```
Authorization: Bearer {token}
```

**Response:**
```json
{
  "id": 1,
  "username": "john_doe",
  "role": "USER",
  "userId": 123,
  "createdAt": "2025-01-15T10:30:00",
  "updatedAt": "2025-01-15T10:30:00"
}
```

### 5. Get All Users
**GET** `/auth/users`

Get list of all users (requires authentication).

**Response:**
```json
[
  {
    "id": 1,
    "username": "john_doe",
    "role": "USER",
    "userId": 123
  },
  {
    "id": 2,
    "username": "jane_doe",
    "role": "DOCTOR",
    "userId": 456
  }
]
```

### 6. Get User By ID
**GET** `/auth/users/{id}`

Get user by ID.

**Path Parameters:**
- `id` (required): User ID

**Response:**
```json
{
  "id": 1,
  "username": "john_doe",
  "role": "USER",
  "userId": 123
}
```

### 7. Ping (Admin Only)
**GET** `/auth/ping`

Test endpoint (requires ADMIN role).

**Headers:**
```
Authorization: Bearer {admin_token}
```

**Response:**
```
Welcome
```

## JWT Token Structure

The JWT token contains:
- `sub`: Username
- `role`: User role (USER, DOCTOR, ADMIN)
- `userId`: User ID reference
- `iat`: Issued at timestamp
- `exp`: Expiration timestamp

## Usage Examples

### Register a new user:
```bash
curl -X POST http://localhost:8081/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test_user",
    "password": "test123",
    "role": "USER",
    "userId": 1
  }'
```

### Login:
```bash
curl -X POST "http://localhost:8081/auth/login?username=test_user&password=test123"
```

### Validate token:
```bash
curl -X POST http://localhost:8081/auth/validate \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Get current user:
```bash
curl -X GET http://localhost:8081/auth/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Role Values

- `USER` - Regular patient user
- `DOCTOR` - Doctor user
- `ADMIN` - Administrator

## Security

- All passwords are hashed using BCrypt (12 rounds)
- JWT tokens expire after 1 hour
- Token validation is done via `/auth/validate` endpoint
- CORS is enabled for all origins

