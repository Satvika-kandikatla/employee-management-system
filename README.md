# Employee Management System

A full-stack Employee Management System built with:
- **Backend**: Spring Boot 3.2 + JWT Authentication + BCrypt
- **Frontend**: Angular 19 + Angular Material UI
- **Database**: MySQL
## Features
- Admin & Employee Login with JWT
- Dashboard with stats
- Employee CRUD operations
- Search & Pagination
- Role-based access control
- BCrypt password encryption

## Local Setup

### Backend
```bash
cd ems-backend
mvn spring-boot:run
```

### Frontend
```bash
cd ems-frontend
npm install
ng serve
```

## Default Credentials
- Admin: admin@company.com / password123
- Employee: alice@company.com / password123