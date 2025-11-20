# hosdoc_auth_user Table Structure

## Table Name
`hosdoc_auth_user`

## Table Description
Stores user authentication information for the HosDoc application. This table contains credentials and role-based access control data for doctors, patients, and administrators.

## Column Details

| Column Name | Data Type | Constraints | Description |
|------------|-----------|-------------|-------------|
| `id` | INT | PRIMARY KEY, AUTO_INCREMENT, NOT NULL | Unique identifier for each user record |
| `username` | VARCHAR(255) | NOT NULL, UNIQUE | Username for login (should be unique) |
| `password` | VARCHAR(255) | NOT NULL | BCrypt hashed password (not plain text) |
| `role` | VARCHAR(50) | NOT NULL, ENUM('DOCTOR','PATIENT','ADMIN') | User role defining access level |
| `user_id` | BIGINT | NULL | Foreign reference to Doctor ID or Patient ID in respective tables |

## Indexes
- **Primary Key**: `id` (auto-increment)
- **Unique Index**: `username` (ensures unique usernames)

## Sample Data

```sql
INSERT INTO hosdoc_auth_user (username, password, role, user_id) VALUES
('admin', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GZ1s3XpJY5N2', 'ADMIN', 1),
('doctor1', '$2a$12$xyz123...', 'DOCTOR', 101),
('patient1', '$2a$12$abc456...', 'PATIENT', 201);
```

## Constraints
1. **Username**: Must be unique across all records
2. **Password**: Must be hashed using BCrypt (12 rounds)
3. **Role**: Must be one of: DOCTOR, PATIENT, ADMIN
4. **User ID**: Can be NULL or reference a Doctor/Patient ID

## Relationships
- `user_id` can reference:
  - Doctor table (when `role = 'DOCTOR'`)
  - Patient table (when `role = 'PATIENT'`)
  - Admin table (when `role = 'ADMIN'`) or can be NULL

## Security Notes
- Passwords are never stored in plain text
- BCrypt hashing with 12 rounds ensures secure password storage
- Role-based access control is enforced at the application level

## Entity Mapping (JPA)
- **Entity Class**: `com.kalex.hosdoc_auth.entity.User`
- **Table Annotation**: `@Table(name = "hosdoc_auth_user")`
- **Primary Key**: `@Id @GeneratedValue(strategy = GenerationType.IDENTITY)`
- **Enum Mapping**: `@Enumerated(EnumType.STRING)` for role field








