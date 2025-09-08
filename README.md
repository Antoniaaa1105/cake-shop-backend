# 🍰 Cake Shop Backend

Backend pentru aplicația web a unei cofetării, realizat cu **Spring Boot** și **PostgreSQL (Supabase)**.  
Asigură gestionarea produselor și autentificarea utilizatorilor.

---

## 🛠️ Tehnologii
- Java 17, Spring Boot 3  
- Spring Security + JWT  
- PostgreSQL (Supabase)  
- Maven  

---

## 🚀 Rulare proiect

1. Clonează repository:
   ```bash
   git clone https://github.com/USERNAME/cake-shop-backend.git
2.Deschide proiectul în IntelliJ IDEA (Maven project).

3.Setează datele de conectare în application.properties.

4.Rulează aplicația:

mvn spring-boot:run

📌 Endpoint-uri

POST /api/auth/register – înregistrare

POST /api/auth/login – autentificare

GET /api/products – listă produse

POST /api/products – adaugă produs (admin)

