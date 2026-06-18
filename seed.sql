-- ============================================================
-- SEED DE DATOS PARA EL FRONT (PostgreSQL / Supabase)
-- ============================================================
-- - Idempotente: se puede correr varias veces sin duplicar (usa WHERE NOT EXISTS).
-- - No hardcodea IDs: las FK se resuelven por mail/titulo (no rompe las secuencias identity).
-- - Password de TODOS los usuarios: "password123"
--   (hash BCrypt compatible con Spring Security).
-- - Enums se guardan como el NOMBRE del valor (EnumType.STRING).
-- ============================================================

-- ----------------------------------------------------------------
-- USUARIOS  (rol: USUARIO / ADMINISTRADOR ; estado_registro: ACTIVO / ELIMINADO)
-- ----------------------------------------------------------------
INSERT INTO usuario (user_rol, nombre, apellido, mail, contrasenia, estado_registro)
SELECT 'ADMINISTRADOR', 'Admin', 'Root', 'admin@demo.com', '$2a$10$VLTixYI9stW8FFjur2dsDeB7j2YNww78JRiaGiHft9h9YM0qo4xpS', 'ACTIVO'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE mail = 'admin@demo.com');

INSERT INTO usuario (user_rol, nombre, apellido, mail, contrasenia, estado_registro)
SELECT 'USUARIO', 'Lucia', 'Gomez', 'vendedor1@demo.com', '$2a$10$VLTixYI9stW8FFjur2dsDeB7j2YNww78JRiaGiHft9h9YM0qo4xpS', 'ACTIVO'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE mail = 'vendedor1@demo.com');

INSERT INTO usuario (user_rol, nombre, apellido, mail, contrasenia, estado_registro)
SELECT 'USUARIO', 'Martin', 'Diaz', 'vendedor2@demo.com', '$2a$10$VLTixYI9stW8FFjur2dsDeB7j2YNww78JRiaGiHft9h9YM0qo4xpS', 'ACTIVO'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE mail = 'vendedor2@demo.com');

INSERT INTO usuario (user_rol, nombre, apellido, mail, contrasenia, estado_registro)
SELECT 'USUARIO', 'Sofia', 'Ruiz', 'vendedor3@demo.com', '$2a$10$VLTixYI9stW8FFjur2dsDeB7j2YNww78JRiaGiHft9h9YM0qo4xpS', 'ACTIVO'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE mail = 'vendedor3@demo.com');

INSERT INTO usuario (user_rol, nombre, apellido, mail, contrasenia, estado_registro)
SELECT 'USUARIO', 'Juan', 'Comprador', 'comprador@demo.com', '$2a$10$VLTixYI9stW8FFjur2dsDeB7j2YNww78JRiaGiHft9h9YM0qo4xpS', 'ACTIVO'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE mail = 'comprador@demo.com');

-- ----------------------------------------------------------------
-- DIRECCIONES  (para que el comprador pueda hacer checkout)
-- ----------------------------------------------------------------
INSERT INTO direccion (usuario_id, calle, numero, ciudad, codigo_postal, provincia, tipo_direccion, predeterminada, activa, fecha_creacion, notas, estado_registro)
SELECT u.id, 'Av. Corrientes', '1234', 'CABA', '1043', 'Buenos Aires', 'CASA', true, true, NOW(), 'Timbre 4B', 'ACTIVO'
FROM usuario u WHERE u.mail = 'comprador@demo.com'
AND NOT EXISTS (SELECT 1 FROM direccion d WHERE d.usuario_id = u.id AND d.calle = 'Av. Corrientes' AND d.numero = '1234');

INSERT INTO direccion (usuario_id, calle, numero, ciudad, codigo_postal, provincia, tipo_direccion, predeterminada, activa, fecha_creacion, notas, estado_registro)
SELECT u.id, 'San Martin', '567', 'Rosario', '2000', 'Santa Fe', 'TRABAJO', false, true, NOW(), NULL, 'ACTIVO'
FROM usuario u WHERE u.mail = 'comprador@demo.com'
AND NOT EXISTS (SELECT 1 FROM direccion d WHERE d.usuario_id = u.id AND d.calle = 'San Martin' AND d.numero = '567');

