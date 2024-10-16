--ALTER TABLE compras ADD COLUMN status_id INT REFERENCES status_compras(id);
--ALTER TABLE compras ADD COLUMN limite_disponivel_momento_compra DECIMAL(10,2);
ALTER TABLE compras ADD COLUMN data_limite TIMESTAMP;