-- Creación de la tabla de clientes
CREATE TABLE customers (
                           id UUID PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           email VARCHAR(255) NOT NULL UNIQUE,
                           password VARCHAR(255) NOT NULL,
                           created TIMESTAMP NOT NULL,
                           modified TIMESTAMP,
                           last_login TIMESTAMP,
                           token VARCHAR(512),
                           is_active BOOLEAN
);

-- Creación de la tabla de teléfonos con su respectiva llave foránea
CREATE TABLE phones (
                        id UUID PRIMARY KEY,
                        number VARCHAR(255),
                        city_code VARCHAR(255),
                        country_code VARCHAR(255),
                        customer_id UUID,
                        CONSTRAINT fk_phone_customer
                            FOREIGN KEY (customer_id)
                                REFERENCES customers(id)
                                ON DELETE CASCADE
);