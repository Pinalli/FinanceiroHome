CREATE TABLE status_compras (
                                id SERIAL PRIMARY KEY,
                                descricao VARCHAR(20) NOT NULL
);

ALTER TABLE compras ADD COLUMN status_id INT REFERENCES status_compras(id);