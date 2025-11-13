-- HosDoc Authentication Service Database Schema
-- Database: hosdoc
-- Table: hosdoc_auth_user

CREATE DATABASE IF NOT EXISTS hosdoc;
USE hosdoc;

-- Drop table if exists (for development only)
-- DROP TABLE IF EXISTS hosdoc_auth_user;

-- Create hosdoc_auth_user table
CREATE TABLE IF NOT EXISTS hosdoc_auth_user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    user_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_role (role),
    INDEX idx_user_id (user_id),
    CONSTRAINT chk_role CHECK (role IN ('DOCTOR', 'PATIENT', 'ADMIN'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Sample data (passwords are BCrypt hashed - 'password123' hashed with 12 rounds)
-- Note: These are example hashes. In production, use actual BCrypt hashes from the application.
INSERT INTO hosdoc_auth_user (username, password, role, user_id) VALUES
('admin', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GZ1s3XpJY5N2', 'ADMIN', 1);

-- Query to view table structure
DESCRIBE hosdoc_auth_user;

-- Query to view all users (without showing passwords)
SELECT id, username, role, user_id, created_at, updated_at 
FROM hosdoc_auth_user;




