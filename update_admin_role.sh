#!/bin/bash

# Update user role in PostgreSQL
docker exec -i business-service-business-postgres-1 psql -U business_user -d business_db << EOF
UPDATE users SET role = 'ADMIN' WHERE name = 'admin';
SELECT id, name, role FROM users WHERE name = 'admin';
EOF

# Clear Redis cache for the admin user
docker exec -i business-service-business-redis-1 redis-cli DEL "business:user::1"

echo "Script executed to update admin role to ADMIN and cleared Redis cache for this user.
The following credentials are the default for the development environment:
  name = admin
  password = adminPassword123!
"