-- ----------------------------------------------------------------
-- DESCUENTOS  (tipo: PORCENTAJE / MONTO)
-- ----------------------------------------------------------------
INSERT INTO descuento (codigo_descuento, tipo, valor, valido_desde, valido_hasta, estado_registro)
SELECT 'BIENVENIDO10', 'PORCENTAJE', 10, CURRENT_DATE, CURRENT_DATE + INTERVAL '90 day', 'ACTIVO'
WHERE NOT EXISTS (SELECT 1 FROM descuento WHERE codigo_descuento = 'BIENVENIDO10');

INSERT INTO descuento (codigo_descuento, tipo, valor, valido_desde, valido_hasta, estado_registro)
SELECT 'VERANO5000', 'MONTO', 5000, CURRENT_DATE, CURRENT_DATE + INTERVAL '60 day', 'ACTIVO'
WHERE NOT EXISTS (SELECT 1 FROM descuento WHERE codigo_descuento = 'VERANO5000');

-- ----------------------------------------------------------------
-- PRODUCTOS
-- categoria: PANTALONES, CAMISETAS, ZAPATILLAS, CAMPERAS, ACCESORIOS, OTRO
-- marca:     NIKE, ADIDAS, REEBOK, PUMA, CONVERSE
-- talle:     ropa XS,S,M,L,XL  |  calzado T6..T12, T6M..T12M (hombre), T6W..T12W (mujer)
-- color:     ROJO, VERDE, AZUL, AMARILLO, NEGRO, BLANCO
-- estado:    NUEVO / USADO     |  estado_producto: DISPONIBLE / VENDIDO
-- ----------------------------------------------------------------

-- Helper de patron: cada producto se asocia a un vendedor por mail y solo se inserta si no existe (por titulo).

-- ZAPATILLAS
INSERT INTO producto (titulo, descripcion, precio, categoria, marca, talle, color, estado, imagen_url, estado_registro, estado_producto, usuario_id)
SELECT 'Nike Air Force 1', 'Zapatillas urbanas clasicas, cuero blanco impecable.', 95000, 'ZAPATILLAS', 'NIKE', 'T8M', 'BLANCO', 'NUEVO', 'https://picsum.photos/seed/airforce/600/600', 'ACTIVO', 'DISPONIBLE', u.id
FROM usuario u WHERE u.mail = 'vendedor1@demo.com'
AND NOT EXISTS (SELECT 1 FROM producto WHERE titulo = 'Nike Air Force 1');

INSERT INTO producto (titulo, descripcion, precio, categoria, marca, talle, color, estado, imagen_url, estado_registro, estado_producto, usuario_id)
SELECT 'Adidas Superstar', 'Modelo icono con punta de goma, casi nuevas.', 80000, 'ZAPATILLAS', 'ADIDAS', 'T7W', 'BLANCO', 'NUEVO', 'https://picsum.photos/seed/superstar/600/600', 'ACTIVO', 'DISPONIBLE', u.id
FROM usuario u WHERE u.mail = 'vendedor2@demo.com'
AND NOT EXISTS (SELECT 1 FROM producto WHERE titulo = 'Adidas Superstar');

INSERT INTO producto (titulo, descripcion, precio, categoria, marca, talle, color, estado, imagen_url, estado_registro, estado_producto, usuario_id)
SELECT 'Converse Chuck Taylor', 'Botitas de lona negras, segunda vida autentica.', 35000, 'ZAPATILLAS', 'CONVERSE', 'T9M', 'NEGRO', 'USADO', 'https://picsum.photos/seed/chuck/600/600', 'ACTIVO', 'DISPONIBLE', u.id
FROM usuario u WHERE u.mail = 'vendedor3@demo.com'
AND NOT EXISTS (SELECT 1 FROM producto WHERE titulo = 'Converse Chuck Taylor');

