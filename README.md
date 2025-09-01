# MIALEDS - Sistema de Control de Inventario y Ventas

> Una solución integral para que los **dueños de pequeñas empresas** tomen el control de su inventario, ventas y proveedores. Este proyecto está construido con un backend robusto en Spring Boot, enfocado en la seguridad y la integridad de los datos.

---

## ⭐ Característica Destacada: Control de Acceso por Roles

La funcionalidad más importante de este sistema es su estricto control de seguridad basado en roles. Esto garantiza que cada empleado solo pueda acceder a las áreas que le corresponden, separando las responsabilidades y protegiendo la información crítica del negocio.

- **Rol de Administrador:** Acceso total al sistema.
- **Rol de Usuario (Empleado):** Acceso limitado a los módulos de venta y consulta.

---

## 🛠️ Stack Tecnológico

### Backend
- **Lenguaje:** Java 21
- **Framework:** Spring Boot
- **Seguridad:** Spring Security (Autenticación por formulario y roles)
- **Acceso a Datos:** Spring Data JPA / Hibernate
- **Build Tool:** Maven

### Frontend
- **Framework CSS:** Materialize CSS
- **JavaScript:**
  - **Librerías:** jQuery, Chart.js
  - **Técnicas:** AJAX para comunicación asíncrona
- **Motor de Plantillas:** Thymeleaf

### Base de Datos
- **Motor:** MySQL

### DevOps & Despliegue
- **Contenerización:** Docker & Docker Compose

### Pruebas
- **Unitarias y de Integración:** JUnit 5 & Mockito
- **Cobertura de Código:** JaCoCo

---

## 🐳 Cómo Empezar con Docker (Recomendado)

Esta es la forma más sencilla y rápida de levantar todo el entorno (aplicación + base de datos) con un solo comando.

### 1. Prerrequisitos
- Docker
- Docker Compose

### 2. Instalación

1.  **Clona el repositorio.**
    ```bash
    git clone https://github.com/tu-usuario/tu-repositorio.git
    cd mialeds-control-de-inventario
    ```

2.  **Crea tu archivo de entorno:**
    - Copia el archivo `.env.example` y renómbralo a `.env`.
    - Abre el nuevo archivo `.env` y rellena tus credenciales de correo de Gmail.

3.  **Levanta los servicios:**
    ```bash
    # El comando --build es necesario solo la primera vez o si haces cambios en el código
    docker-compose up --build -d
    ```

¡Y listo! La aplicación estará corriendo en `http://localhost:8080`.

---

## 🚀 Instalación Manual (Alternativa)

### 1. Prerrequisitos
- JDK 21
- Maven 3+
- MySQL 8+

### 2. Instalación

1.  **Clona el repositorio.**
2.  **Crea la base de datos** en MySQL: `CREATE DATABASE mialeds_db;`
3.  **Configura las credenciales** en `src/main/resources/application.properties` (puedes copiar el archivo `.example`).
4.  **Ejecuta la aplicación** con Maven: `mvn spring-boot:run`.

### 3. Acceso a la Aplicación

- **URL:** `http://localhost:8080`
- **Usuario Administrador:**
  - **Usuario (cédula):** `123456789`
  - **Contraseña:** `admin123`
- **Usuario Empleado:**
  - **Usuario (cédula):** `987654321`
  - **Contraseña:** `user123`

---

## 🚧 Estado del Proyecto y Futuro

Este proyecto es una **base sólida y funcional (trabajo en progreso)**. La visión a futuro es desacoplar el frontend y construir una interfaz de usuario más moderna y reactiva utilizando **Angular**.

---

## 📄 Licencia

Distribuido bajo la Licencia MIT. Ver `LICENSE` para más información.
