# Cofetarie - Spring Boot Application

POST http://localhost:8080/api/cart/{username}/add/{idProduct}
POST http://localhost:8080/api/cart/{username}
GET  http://localhost:8080/api/cart/{username}
exemplu test in Postman:
{
"id": 1,
"user": {
"id": 5,
"username": "user",
"email": "alex@example.com"
},
"items": [
{
"id": 3,
"productName": "Tort de ciocolată",
"quantity": 1,
"price": 100.0
}
],
"totalPrice": 100.0
}

POST  http://localhost:8080/api/products
exemplu test in Postman{
"name": "Amandina",
"description": "Amandina copilariei",
"price": 25,
"category": {
"id": 3
}
}

POST http://localhost:8080/api/auth/register
exemplu Postman:
{
"username": "testuser",
"password": "testpassword"
}

GET /api/products Obține toate produsele
GET /api/products/{id} Detalii produs

# GET /api/cart/{username} Obține conținutul coșului

# POST /api/cart/{username}/add/{id} Adaugă produs în coș

# PUT /api/cart/{username}/update/{id} Actualizează cantitatea

# DELETE /api/cart/{username}/remove/{id} Elimină produs din coș

# DELETE /api/cart/{username}/clear	Șterge conținutul coșului

# POST /api/cart/{username}/checkout	Plasează comandă

# GET /api/orders/{username} Obține comenzile utilizatorului


