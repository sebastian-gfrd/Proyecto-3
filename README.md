El siguiente es el archivo `readme.txt` que describe los pasos necesarios para instalar, configurar y ejecutar la aplicación de microservicios **ALPESCAB** utilizando Spring Boot, Spring WebFlux y MongoDB.

---
# readme.txt

## Proyecto ALPESCAB: Microservicios de Transporte y Entrega

Este proyecto implementa los microservicios de la *startup* ALPESCAB para la gestión de usuarios, conductores, vehículos y viajes, utilizando Spring Boot, Spring Data MongoDB y un enfoque reactivo (Spring WebFlux) para manejar la alta concurrencia.

### 1. Requisitos Previos (Prerequisites)

Para ejecutar el proyecto, asegúrese de tener instalados los siguientes componentes:

*   **Java Development Kit (JDK):** Versión 17 o superior, ya que las versiones modernas de Spring Boot requieren al menos Java 17.
*   **Gestor de Dependencias:** **Maven** o **Gradle**.
*   **Base de Datos:** Un servidor de **MongoDB** en ejecución. Puede ser una instancia local (por defecto en `localhost:27017`), un contenedor Docker, o un clúster MongoDB Atlas (el clúster gratuito M0 es suficiente para pruebas).
*   **Herramientas de Testing:** Postman o `curl` para probar los *endpoints* REST.
*   **Docker:** Para la contenedorización del microservicio (paso esencial si se planea el despliegue en AWS ECS).
*   **AWS CLI/Consola:** Necesario si se va a realizar el despliegue en AWS Elastic Container Service (ECS).

### 2. Configuración del Proyecto (Setup)

#### 2.1. Creación e Inicialización del Proyecto
El proyecto base se crea utilizando **Spring Initializr**, seleccionando:
1.  **Tipo:** Maven Project.
2.  **Lenguaje:** Java.
3.  **Versión de Spring Boot:** Última versión estable (e.g., 3.1.4 en los ejemplos).
4.  **Dependencias Esenciales:**
    *   **Spring Web** (o **Spring Reactive Web / WebFlux**).
    *   **Spring Data MongoDB**.
    *   **OAuth2 Resource Server** (incluye Spring Security y componentes JWT para endpoints seguros).
    *   **Validation** (para DTOs).

#### 2.2. Configuración de Conexión a MongoDB
La conexión a la base de datos se define en el archivo `application.properties`:

```properties
# Configuración por defecto si no se especifica
# spring.data.mongodb.host=localhost
# spring.data.mongodb.port=27017

# URI de Conexión (ejemplo para Atlas o entorno externo)
spring.data.mongodb.uri=${MONGODB_URI}
spring.data.mongodb.database=alpescab_db 
```
Si se utilizan colecciones embebidas y se requiere atomicidad o transacciones (como en RF4/RF5/RF6/RF7), el servicio debe inyectar y utilizar **`MongoTemplate`** o **`ReactiveMongoTemplate`** para un control granular sobre las operaciones. Para usar `@Transactional` en el modo síncrono, se debe registrar explícitamente el `MongoTransactionManager`.

#### 2.3. Configuración de Seguridad (JWT)
Si se implementa la seguridad (como en los ejemplos de las fuentes), se requiere:
1.  **Generación de Claves RSA:** Crear los archivos `private.pem` y `public.pem` en la ruta `src/main/resources/certs/` para la firma asimétrica de los tokens (RS256).
2.  **Binding de Propiedades:** Configurar las propiedades para que Spring pueda acceder a las claves:
    ```properties
    rsa.private-key=classpath:certs/private.pem
    rsa.public-key=classpath:certs/public.pem
    ```
3.  **Implementación de Seguridad:** Implementar `MongoUserDetailsService` para cargar los datos del usuario (`username`, *password hash*, `roles`) desde MongoDB y configurar el `SecurityConfig` para exponer `/auth/register` y `/auth/token` sin autenticación. Se debe usar **`BCryptPasswordEncoder`** para asegurar las contraseñas.

### 3. Inicialización de la Base de Datos

El proyecto utiliza un modelo de base de datos documental con las siguientes colecciones principales:
*   `Ciudades`
*   `Usuarios` (contiene subdocumentos `metodosPago`)
*   `Conductores`
*   `Vehiculos` (contiene subdocumentos `disponibilidad`)
*   `Tarifas`
*   `Viajes` (contiene subdocumentos `paradas`)
*   `Calificaciones`