INSERT INTO producto (titulo, descripcion, precio, categoria, marca, talle, color, estado, imagen_url, estado_registro, estado_producto, usuario_id)
SELECT 'Puma Suede Classic', 'Gamuza azul, suela en buen estado.', 42000, 'ZAPATILLAS', 'PUMA', 'T10M', 'AZUL', 'USADO', 'https://picsum.photos/seed/suede/600/600', 'ACTIVO', 'DISPONIBLE', u.id
FROM usuario u WHERE u.mail = 'vendedor1@demo.com'
AND NOT EXISTS (SELECT 1 FROM producto WHERE titulo = 'Puma Suede Classic');

-- CAMISETAS
INSERT INTO producto (titulo, descripcion, precio, categoria, marca, talle, color, estado, imagen_url, estado_registro, estado_producto, usuario_id)
SELECT 'Remera Nike Dri-Fit', 'Remera deportiva transpirable, sin uso.', 25000, 'CAMISETAS', 'NIKE', 'M', 'NEGRO', 'NUEVO', 'https://picsum.photos/seed/drifit/600/600', 'ACTIVO', 'DISPONIBLE', u.id
FROM usuario u WHERE u.mail = 'vendedor2@demo.com'
AND NOT EXISTS (SELECT 1 FROM producto WHERE titulo = 'Remera Nike Dri-Fit');

INSERT INTO producto (titulo, descripcion, precio, categoria, marca, talle, color, estado, imagen_url, estado_registro, estado_producto, usuario_id)
SELECT 'Camiseta Adidas Originals', 'Algodon blanco con logo trefoil, leve uso.', 12000, 'CAMISETAS', 'ADIDAS', 'L', 'BLANCO', 'USADO', 'https://picsum.photos/seed/originals/600/600', 'ACTIVO', 'DISPONIBLE', u.id
FROM usuario u WHERE u.mail = 'vendedor3@demo.com'
AND NOT EXISTS (SELECT 1 FROM producto WHERE titulo = 'Camiseta Adidas Originals');

INSERT INTO producto (titulo, descripcion, precio, categoria, marca, talle, color, estado, imagen_url, estado_registro, estado_producto, usuario_id)
SELECT 'Remera Reebok Vintage', 'Pieza retro roja de los 90, unica.', 9000, 'CAMISETAS', 'REEBOK', 'S', 'ROJO', 'USADO', 'https://picsum.photos/seed/reebokv/600/600', 'ACTIVO', 'DISPONIBLE', u.id
FROM usuario u WHERE u.mail = 'vendedor1@demo.com'
AND NOT EXISTS (SELECT 1 FROM producto WHERE titulo = 'Remera Reebok Vintage');

-- PANTALONES
INSERT INTO producto (titulo, descripcion, precio, categoria, marca, talle, color, estado, imagen_url, estado_registro, estado_producto, usuario_id)
SELECT 'Jogging Nike Tech', 'Pantalon tech fleece negro, abrigado, nuevo.', 38000, 'PANTALONES', 'NIKE', 'M', 'NEGRO', 'NUEVO', 'https://picsum.photos/seed/techfleece/600/600', 'ACTIVO', 'DISPONIBLE', u.id
FROM usuario u WHERE u.mail = 'vendedor2@demo.com'
AND NOT EXISTS (SELECT 1 FROM producto WHERE titulo = 'Jogging Nike Tech');

INSERT INTO producto (titulo, descripcion, precio, categoria, marca, talle, color, estado, imagen_url, estado_registro, estado_producto, usuario_id)
SELECT 'Pantalon Adidas Tiro', 'Pantalon de entrenamiento azul, poco uso.', 20000, 'PANTALONES', 'ADIDAS', 'L', 'AZUL', 'USADO', 'https://picsum.photos/seed/tiro/600/600', 'ACTIVO', 'DISPONIBLE', u.id
FROM usuario u WHERE u.mail = 'vendedor3@demo.com'
AND NOT EXISTS (SELECT 1 FROM producto WHERE titulo = 'Pantalon Adidas Tiro');

