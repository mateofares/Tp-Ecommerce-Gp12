package com.tpo.ecommerce.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Migraciones idempotentes que corren al arrancar, despues de que Hibernate crea/actualiza
 * las tablas. Se usa JdbcTemplate (no schema.sql) porque el bloque PL/pgSQL DO $$...$$ contiene
 * ';' internos que el splitter de Spring no maneja; el driver JDBC si acepta el bloque completo.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseMigrationRunner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        // 1) imagen_url debe ser TEXT para almacenar imagenes en base64 (data URL).
        ejecutar("ALTER TABLE producto ALTER COLUMN imagen_url TYPE TEXT");

        // 2) Hibernate genera un CHECK para columnas enum (talle in 'XS','S','M','L','XL') que
        //    ddl-auto=update no actualiza al agregar los talles de calzado (T6..T12). Se elimina
        //    el constraint viejo buscandolo por definicion para no depender del nombre autogenerado.
        ejecutar(
            "DO $$ DECLARE c record; BEGIN " +
            "  FOR c IN SELECT con.conname FROM pg_constraint con " +
            "    JOIN pg_class rel ON rel.oid = con.conrelid " +
            "    WHERE rel.relname = 'producto' AND con.contype = 'c' " +
            "      AND pg_get_constraintdef(con.oid) ILIKE '%talle%' " +
            "  LOOP EXECUTE 'ALTER TABLE producto DROP CONSTRAINT ' || quote_ident(c.conname); END LOOP; " +
            "END $$"
        );
    }

    private void ejecutar(String sql) {
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            // Idempotente: si ya esta aplicada o la tabla aun no existe, se ignora.
            log.warn("Migracion omitida ({}): {}", e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
