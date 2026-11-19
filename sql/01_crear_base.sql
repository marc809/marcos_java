-- Script de creación de la base (Manual del Programador LP3).
-- Ejecutar en pgAdmin: clic derecho sobre PostgreSQL 17 → Query Tool → F5.

-- 1. Crear la base de datos
CREATE DATABASE lp3_db;

-- 2. Crear el usuario con contraseña
CREATE USER lp3_user WITH PASSWORD 'lp3_pass';

-- 3. Asignar permisos sobre la base de datos
GRANT ALL PRIVILEGES ON DATABASE lp3_db TO lp3_user;

-- 4. Conectarse a lp3_db y asignar permisos sobre el esquema
-- En psql se usa \c lp3_db
-- En pgAdmin: cerrá este Query Tool, clic derecho en lp3_db → Query Tool, y ejecutá:

GRANT ALL ON SCHEMA public TO lp3_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO lp3_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO lp3_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO lp3_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO lp3_user;