-- CAMPERAS
INSERT INTO producto (titulo, descripcion, precio, categoria, marca, talle, color, estado, imagen_url, estado_registro, estado_producto, usuario_id)
SELECT 'Campera Puma Windbreaker', 'Rompeviento amarillo, impermeable, nuevo.', 55000, 'CAMPERAS', 'PUMA', 'XL', 'AMARILLO', 'NUEVO', 'https://picsum.photos/seed/windbreaker/600/600', 'ACTIVO', 'DISPONIBLE', u.id
FROM usuario u WHERE u.mail = 'vendedor1@demo.com'
AND NOT EXISTS (SELECT 1 FROM producto WHERE titulo = 'Campera Puma Windbreaker');

INSERT INTO producto (titulo, descripcion, precio, categoria, marca, talle, color, estado, imagen_url, estado_registro, estado_producto, usuario_id)
SELECT 'Buzo Converse Hoodie', 'Buzo verde con capucha, algodon grueso.', 28000, 'CAMPERAS', 'CONVERSE', 'M', 'VERDE', 'USADO', 'https://picsum.photos/seed/hoodie/600/600', 'ACTIVO', 'DISPONIBLE', u.id
FROM usuario u WHERE u.mail = 'vendedor2@demo.com'
AND NOT EXISTS (SELECT 1 FROM producto WHERE titulo = 'Buzo Converse Hoodie');

-- ACCESORIOS (sin talle)
INSERT INTO producto (titulo, descripcion, precio, categoria, marca, talle, color, estado, imagen_url, estado_registro, estado_producto, usuario_id)
SELECT 'Gorra Nike Classic', 'Gorra negra ajustable, nueva.', 15000, 'ACCESORIOS', 'NIKE', NULL, 'NEGRO', 'NUEVO', 'https://picsum.photos/seed/gorra/600/600', 'ACTIVO', 'DISPONIBLE', u.id
FROM usuario u WHERE u.mail = 'vendedor3@demo.com'
AND NOT EXISTS (SELECT 1 FROM producto WHERE titulo = 'Gorra Nike Classic');

INSERT INTO producto (titulo, descripcion, precio, categoria, marca, talle, color, estado, imagen_url, estado_registro, estado_producto, usuario_id)
SELECT 'Mochila Adidas', 'Mochila urbana negra, varios compartimentos.', 30000, 'ACCESORIOS', 'ADIDAS', NULL, 'NEGRO', 'NUEVO', 'https://picsum.photos/seed/mochila/600/600', 'ACTIVO', 'DISPONIBLE', u.id
FROM usuario u WHERE u.mail = 'vendedor1@demo.com'
AND NOT EXISTS (SELECT 1 FROM producto WHERE titulo = 'Mochila Adidas');

-- Producto VENDIDO (no aparece en el catalogo, util para probar el filtro DISPONIBLE)
INSERT INTO producto (titulo, descripcion, precio, categoria, marca, talle, color, estado, imagen_url, estado_registro, estado_producto, usuario_id)
SELECT 'Nike Dunk Low (vendida)', 'Ya vendida, no deberia aparecer en el catalogo.', 120000, 'ZAPATILLAS', 'NIKE', 'T9M', 'ROJO', 'USADO', 'https://picsum.photos/seed/dunk/600/600', 'ACTIVO', 'VENDIDO', u.id
FROM usuario u WHERE u.mail = 'vendedor2@demo.com'
AND NOT EXISTS (SELECT 1 FROM producto WHERE titulo = 'Nike Dunk Low (vendida)');

-- ============================================================
-- VERIFICACION rapida (opcional)
-- ============================================================
-- SELECT mail, user_rol FROM usuario ORDER BY id;
-- SELECT titulo, categoria, marca, talle, color, estado, estado_producto FROM producto ORDER BY id;
