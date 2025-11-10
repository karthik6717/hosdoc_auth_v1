# HosDoc Auth Service - All CURL Commands

Base URL: `http://localhost:8081/api/auth`

---

## 1. Register User

**Endpoint:** `POST /api/auth/register`

**Request:**
```bash
curl --location --request POST 'http://localhost:8081/api/auth/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "john_doe",
    "password": "password123",
    "role": "USER",
    "userId": 123
}'
```

**Success Response (201):**
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

**Error Response (409) - Username Already Exists:**
```json
{
    "status": 409,
    "error": "Conflict",
    "message": "User with username 'john_doe' already exists",
    "path": "/api/auth/register"
}
```

---

## 2. Login

**Endpoint:** `POST /api/auth/login`

**Request:**
```bash
curl --location --request POST 'http://localhost:8081/api/auth/login?username=john_doe&password=password123'
```

**Alternative (URL encoded):**
```bash
curl --location --request POST 'http://localhost:8081/api/auth/login?username=john_doe%40gmail.com&password=Karthik%40123'
```

**Success Response:**
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huX2RvZSIsInJvbGUiOiJVU0VSIiwidXNlcklkIjoxMjMsImV4cCI6MTcwNTI4MDAwMCwiaWF0IjoxNzA1MjQ0MDAwfQ...
```

**Error Response (403):**
```json
{
    "timestamp": "2025-01-15T10:30:00.000+00:00",
    "status": 403,
    "error": "Forbidden",
    "message": "Access Denied",
    "path": "/api/auth/login"
}
```

---

## 3. Validate Token

**Endpoint:** `POST /api/auth/validate`

**Request:**
```bash
curl --location --request POST 'http://localhost:8081/api/auth/validate' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huX2RvZSIsInJvbGUiOiJVU0VSIiwidXNlcklkIjoxMjMsImV4cCI6MTcwNTI4MDAwMCwiaWF0IjoxNzA1MjQ0MDAwfQ...'
```

**Success Response (200):**
```
(Empty body - 200 OK status)
```

**Error Response (401):**
```
(Empty body - 401 Unauthorized status)
```

---

## 4. Get Current User (Me)

**Endpoint:** `GET /api/auth/me`

**Request:**
```bash
curl --location --request GET 'http://localhost:8081/api/auth/me' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huX2RvZSIsInJvbGUiOiJVU0VSIiwidXNlcklkIjoxMjMsImV4cCI6MTcwNTI4MDAwMCwiaWF0IjoxNzA1MjQ0MDAwfQ...'
```

**Success Response (200):**
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

**Error Response (401):**
```
(Empty body - 401 Unauthorized status)
```

---

## 5. Get All Users

**Endpoint:** `GET /api/auth/users`

**Request:**
```bash
curl --location --request GET 'http://localhost:8081/api/auth/users'
```

**Success Response (200):**
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

---

## 6. Get User By ID

**Endpoint:** `GET /api/auth/users/{id}`

**Request:**
```bash
curl --location --request GET 'http://localhost:8081/api/auth/users/1'
```

**Success Response (200):**
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

**Error Response (417):**
```json
{
    "timestamp": "2025-01-15T10:30:00.000+00:00",
    "status": 417,
    "error": "Expectation Failed",
    "message": "Invalid Id",
    "path": "/api/auth/users/999"
}
```

---

## 7. Ping (Admin Only)

**Endpoint:** `GET /api/auth/ping`

**Request:**
```bash
curl --location --request GET 'http://localhost:8081/api/auth/ping' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsInVzZXJJZCI6MSwiZXhwIjoxNzA1MjgwMDAwLCJpYXQiOjE3MDUyNDQwMDB9...'
```

**Success Response (200):**
```
Welcome
```

**Error Response (403) - If not admin:**
```json
{
    "timestamp": "2025-01-15T10:30:00.000+00:00",
    "status": 403,
    "error": "Forbidden",
    "message": "Access Denied",
    "path": "/api/auth/ping"
}
```

---

## Complete Workflow Example

### Step 1: Register a new user
```bash
curl --location --request POST 'http://localhost:8081/api/auth/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "testuser",
    "password": "test123",
    "role": "USER",
    "userId": 1
}'
```

### Step 2: Login to get JWT token
```bash
curl --location --request POST 'http://localhost:8081/api/auth/login?username=testuser&password=test123'
```

**Save the token from response:**
```bash
TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInJvbGUiOiJVU0VSIiwidXNlcklkIjoxLCJleHAiOjE3MDUyODAwMDAsImlhdCI6MTcwNTI0NDAwMH0..."
```

### Step 3: Validate the token
```bash
curl --location --request POST 'http://localhost:8081/api/auth/validate' \
--header "Authorization: Bearer $TOKEN"
```

### Step 4: Get current user info
```bash
curl --location --request GET 'http://localhost:8081/api/auth/me' \
--header "Authorization: Bearer $TOKEN"
```

### Step 5: Get all users
```bash
curl --location --request GET 'http://localhost:8081/api/auth/users'
```

---

## Role Values

- `USER` - Regular patient user
- `DOCTOR` - Doctor user  
- `ADMIN` - Administrator

---

## Common Issues

### 1. Username Already Exists
**Error:** `409 Conflict`
**Solution:** Use a different username or check existing users

### 2. Invalid Credentials
**Error:** `403 Forbidden`
**Solution:** Verify username and password are correct

### 3. Token Expired
**Error:** `401 Unauthorized`
**Solution:** Login again to get a new token (tokens expire after 1 hour)

### 4. Missing Authorization Header
**Error:** `401 Unauthorized`
**Solution:** Include `Authorization: Bearer {token}` header

---

## PowerShell Alternative (Windows)

### Register:
```powershell
Invoke-RestMethod -Uri "http://localhost:8081/api/auth/register" `
  -Method Post `
  -ContentType "application/json" `
  -Body '{"username":"testuser","password":"test123","role":"USER","userId":1}'
```

### Login:
```powershell
Invoke-RestMethod -Uri "http://localhost:8081/api/auth/login?username=testuser&password=test123" `
  -Method Post
```

### Get Current User:
```powershell
$token = "YOUR_JWT_TOKEN"
Invoke-RestMethod -Uri "http://localhost:8081/api/auth/me" `
  -Method Get `
  -Headers @{Authorization="Bearer $token"}
```

---

## Notes

- All endpoints support CORS (`*`)
- JWT tokens expire after **1 hour**
- Passwords are hashed using **BCrypt (12 rounds)**
- Username must be **unique** in the database
- The `/ping` endpoint requires **ADMIN** role

