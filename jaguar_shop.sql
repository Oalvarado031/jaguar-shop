DROP TABLE IF EXISTS detalle_pedido;
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS categoria;

CREATE TABLE categoria (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE producto (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio NUMERIC(10,2) NOT NULL CHECK (precio >= 0),
    imagen VARCHAR(255),
    stock INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0),
    categoria_id BIGINT NOT NULL,
    CONSTRAINT fk_producto_categoria
        FOREIGN KEY (categoria_id) REFERENCES categoria(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE pedido (
    id BIGSERIAL PRIMARY KEY,
    nombre_cliente VARCHAR(150) NOT NULL,
    correo VARCHAR(150) NOT NULL,
    comentario TEXT,
    fecha_pedido TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total NUMERIC(10,2) NOT NULL DEFAULT 0 CHECK (total >= 0)
);

CREATE TABLE detalle_pedido (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INTEGER NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(10,2) NOT NULL CHECK (precio_unitario >= 0),
    subtotal NUMERIC(10,2) NOT NULL CHECK (subtotal >= 0),
    CONSTRAINT fk_detalle_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES producto(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

INSERT INTO categoria (nombre) VALUES
('Textil'),
('Termos y botellas'),
('Gorras y accesorios'),
('Utiles y papeleria');

INSERT INTO producto (nombre, descripcion, precio, imagen, stock, categoria_id) VALUES
('Camiseta UAM Jaguar', 'Camiseta de algodon con el logo bordado del Jaguar UAM. Disponible en tallas S, M, L, XL.', 350.00, 'camiseta-jaguar.jpg', 40, 1),
('Sudadera UAM', 'Sudadera con capucha y estampado institucional. Ideal para el clima fresco del campus.', 750.00, 'sudadera-uam.jpg', 25, 1),
('Polo institucional', 'Polo tipo pique con el escudo de la universidad bordado al pecho.', 480.00, 'polo-uam.jpg', 30, 1),
('Termo acero Jaguar', 'Termo de acero inoxidable 750ml con grabado laser del Jaguar UAM. Mantiene temperatura 12h.', 420.00, 'termo-jaguar.jpg', 35, 2),
('Botella deportiva UAM', 'Botella de 600ml libre de BPA con tapa de seguridad y logo de la universidad.', 220.00, 'botella-uam.jpg', 50, 2),
('Gorra UAM Jaguar', 'Gorra ajustable con bordado frontal del Jaguar. Color azul institucional.', 280.00, 'gorra-jaguar.jpg', 45, 3),
('Llavero metalico UAM', 'Llavero de metal con el escudo de la universidad en relieve.', 90.00, 'llavero-uam.jpg', 80, 3),
('Mochila UAM', 'Mochila resistente con compartimento para laptop y logo institucional.', 890.00, 'mochila-uam.jpg', 20, 3),
('Cuaderno UAM', 'Cuaderno universitario de 100 hojas con portada del Jaguar UAM.', 75.00, 'cuaderno-uam.jpg', 100, 4),
('Taza ceramica Jaguar', 'Taza de ceramica 350ml con el logo del Jaguar UAM. Apta para microondas.', 180.00, 'taza-jaguar.jpg', 60, 4);
