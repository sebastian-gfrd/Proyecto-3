# ALPESCAB Microservices - Sistema Transaccional Reactivo

Sistema de microservicios reactivo para la gestión de viajes de ride-hailing de ALPESCAB (Entrega 3).

## 🚀 Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring WebFlux** - Framework reactivo non-blocking
- **Spring Data MongoDB Reactive** - Acceso reactivo a MongoDB
- **Lombok** - Reducción de código boilerplate
- **Jakarta Validation** - Validación de datos
- **Maven** - Gestión de dependencias

## 📁 Estructura del Proyecto

```
src/main/java/com/alpescab/
├── AlpescabApplication.java       # Clase principal de la aplicación
├── model/
│   └── Viajes.java                # Modelo de documento MongoDB
├── repository/
│   └── ViajesRepository.java      # Repositorio reactivo
├── service/
│   └── ViajesService.java         # Lógica de negocio
├── controller/
│   └── ViajesController.java      # Controlador REST
└── config/
    └── MongoConfig.java           # Configuración de MongoDB
```

## 🔧 Configuración

### Requisitos Previos

1. Java 17 o superior
2. Maven 3.6+
3. MongoDB 4.4+ (ejecutándose en localhost:27017)

### Configuración de MongoDB

La aplicación se conecta a MongoDB con la siguiente configuración (en `application.properties`):

```properties
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=alpescab_db
```

## 🏃 Ejecución

### Compilar el proyecto

```bash
mvn clean install
```

### Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación se ejecutará en `http://localhost:8080`

## 📡 Endpoints API

### Viajes

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/viajes` | Obtener todos los viajes |
| GET | `/api/viajes/{id}` | Obtener viaje por ID |
| POST | `/api/viajes` | Crear nuevo viaje |
| PUT | `/api/viajes/{id}` | Actualizar viaje |
| DELETE | `/api/viajes/{id}` | Eliminar viaje |
| GET | `/api/viajes/conductor/{conductorId}` | Obtener viajes por conductor |
| GET | `/api/viajes/pasajero/{pasajeroId}` | Obtener viajes por pasajero |
| GET | `/api/viajes/estado/{estado}` | Obtener viajes por estado |

### Ejemplo de Petición POST

```json
{
  "conductorId": "COND001",
  "pasajeroId": "PAS001",
  "origenUbicacion": "Calle 123, Bogotá",
  "destinoUbicacion": "Carrera 45, Bogotá",
  "fechaInicio": "2025-11-30T17:00:00",
  "estado": "SOLICITADO",
  "tarifa": 15000.0,
  "distanciaKm": 5.2
}
```

### Estados de Viaje

- `SOLICITADO` - Viaje solicitado por el pasajero
- `ACEPTADO` - Viaje aceptado por el conductor
- `EN_CURSO` - Viaje en progreso
- `COMPLETADO` - Viaje finalizado
- `CANCELADO` - Viaje cancelado

## 🔄 Arquitectura Reactiva

Este proyecto implementa una arquitectura completamente reactiva (non-blocking) utilizando:

- **Flux<T>**: Para streams de múltiples elementos (listas)
- **Mono<T>**: Para streams de un solo elemento o vacío

### Ventajas de la Arquitectura Reactiva

1. **Alto rendimiento**: Manejo eficiente de múltiples peticiones concurrentes
2. **Escalabilidad**: Mejor uso de recursos del sistema
3. **Non-blocking**: No bloquea hilos mientras espera respuestas de I/O
4. **Backpressure**: Control de flujo de datos entre productor y consumidor

## 📝 Modelo de Datos

### Viajes

```java
{
  "id": "String",
  "conductorId": "String (requerido)",
  "pasajeroId": "String (requerido)",
  "origenUbicacion": "String (requerido)",
  "destinoUbicacion": "String (requerido)",
  "fechaInicio": "LocalDateTime (requerido)",
  "fechaFin": "LocalDateTime",
  "estado": "EstadoViaje (requerido)",
  "tarifa": "Double (positivo)",
  "distanciaKm": "Double (positivo)",
  "comentarios": "String"
}
```

## 🧪 Pruebas

### Probar con cURL

```bash
# Listar todos los viajes
curl http://localhost:8080/api/viajes

# Crear un viaje
curl -X POST http://localhost:8080/api/viajes \
  -H "Content-Type: application/json" \
  -d '{
    "conductorId": "COND001",
    "pasajeroId": "PAS001",
    "origenUbicacion": "Calle 123",
    "destinoUbicacion": "Carrera 45",
    "fechaInicio": "2025-11-30T17:00:00",
    "estado": "SOLICITADO",
    "tarifa": 15000.0,
    "distanciaKm": 5.2
  }'
```

## 📦 Dependencias Principales

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb-reactive</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
</dependencies>
```

## 👥 Autor

Proyecto desarrollado para la Entrega 3 de Sistemas Transaccionales - ALPESCAB

## 📄 Licencia

Este proyecto es parte de un trabajo académico.
