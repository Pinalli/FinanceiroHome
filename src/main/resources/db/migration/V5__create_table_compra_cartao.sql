CREATE TABLE compra_cartao (
                              id SERIAL PRIMARY KEY,
                              descricao VARCHAR(255) NOT NULL,
                              valor DECIMAL(10, 2) NOT NULL,
                              data_compra DATE NOT NULL,
                              categoria VARCHAR(100),
                              parcelas INT,
                              cartao_id INT REFERENCES cartao_credito(id) ON DELETE CASCADE,
                              usuario_id INT REFERENCES usuario(id) ON DELETE CASCADE
);