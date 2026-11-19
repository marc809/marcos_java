# Manual del programador — Chacomer Motos

Universidad Autónoma San Sebastián (UASS)  
Lenguaje de Programación III — Java · 6.º semestre · Ciclo 2026

Este manual describe, en orden, cómo dejar el entorno listo y ejecutar **este** proyecto Spring Boot. Sigue la misma lógica del *Manual del Programador LP3* de la cátedra (JDK 17, VS Code, PostgreSQL, Maven Wrapper).

Si un comando no se reconoce, leé el mensaje de error completo antes de cambiar otra cosa.

## 1. Resumen del entorno

| Herramienta | Versión y propósito |
| --- | --- |
| Eclipse Temurin JDK | **17 LTS** — compila y ejecuta Java |
| Visual Studio Code | última estable — editor |
| Extension Pack for Java | última — Maven, depuración y ejecución |
| PostgreSQL | **17** o superior, puerto **5432** |
| pgAdmin 4 | administración gráfica |
| Maven Wrapper (`mvnw` / `mvnw.cmd`) | viene en el proyecto |
| Git | control de versiones |

## 2. Instalación del JDK 17

1. Entrá a https://adoptium.net/
2. Elegí **Temurin 17 (LTS)** e instalá el `.msi` de Windows. Marcá **Add to PATH**.
3. Cerrá y abrí de nuevo la terminal. Verificá:

```bat
java -version
```

Debe aparecer `17.0.x`. Si dice que `java` no se reconoce, agregá la carpeta `bin` del JDK a la variable PATH.

## 3. Visual Studio Code y Java

1. https://code.visualstudio.com/ — instalá marcando PATH y “Abrir con Code”.
2. Extensiones (`Ctrl+Shift+X`): **Extension Pack for Java** (Microsoft, `vscjava.vscode-java-pack`).
3. Paleta `Ctrl+Shift+P` → `Java: Configure Java Runtime`. El JDK 17 debe listarse.

## 4. PostgreSQL y pgAdmin

1. https://www.postgresql.org/download/ — instalador de PostgreSQL **17**.
2. Componentes: Server, pgAdmin 4, Command Line Tools.
3. Puerto **5432** (no lo cambies: Spring Boot lo espera).
4. Anotá la contraseña del superusuario `postgres`.
5. Verificá:

```bat
psql --version
```

Si `psql` no se reconoce, agregá `C:\Program Files\PostgreSQL\17\bin` al PATH.

Abrí pgAdmin, expandí **Servers → PostgreSQL 17**. Si ves la base `postgres`, la instalación está bien.

## 5. Creación de la base del proyecto

No uses el superusuario `postgres` para la aplicación.

1. En pgAdmin: clic derecho sobre el servidor → **Query Tool**.
2. Pegá y ejecutá (`F5`) el script `sql/01_crear_base.sql`.
3. El Query Tool de pgAdmin **no** entiende `\c lp3_db`. Después de crear la base, abrí un Query Tool **sobre `lp3_db`** y ejecutá los `GRANT` del final del archivo.
4. Verificación: **Servers → PostgreSQL 17 → Databases**. Debe existir `lp3_db`.

Usuario de la app: `lp3_user` / `lp3_pass`. Si cambiás la contraseña, actualizá también `application.properties`.

## 6. Apertura del proyecto

Este repositorio **ya está generado** (Maven, `com.uass`, Java 17, dependencias Web + JPA + PostgreSQL). No hace falta volver a Spring Initializr.

1. VS Code → **File → Open Folder…** → carpeta `chacomer`.
2. Confirmá confianza en el espacio de trabajo.
3. Esperá a que Maven baje dependencias (barra inferior).

Archivos clave:

- `src/main/java/com/uass/chacomer/ChacomerApplication.java` — `main()`; no hace falta tocarlo para correr.
- `src/main/resources/application.properties` — URL, usuario, clave y WhatsApp.
- `pom.xml` — dependencias Maven.

## 7. Configuración (`application.properties`)

Valores que deben coincidir con el script SQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/lp3_db
spring.datasource.username=lp3_user
spring.datasource.password=lp3_pass
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

WhatsApp de la tienda (sin `+` ni espacios):

```properties
chacomer.whatsapp=595981000000
```

## 8. Ejecución

Terminal integrada (`Ctrl+ñ` o **Terminal → New Terminal**), en la raíz del proyecto:

```bat
mvnw.cmd spring-boot:run
```

La primera vez Maven descarga dependencias (puede tardar). Buscá en consola:

- `HikariPool-1 - Start completed` → conectó a PostgreSQL.
- `Tomcat started on port 8080` → web en http://localhost:8080
- `Started ChacomerApplication` → arranque OK.

Para detener: `Ctrl+C` en esa terminal.

## 9. Verificación

1. Navegador: http://localhost:8080 — debe verse el catálogo.
2. API: http://localhost:8080/api/motos y http://localhost:8080/api/categorias
3. pgAdmin → `lp3_db` → Schemas → public → Tables: `categorias` y `motos`.
4. Query Tool:

```sql
SELECT table_name FROM information_schema.tables WHERE table_schema = 'public';
SELECT marca, modelo, cilindrada, precio FROM motos;
```

## 10. Errores frecuentes

| Mensaje | Qué hacer |
| --- | --- |
| `java` no se reconoce | JDK fuera del PATH |
| Connection refused | Servicio PostgreSQL detenido (Servicios de Windows → `postgresql-x64-17`) |
| password authentication failed | Clave distinta entre SQL y `application.properties` |
| Port 8080 was already in use | Cerrá la otra instancia con `Ctrl+C` o poné `server.port=8081` |
| BUILD FAILURE | Leé la línea `[ERROR]`: archivo y número de línea |
| Access to DialectResolutionInfo cannot be null | No hay conexión a PostgreSQL al arrancar |

## 11. Lista de comprobación

- [ ] `java -version` muestra 17
- [ ] Extension Pack for Java reconoce el JDK
- [ ] Existe `lp3_db` y el usuario `lp3_user`
- [ ] `application.properties` con URL / user / pass correctos
- [ ] `mvnw.cmd spring-boot:run` llega a Tomcat 8080
- [ ] La web muestra motos y el botón abre WhatsApp

## Referencias (APA 7)

Broadcom. (s. f.). *Spring Initializr*. https://start.spring.io/  
Broadcom. (2026). *Spring Boot reference documentation*. https://docs.spring.io/spring-boot/index.html  
Eclipse Adoptium. (s. f.). *Eclipse Temurin*. https://adoptium.net/  
PostgreSQL Global Development Group. (2026). *PostgreSQL documentation*. https://www.postgresql.org/docs/current/index.html  
Microsoft. (s. f.). *Getting started with Java in Visual Studio Code*. https://code.visualstudio.com/docs/java/java-tutorial  
Walls, C. (2022). *Spring in Action* (6.ª ed.). Manning.
