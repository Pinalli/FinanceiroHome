CREATE TABLE compras (
                         id SERIAL PRIMARY KEY,
                         cartao_credito_id BIGINT,
                         categoria VARCHAR(255),
                         data TIMESTAMP,
                         descricao TEXT,
                         parcelas INT,
                         parcelas_pagas INT,
                         valor DECIMAL(10, 2),
                         FOREIGN KEY (cartao_credito_id) REFERENCES cartoes_credito(id)
);