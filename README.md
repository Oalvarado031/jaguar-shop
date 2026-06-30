# Jaguar Shop

Tienda web oficial de productos **UAM (Jaguar)**:
**Spring Boot 3 + Java 21 + Lombok + Spring Security + Thymeleaf (Layout Dialect) + PostgreSQL**.

## Arquitectura

```
src/main/java/com/jaguar_shop
├── Application.java          # clase principal
├── WebConfig.java            # sirve /uploads/productos/**
├── config/
│   ├── PasswordConfig.java   # BCryptPasswordEncoder
│   ├── SecurityConfig.java   # reglas de acceso (público / login / ADMIN)
│   └── DataInitializer.java  # siembra roles + usuario admin
├── controller/               # Home, Producto, Rol, Usuario, Auth, Pedido
├── modelo/                   # Categoria, Producto, Pedido, DetallePedido, Rol, Usuario, UsuarioRol (Lombok)
├── repository/               # *Repository extends JpaRepository
├── security/                 # JaguarUserDetailsService (login contra la BD)
├── dto/                      # CheckoutRequest
└── service/                  # interfaces
    └── impl/                 # implementaciones (@Service @RequiredArgsConstructor)

src/main/resources
├── application.properties
├── static/                   # css/jaguar.css, js/jaguar.js, images/
└── templates/
    ├── layouts/main.html     # layout con Thymeleaf Layout Dialect + sec:authorize
    ├── home.html, login.html, registro.html
    ├── productos/  (lista, formulario, detalle)
    ├── pedidos/    (lista, confirmacion)
    ├── roles/      (lista, formulario)
    └── usuarios/   (lista, formulario)
```

Convenciones:
- Paquete base `com.jaguar_shop`, carpeta de entidades `modelo/`.
- **Lombok** (`@Getter/@Setter`, `@RequiredArgsConstructor`, `@Builder`).
- Patrón **Service (interfaz) + service/impl (implementación)**.
- Vistas con **Thymeleaf Layout Dialect** (`layout:decorate` / `layout:fragment`).
- **Spring Security** + `PasswordConfig` (BCrypt) + `sec:authorize` en las vistas.
- **CRUD de productos** con subida de imágenes (`MultipartFile` → `uploads/productos/`).
- `spring.jpa.hibernate.ddl-auto=update` (Hibernate crea/actualiza las tablas).
- **Maven Wrapper** (`mvnw`).

## Requisitos
- Java 21, PostgreSQL 14+ (Maven no es necesario: usa `./mvnw`).

## Puesta en marcha

1. Crear la base de datos:
   ```sql
   CREATE DATABASE jaguar_shop;
   ```

2. (Opcional) Cargar datos iniciales de catálogo:
   ```bash
   psql -U postgres -d jaguar_shop -f src/main/resources/db/schema.sql
   ```
   > Con `ddl-auto=update` Hibernate crea las tablas (incluidas `rol`, `usuario`, `usuario_rol`).
   > El script solo sirve para **precargar categorías y productos** de ejemplo.

3. Revisar la conexión en `src/main/resources/application.properties`
   (por defecto `postgres` / `postgres`).

4. Levantar la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```
   O desde IntelliJ: ejecutar la clase `Application`.

---
Hecho por Oscar Alvarado
