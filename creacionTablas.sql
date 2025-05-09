-- Tabla 'usuarios' (para registro/login y datos de pago)
CREATE TABLE Usuarios (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,          
    nombre VARCHAR(100) NOT NULL,
    numero_tarjeta VARCHAR(16)
);

-- Tabla 'pedidos' (solo importe total, relacionado con usuario)
CREATE TABLE Pedidos (
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    importe_total DECIMAL(10, 2) NOT NULL,   
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
