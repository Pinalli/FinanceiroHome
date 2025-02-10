CREATE TABLE parcela_compra (
                               id SERIAL PRIMARY KEY,
                               compra_id INT REFERENCES compra_cartao(id) ON DELETE CASCADE,
                               numero_parcela INT NOT NULL,
                               valor_parcela DECIMAL(10, 2) NOT NULL,
                               data_vencimento DATE NOT NULL,
                               status VARCHAR(50) NOT NULL
);