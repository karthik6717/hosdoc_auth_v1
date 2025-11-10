# Fix Duplicate Usernames Issue

## Problem
The error "Query did not return a unique result: 2 results were returned" occurs because there are duplicate usernames in the database.

## Solution Applied

1. **Updated UserDao** to use `Optional<User>` and added `findFirstByUsername()` method that handles duplicates by returning the first result ordered by ID.

2. **Updated all services** to use `findFirstByUsername()` instead of `findByUsername()`.

## Database Cleanup (Recommended)

To fix the root cause, you should remove duplicate usernames from the database:

```sql
-- Find duplicate usernames
SELECT username, COUNT(*) as count 
FROM hosdoc_auth_user 
GROUP BY username 
HAVING COUNT(*) > 1;

-- Delete duplicates, keeping only the first one (lowest ID)
DELETE u1 FROM hosdoc_auth_user u1
INNER JOIN hosdoc_auth_user u2 
WHERE u1.id > u2.id AND u1.username = u2.username;

-- Verify no duplicates remain
SELECT username, COUNT(*) as count 
FROM hosdoc_auth_user 
GROUP BY username 
HAVING COUNT(*) > 1;
```

## Ensure Unique Constraint

Make sure the database has a unique constraint on username:

```sql
-- Add unique index if it doesn't exist
ALTER TABLE hosdoc_auth_user 
ADD UNIQUE INDEX idx_username_unique (username);
```

## Testing

After cleanup, test login again:
```bash
curl -X POST 'http://localhost:8081/api/auth/login?username=gkarthik@gmail.com&password=Karthik@123'
```

