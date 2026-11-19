# Chacomer Motos

Sitio web de la tienda **Chacomer**: catálogo de motos de **media** y **alta cilindrada**, con precios en guaraníes. Si alguien quiere comprar, el botón abre **WhatsApp** con el modelo ya escrito.

Proyecto de *Lenguaje de Programación III* (UASS): **Java 17**, **Spring Boot 3.5**, **PostgreSQL**, frontend HTML/CSS/JS.

## Cómo arrancar (resumen)

1. Instalá JDK 17, VS Code (Extension Pack for Java) y PostgreSQL 17 + pgAdmin, como indica `docs/MANUAL_PROGRAMADOR.md`.
2. En pgAdmin ejecutá `sql/01_crear_base.sql` (crea `lp3_db` / `lp3_user` / `lp3_pass`).
3. Poné tu número de WhatsApp (código de país, sin `+`) en `src/main/resources/application.properties`:

```properties
chacomer.whatsapp=595981000000
```

4. En la raíz del proyecto:

```bat
mvnw.cmd spring-boot:run
```

5. Abrí [http://localhost:8080](http://localhost:8080).

Documentación completa: `docs/DOCUMENTACION_PROYECTO.md`.
