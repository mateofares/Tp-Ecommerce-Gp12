# Documento de avance - Ecommerce API

Fecha de corte: 20/03/2026

## 1) Resumen general
Se implemento una API REST con Spring Boot para gestionar usuarios. La arquitectura separa responsabilidades en capas: controlador (HTTP), servicio (reglas de negocio), repositorio (persistencia), entidad (modelo de datos) y DTOs (objetos de transferencia).

## 2) Tecnologias y librerias usadas
- Java + Spring Boot: punto de entrada y configuracion automatica de la app.
- Spring Web: controladores REST y manejo de requests/responses.
- Spring Data JPA + Hibernate: mapeo ORM y acceso a base de datos.
- PostgreSQL: base de datos relacional.
- Lombok: generacion automatica de getters/constructores.

## 3) Configuracion principal
Archivo: `src/main/resources/application.properties`
- `server.port=8080`: puerto de la API.
- `spring.datasource.url`: conexion a PostgreSQL (Supabase).
- `spring.datasource.username`: usuario de BD.
- `spring.datasource.password`: existe en el archivo, pero en este documento se omite por seguridad.
- `spring.jpa.hibernate.ddl-auto=update`: crea/actualiza tablas segun entidades.
- `spring.jpa.show-sql=true` y `spring.jpa.properties.hibernate.format_sql=true`: imprime SQL en consola.
- `spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true`: evita problemas con pooler.

## 4) Paquetes y responsabilidades
- `com.tpo.ecommerce.controller`
  - `UsuarioController`: expone endpoints REST para usuarios.
- `com.tpo.ecommerce.service`
  - `UsuarioService`: logica de negocio (validaciones, login, alta, update).
  - `IUsuarioService`: interfaz del servicio.
- `com.tpo.ecommerce.repository`
  - `UsuarioRepository`: acceso a BD via JPA (incluye `findByMail`).
- `com.tpo.ecommerce.entity`
  - `Usuario`: entidad JPA mapeada a la tabla `usuario`.
- `com.tpo.ecommerce.dto`
  - `UsuarioDTO`: datos expuestos por la API.
  - `LoginRequestDTO`: request del login.
- `com.tpo.ecommerce.enums`
  - `UserRol`: enum con roles `USUARIO` y `ADMINISTRADOR`.
- `com.tpo.ecommerce.mapper`
  - `MapperUsuario`: convierte entidad `Usuario` a `UsuarioDTO`.

## 5) Modelo de datos (Entidad)
Entidad `Usuario` (tabla `usuario`):
- `id` (Long, PK, autogenerado)
- `userRol` (Enum: USUARIO, ADMINISTRADOR)
- `nombre` (String)
- `mail` (String)
- `contrasenia` (String)
- `apellido` (String)

## 6) DTOs
- `UsuarioDTO`: usado en requests y responses. Incluye `contrasenia` (nota en el codigo: "quizas sacarla").
- `LoginRequestDTO`: contiene `mail` y `contrasenia`.

## 7) Endpoints y codigos de respuesta
Base URL: `/usuarios`

### 7.1) Obtener usuarios
- Metodo: `GET /usuarios`
- Parametros opcionales (query): `id`, `userRol`, `nombre`, `mail`, `apellido`
- Respuesta: lista de `UsuarioDTO`
- Codigo de exito:
  - `200 OK`
- Posibles errores:
  - `400 Bad Request` si algun parametro no puede parsearse (por ejemplo, `id` no numerico).

### 7.2) Eliminar usuario
- Metodo: `DELETE /usuarios?id={id}`
- Parametros requeridos: `id`
- Respuesta: vacia
- Codigo de exito:
  - `204 No Content`
- Posibles errores:
  - `400 Bad Request` si falta `id` o es invalido.

### 7.3) Actualizar usuario (parcial)
- Metodo: `PATCH /usuarios/{id}`
- Body: `UsuarioDTO` (se actualizan solo campos no nulos)
- Respuesta: `UsuarioDTO` actualizado
- Codigo de exito:
  - `200 OK`
- Posibles errores:
  - `400 Bad Request` si el body no es valido.
  - `500 Internal Server Error` si el `id` no existe (hoy se lanza `RuntimeException`).

### 7.4) Registrar usuario
- Metodo: `POST /usuarios/register`
- Body: `UsuarioDTO`
- Respuesta: `UsuarioDTO` creado
- Codigo de exito:
  - `200 OK`
- Posibles errores:
  - `400 Bad Request` si faltan campos o son vacios (se valida en servicio, pero hoy lanza `RuntimeException`).
  - `500 Internal Server Error` si el mail ya existe (se lanza `RuntimeException`).

### 7.5) Login
- Metodo: `POST /usuarios/login`
- Body: `LoginRequestDTO`
- Respuesta: `UsuarioDTO`
- Codigo de exito:
  - `200 OK`
- Posibles errores:
  - `500 Internal Server Error` si el mail no existe o la contrasenia no coincide (se lanza `RuntimeException`).

## 8) Observaciones sobre errores y codigos
Actualmente no hay un manejador global de excepciones (`@ControllerAdvice`). Por eso, varias validaciones y errores terminan como `500 Internal Server Error`. Para presentar la API con codigos mas claros (por ejemplo `404` o `409`), se recomienda:
- Reemplazar `RuntimeException` por `ResponseStatusException` o excepciones personalizadas.
- Agregar un `@ControllerAdvice` para mapear errores a HTTP status adecuados.

## 9) Proximos pasos sugeridos
- Mejorar manejo de errores y codigos HTTP.
- Ocultar `contrasenia` en respuestas o quitarla del DTO de salida.
- Agregar endpoints para productos, categorias y pedidos.
- Agregar autenticacion real (JWT) y encriptacion de contrasenias.
