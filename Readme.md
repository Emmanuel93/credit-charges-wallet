Aquí tienes un README.md actualizado y profesional, acorde a tus últimos cambios y peticiones. Lo estructuré para que incluya toda la información relevante, ejemplos CURL, y notas útiles.

````markdown
# Credit Charges Wallet

Microservicio Spring Boot para gestión de cargos con tabla de amortización, usando PostgreSQL y DTOs para comunicación.

---

## 🚀 Ejecución local (con y sin Docker)

### 📦 Variables de entorno soportadas

| Variable              | Descripción                                              | Valor por defecto             |
|-----------------------|---------------------------------------------------------|------------------------------|
| `SERVER_PORT`          | Puerto en el que corre el servicio                       | 8080                         |
| `SPRING_DATASOURCE_URL`| URL de conexión a la base de datos PostgreSQL           | jdbc:postgresql://localhost:5432/credit_charges |
| `SPRING_DATASOURCE_USERNAME` | Usuario de la base de datos                         | postgres                     |
| `SPRING_DATASOURCE_PASSWORD` | Password de la base de datos                         | postgres                     |
| `JAVA_OPTS`            | Opciones JVM adicionales (memoria, debug, etc.)          | *(vacío)*                    |

---

### 🚦 Ejecución **SIN Docker**

1. **Requisitos:** Java 17+, Maven, PostgreSQL en local o remoto.

2. **Compilar:**
    ```bash
    mvn clean package
    ```

3. **Ejecutar PostgreSQL localmente** (o usa Docker Compose, ver abajo).

4. **Ejecutar el microservicio:**
    ```bash
    java -jar target/credit-charges-wallet.jar
    ```

5. Puedes pasar propiedades en línea o vía archivo `application.yml`.

---

### 🚦 Ejecución **CON Docker**

1. **Construir la imagen:**
    ```bash
    docker build -f docker/Dockerfile -t credit-charges-wallet .
    ```

2. **Ejecutar contenedor junto a PostgreSQL con Docker Compose:**

   ```bash
   docker-compose up -d
````

*(Incluye PostgreSQL configurado en docker-compose.yml)*

3. **Ejecutar la aplicación:**

   ```bash
   docker run -p 8080:8080 \
      --network <network-name> \
      -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/credit_charges \
      -e SPRING_DATASOURCE_USERNAME=postgres \
      -e SPRING_DATASOURCE_PASSWORD=postgres \
      credit-charges-wallet
   ```

---

## 🌐 Endpoints principales

| Método | Ruta                                         | Descripción                                         |
| ------ | -------------------------------------------- | --------------------------------------------------- |
| GET    | `/v1/prestamo/{walletId}`                    | Obtener nuevo UUID de transacción para idempotencia |
| POST   | `/v1/api/charges/{transactionId}`            | Crear cargo asociado a transacción UUID             |
| POST   | `/v1/api/charges/compensate/{transactionId}` | Compensar cargo existente                           |

---

## 🧾 Ejemplos Curl

### 1️⃣ Obtener TransactionId

```bash
curl -X GET http://localhost:8080/v1/prestamo/wallet-123 \
  -H "Authorization: Bearer <Cognito_Access_Token>"
```

**Respuesta esperada:**

```json
{
  "transactionId": "c839b41c-8728-4e44-a98e-9bb2db1ef8d1"
}
```

---

### 2️⃣ Crear cargo con transactionId generado

```bash
curl -X POST http://localhost:8080/v1/api/charges/c839b41c-8728-4e44-a98e-9bb2db1ef8d1 \
  -H "Authorization: Bearer <Cognito_Access_Token>" \
  -H "Content-Type: application/json" \
  -d '{
    "walletId": "wallet-123",
    "transactionId": "c839b41c-8728-4e44-a98e-9bb2db1ef8d1",
    "amount": 10000,
    "months": 12,
    "firstDueDate": "2024-08-01T00:00:00",
    "annualRate": 0.18,
    "amortizationType": "FRANCESA",
    "createdAt": "2024-07-09T01:00:00",
    "updatedAt": "2024-07-09T01:00:00"
  }'
```

**Respuesta esperada:**

```json
{
  "chargeId": "448f9e7f-39d1-444d-bda5-b0f382ce8e2d",
  "transactionId": "c839b41c-8728-4e44-a98e-9bb2db1ef8d1",
  "status": "GENERADO",
  "createdAt": "2025-07-09T03:04:09.487496",
  "updatedAt": "2025-07-09T03:04:09.487496",
  "amortizationTable": [
    {
      "detailId": "91058515-5b60-4a56-8efa-f0bd2eaeed16",
      "monthNumber": 1,
      "dueDate": "2024-08-01T00:00:00",
      "paymentAmount": 916.80,
      "createdAt": "2025-07-09T03:04:09.487496",
      "updatedAt": "2025-07-09T03:04:09.487496"
    }
  ]
}
```

---

### 3️⃣ Compensar cargo

```bash
curl -X POST http://localhost:8080/v1/api/charges/compensate/c839b41c-8728-4e44-a98e-9bb2db1ef8d1 \
  -H "Authorization: Bearer <Cognito_Access_Token>"
```

**Respuesta esperada:**

```json
{
  "chargeId": "448f9e7f-39d1-444d-bda5-b0f382ce8e2d",
  "transactionId": "c839b41c-8728-4e44-a98e-9bb2db1ef8d1",
  "status": "CANCELADO",
  "createdAt": "2025-07-09T03:04:09.487496",
  "updatedAt": "2025-07-09T04:00:00.123456",
  "amortizationTable": [...]
}
```

---

## 🧪 Pruebas

* Tests unitarios y de integración con JUnit 5 y Mockito en `src/test/java`.
* Incluyen validación de creación, compensación y manejo de errores.

---

## 🗂 Estructura relevante del proyecto

```
docker/
  Dockerfile
  docker-compose.yml
src/
  main/
    java/spin/cedit/creditchargeswallet/
      application/
      domain/
      interfaces/
        controller/
      infrastructure/
        jpa/
        ...
    resources/
      application.yml
README.md
```

---

## 📋 Notas adicionales

* Usar Java 17 y Spring Boot 3 para compatibilidad total.
* PostgreSQL es la base relacional para persistencia.
* Docker Compose levanta PostgreSQL local para pruebas.
* Validación robusta con Jakarta Validation en DTOs.
* DTOs usados para desacoplar capa de dominio y API.


