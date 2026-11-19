# Documentación del proyecto Chacomer Motos

**Asignatura:** Lenguaje de Programación III  
**Carrera:** Informática · UASS  
**Stack:** Java 17, Spring Boot 3.5, Spring Web, Spring Data JPA, PostgreSQL 17, HTML/CSS/JavaScript

---

## 1. Introducción

Chacomer es una tienda de venta de vehículos cuya vitrina más fuerte son las **motos de media y alta cilindrada**. Este proyecto publica un sitio web sencillo: el visitante recorre el catálogo, ve precios y, si quiere comprar o consultar, es derivado a **WhatsApp** con un mensaje listo.

No hay carrito ni pasarela de pago. El cierre de la venta lo hace el vendedor en el chat.

## 2. Objetivos

- Exponer un catálogo persistido en PostgreSQL.
- Separar **backend** (API REST + JPA) y **frontend** (páginas estáticas servidas por Spring).
- Permitir filtrar por media / alta cilindrada.
- Generar el enlace `https://wa.me/<número>?text=...` con marca, modelo, cc y precio.
- Dejar el entorno y la ejecución documentados como en el manual de LP3.

## 3. Alcance

**Incluye:** listado, detalle, filtros, WhatsApp, carga inicial de datos, scripts SQL de la base.

**No incluye:** login de administradores, stock en tiempo real, pagos en línea ni app móvil.

## 4. Arquitectura

```
Navegador (HTML / CSS / JS)
        │  GET /
        │  GET /api/motos  GET /api/categorias  GET /api/config
        ▼
Spring Boot 3.5 (Tomcat :8080)
  · static/          → frontend
  · ApiControlador   → JSON
  · Repositorios JPA
        ▼
PostgreSQL 17  (lp3_db · tablas categorias, motos)
```

### Capas (paquetes)

| Paquete | Rol |
| --- | --- |
| `com.uass.chacomer` | Clase `main` (`ChacomerApplication`) |
| `entidad` | `@Entity` mapeadas a tablas |
| `repositorio` | `JpaRepository` |
| `controlador` | API REST `/api` |
| `config` | `CargaInicial` del catálogo |

El frontend vive en `src/main/resources/static/` (`index.html`, `css/estilos.css`, `js/app.js`). Spring lo sirve en la raíz del puerto 8080.

## 5. Modelo de datos

```
categorias                      motos
-----------                     ----------------------
id              PK              id               PK
nombre                          marca
codigo                          modelo
descripcion                     cilindrada
                                precio
                                color
                                descripcion
                                imagen_url
                                disponible
                                categoria_id     FK → categorias.id
```

- **Media cilindrada** (`codigo = media`): 250–499 cc.  
- **Alta cilindrada** (`codigo = alta`): 500 cc o más.

Hibernate crea/actualiza las tablas con `spring.jpa.hibernate.ddl-auto=update`. La primera ejecución inserta 2 categorías y 8 motos (`CargaInicial`).

## 6. Flujos principales

1. **Arranque:** Spring conecta a `lp3_db`, crea tablas si faltan, carga el catálogo si está vacío.
2. **Visita al sitio:** el navegador pide `/` y recibe `index.html`.
3. **Catálogo:** `app.js` llama `GET /api/config` (número de WhatsApp) y `GET /api/motos`.
4. **Filtro:** `GET /api/motos?categoria=media` o `alta`.
5. **Compra / consulta:** el usuario abre el detalle y pulsa *Quiero esta moto*. El navegador abre WhatsApp Web/app con el texto prearmado.

## 7. API REST

| Método | Ruta | Descripción |
| --- | --- | --- |
| GET | `/api/config` | Nombre de la tienda y WhatsApp |
| GET | `/api/categorias` | Media y alta cilindrada |
| GET | `/api/motos` | Catálogo completo (precio ascendente) |
| GET | `/api/motos?categoria=media` | Solo medias |
| GET | `/api/motos?categoria=alta` | Solo altas |
| GET | `/api/motos/{id}` | Una moto |

Ejemplo de cuerpo (moto):

```json
{
  "id": 1,
  "marca": "Yamaha",
  "modelo": "MT-03",
  "cilindrada": 321,
  "precio": 32800000,
  "color": "Azul night",
  "descripcion": "...",
  "imagenUrl": "https://...",
  "disponible": true,
  "categoria": { "id": 1, "nombre": "Media cilindrada", "codigo": "media" }
}
```

## 8. Frontend

- Paleta oscura (asfalto / rojo de marca / verde WhatsApp).
- Hero, dos tarjetas de cilindrada, grilla de catálogo, bloque de contacto.
- Modal de detalle.
- Precios con formato `es-PY` (guaraníes).

## 9. Cómo ejecutar

Ver `MANUAL_PROGRAMADOR.md` (secciones 5 a 9) y el `README.md` de la raíz.

Comando Windows:

```bat
mvnw.cmd spring-boot:run
```

URL: http://localhost:8080

## 10. Dónde hacer el primer cambio

El ajuste más visible y útil:

1. **WhatsApp real** — `src/main/resources/application.properties`, propiedad `chacomer.whatsapp`.
2. **Motos / precios** — `src/main/java/com/uass/chacomer/config/CargaInicial.java` (solo aplica si las tablas están vacías; si ya hay datos, editá en pgAdmin la tabla `motos`).
3. **Textos o colores de la web** — `static/index.html` y `static/css/estilos.css`.

## 11. Manual breve de usuario

1. Abrí el sitio.
2. Elegí *Todas*, *Media* o *Alta*.
3. Tocá *Ver y consultar*.
4. Tocá *Quiero esta moto* y enviá el mensaje por WhatsApp.

## 12. Archivos que importan

| Archivo | Para qué |
| --- | --- |
| `pom.xml` | Maven, Java 17, Spring Boot 3.5.6, Web + JPA + PostgreSQL |
| `application.properties` | Base de datos y WhatsApp |
| `ChacomerApplication.java` | Punto de entrada |
| `entidad/Moto.java`, `entidad/Categoria.java` | Tablas |
| `controlador/ApiControlador.java` | JSON del catálogo |
| `config/CargaInicial.java` | Datos de ejemplo |
| `static/index.html` + `css` + `js` | Interfaz |
| `sql/01_crear_base.sql` | `lp3_db` y `lp3_user` |
