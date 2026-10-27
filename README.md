# Gestión de incidencias

Aplicación web para registrar y consultar incidencias técnicas de un equipo de soporte. Prueba técnica para Desarrollador/a Java (perfil Middle).

## Stack

- Java 17 (Temurin) y Spring Boot 4.1.2
- Spring Web, Spring Data JPA, Bean Validation
- H2 en memoria
- Maven (con wrapper `mvnw`)
- Frontend en HTML, CSS y JavaScript sin frameworks
- JUnit 5 y Mockito

## Cómo arrancar y probar

Requisitos: JDK 17. No hace falta instalar Maven.

Arrancar la aplicación (un solo comando):

```bash
./mvnw clean spring-boot:run     # Windows: .\mvnw clean spring-boot:run
```

Ejecutar los tests (un solo comando):

```bash
./mvnw test                      # Windows: .\mvnw test
```

La aplicación queda en http://localhost:8080/ (tabla de incidencias). Al ser H2 en memoria, los datos se reinician en cada arranque.

### API

| Método | Ruta | Descripción | Códigos |
|--------|------|-------------|---------|
| POST | `/api/incidencias` | Crea una incidencia (estado inicial ABIERTA) | 201, 400, 404 (usuario inexistente) |
| GET | `/api/incidencias?estado=ABIERTA` | Lista incidencias, filtro opcional por estado | 200, 400 (estado inválido) |
| PATCH | `/api/incidencias/{id}/estado` | Cambia el estado | 200, 400, 404, 409 |

Todos los errores devuelven el mismo JSON:

```json
{ "status": 409, "error": "Conflict", "message": "...", "timestamp": "..." }
```

### Ejemplos de uso

```bash
curl -X POST localhost:8080/api/incidencias -H "Content-Type: application/json" \
  -d '{"titulo":"Prueba","prioridad":"ALTA"}'

curl -X PATCH localhost:8080/api/incidencias/1/estado -H "Content-Type: application/json" \
  -d '{"estado":"CERRADA"}'
```

(En Windows, usa `curl.exe` y escapa las comillas del JSON: `-d "{\"estado\":\"CERRADA\"}"`.)

## Estructura

```
controller/   IncidenciaController, GlobalExceptionHandler
service/      IncidenciaService (reglas de negocio y transacciones)
repository/   Repositorios Spring Data JPA
model/        Entidades y enums
dto/          Records de entrada y salida
exception/    Excepciones de negocio
resources/    schema.sql (esquema y datos de ejemplo), static/ (frontend)
```

## Decisiones técnicas

- **Un único script SQL.** `schema.sql` contiene tablas, restricciones, índices y datos de ejemplo, porque el enunciado pide un único script. Con `ddl-auto=none` es la única fuente de verdad: Hibernate no genera el esquema.
- **Integridad en la BD**: PK, FK, `UNIQUE` en email, `CHECK` en prioridad y estado (la BD protege los datos aunque falle la aplicación) e índices en `estado` y `usuario_id`.
- **`ON DELETE SET NULL`** en la FK: borrar un usuario no bloquea ni borra sus incidencias, que quedan sin asignar (la asignación es opcional).
- **Enums con `@Enumerated(EnumType.STRING)`**: con `ORDINAL` se guardarían números y chocarían con los `CHECK`.
- **DTOs como `record`** (Java 17): la entidad nunca sale por la API.
- **El servicio devuelve DTOs**, no entidades: `usuarioAsignado` es `LAZY` y `open-in-view=false`, así que el mapeo debe hacerse dentro de la transacción.
- **Regla de negocio en el servicio**: una incidencia CERRADA no puede pasar a otro estado (409, conflicto con el estado actual del recurso). Cerrar una ya cerrada se acepta (200): la operación es idempotente y no reabre nada.
- **Un único `@RestControllerAdvice`** con formato de error uniforme. Cubre también estados inválidos en el filtro o en el cuerpo (400 en lugar de 500).
- **PATCH y no PUT**, porque solo se modifica un campo.
- **Tests de servicio con Mockito**, sin levantar Spring: rápidos y centrados en la lógica.
- **`spring.sql.init.encoding=UTF-8`**: Java 17 usa la codificación de la plataforma por defecto (Windows-1252 en Windows) y estropeaba las tildes del script SQL.
- **Frontend sin frameworks**: `fetch` con comprobación de `response.ok`, mensaje visible si la API falla y `textContent` en lugar de `innerHTML` para evitar XSS.
- **Sin deduplicación de incidencias**: dos incidencias pueden tener el mismo título.

## Pruebas realizadas

- Tests unitarios de servicio (JUnit 5 y Mockito): regla de incidencias cerradas y estado inicial ABIERTA. Más el test de arranque del contexto de Spring.
- Pruebas manuales con curl: GET con y sin filtro, filtro inválido (400), POST válido (201 con `Location`), título vacío y prioridad inválida (400), PATCH sobre incidencia cerrada (409) y sobre una inexistente (404).
- Frontend en el navegador: tabla, filtro por estado y mensaje de error con la API parada.

## Pendiente o mejorable con más tiempo

Rápido (menos de una hora):
- `GET /api/incidencias/{id}`: la cabecera `Location` del 201 apunta a esa ruta, que aún no existe.
- Formulario de creación de incidencias en el frontend (opcional en el enunciado).
- Normalizar el email a minúsculas para evitar duplicados por mayúsculas.
- Resolver el N+1 del listado (una consulta por usuario distinto) con `JOIN FETCH` o `@EntityGraph`.

Más tiempo:
- Tests del controlador (`@WebMvcTest`) y de integración con la base de datos.
- Transiciones de estado más estrictas y paginación en el listado.
- Integración continua: `Jenkinsfile` (build y test) y análisis de calidad con SonarQube.
- Perfil con BD real (PostgreSQL) y migraciones con Flyway.

## Uso de IA

He usado Claude como asistente durante todo el desarrollo, y lo indico con detalle.

**En qué me ha ayudado la IA**
- Propuesta de estructura y orden de trabajo.
- Borradores de código: entidades, servicio, controlador, manejador de errores, tests y frontend.
- Diagnóstico de problemas de entorno.

**Qué he decidido y comprobado yo**
- Stack: Maven y JPA, Java 17 (instalé Temurin 17 en lugar de usar el JDK 23 que tenía).
- Esquema: contrasté la propuesta con una segunda IA y apliqué sus mejoras (`VARCHAR(20)`, `ON DELETE SET NULL`, índice en la FK). Unifiqué esquema y datos en un único script.
- Pruebas: todas las de la sección anterior las ejecuté yo.
- Entorno: resolví `JAVA_HOME`, la codificación UTF-8 y un `data.sql` obsoleto que seguía en `target`.
- Historial de commits pequeños, revisando el estado de Git en cada paso.

**Errores de la IA que tuve que corregir**
- Propuso Spring Boot 3, pero Spring Initializr generó la 4.1.2: mantuve la 4.1.2 (el primer commit dice 3 por error).
- Atribuyó las tildes rotas a la consola de Windows; en realidad Java 17 leía el script con la codificación de la plataforma.