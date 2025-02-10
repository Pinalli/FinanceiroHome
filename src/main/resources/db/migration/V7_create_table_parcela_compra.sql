CREATE TABLE parcela_compra (
                                id SERIAL PRIMARY KEY,
                                numero_parcela INT NOT NULL CHECK (numero_parcela >= 1),
                                valor_parcela DECIMAL(10,2) NOT NULL CHECK (valor_parcela > 0),
                                data_vencimento DATE NOT NULL,
                                status_id INT NOT NULL REFERENCES status_parcela_compra(id),
                                compra_id INT NOT NULL REFERENCES compra_cartao(id) ON DELETE CASCADE,
                                usuario_id INT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE
);