**Pasos:**
1.  Utilice los *scripts* de creación de colecciones (`db.createCollection(...)`) y los esquemas de validación (`$jsonSchema`) proporcionados para asegurar la estructura de los documentos, incluyendo los arrays embebidos como `disponibilidad` y `metodosPago`.
2.  Pueble la base de datos con datos suficientes para probar las consultas RFC1 y RFC2 (histórico de servicios y top 20 conductores).

### 4. Ejecución Local del Microservicio

Para ejecutar la aplicación localmente (asumiendo que MongoDB está disponible):
1.  Compile el proyecto (e.g., `mvn clean install` o `./gradlew build`).
2.  Ejecute la aplicación (e.g., `java -jar target/app-name.jar` o desde su IDE).
3.  El servicio estará disponible en el puerto definido (por defecto 8080 o 8081 en los ejemplos).

### 5. Despliegue en AWS ECS con Fargate (Serverless)

Para el despliegue en un entorno de alta disponibilidad como AWS ECS, se sigue el siguiente flujo de trabajo, que aísla el microservicio de Spring Boot y la base de datos MongoDB en contenedores separados:

#### 5.1. Containerización y Repositorio Docker
1.  **Creación del Dockerfile:** Defina el `Dockerfile` para la aplicación Spring Boot, especificando el JDK base (e.g., `open-jdk8`), copiando el JAR generado y exponiendo el puerto del microservicio (e.g., 8081).
2.  **Creación de la Imagen:** Construya la imagen localmente (e.g., `docker build -t your-repo/app-name:tag .`).
3.  **Carga a Docker Hub:** Suba la imagen a un repositorio público o privado en Docker Hub (o AWS ECR, no mencionado) (e.g., `docker push your-repo/app-name:tag`).

#### 5.2. Configuración en AWS ECS
1.  **Cluster:** Cree un Cluster ECS (se recomienda usar **Fargate** como arquitectura *serverless* para no preocuparse por la infraestructura EC2).
2.  **Task Definition (MongoDB):** Cree una definición de tarea utilizando la imagen oficial de MongoDB de Docker Hub (o una imagen custom si es necesario), mapeando el puerto **27017**.
3.  **Task Definition (Microservicio):** Cree una segunda definición de tarea utilizando la imagen del microservicio Spring Boot subida a Docker Hub, mapeando el puerto **8081**.
4.  **Security Group:** Edite el Grupo de Seguridad asociado a las tareas para añadir reglas de entrada (Inbound Rules) que permitan el tráfico:
    *   Puerto **27017** (TCP) para la conexión a MongoDB.
    *   Puerto **8081** (TCP) para acceder al API REST del microservicio.
5.  **Ejecución:** Ejecute las definiciones de tarea dentro del cluster.

### 6. Pruebas de Endpoints y Servicios

Una vez que el microservicio esté en ejecución (localmente o en ECS, accesible por su IP pública), puede probar los siguientes *endpoints* clave:

| RF/Funcionalidad | Método | Ruta (Ejemplo) | Descripción |
| :--- | :--- | :--- | :--- |
| **RF1 / RF2** | `POST` | `/auth/register` | Registra un nuevo usuario (de servicio o conductor), requiere `username` y `password`. |
| **Autenticación** | `POST` | `/auth/token` | Intercambia `username` y `password` por un **JSON Web Token (JWT)** firmado (RS256). |
| **Seguridad** | `GET` | `/api/secure/hello` | Endpoint protegido. Requiere el JWT en el encabezado `Authorization: Bearer <token>`. |
| **RF6** | `POST` | `/api/viajes/solicitar` | Solicita un servicio y activa la lógica de asignación (cambio atómico de estado del conductor). |
| **RF7** | `PUT` | `/api/viajes/{id}/finalizar`| Marca el viaje como terminado y libera atómicamente el estado del conductor. |
| **RF4/RF5** | `PUT/POST` | `/api/vehiculos/{id}/disponibilidad` | Gestiona la disponibilidad del vehículo, requiere validación de superposición horaria y actualización atómica del array embebido. |

**Ejemplo de prueba de seguridad con `curl`**:
1. Obtener Token:
   ```bash
   TOKEN=$(curl -s -X POST http://localhost:8080/auth/token \
   -H "Content-Type: application/json" \
   -d '{"username":"tim","password":"secret"}' | sed -E 's/.*"token":"([^"]+)".*/\1/')
   ```
2. Acceder a ruta segura:
   ```bash
   curl -i http://localhost:8080/api/secure/hello \
   -H "Authorization: Bearer $TOKEN"
   ```
El microservicio y MongoDB en contenedores se comunican exitosamente a través de una red puente (`bridge Network`).
