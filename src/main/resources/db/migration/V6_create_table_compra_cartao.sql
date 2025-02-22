CREATE TABLE compra_cartao (
                               id SERIAL PRIMARY KEY,
                               descricao VARCHAR(255) NOT NULL,
                               valor_total DECIMAL(10,2) NOT NULL CHECK (valor_total > 0),
                               quantidade_parcelas INT NOT NULL CHECK (quantidade_parcelas >= 1),
                               data_compra DATE NOT NULL,
                               observacao TEXT,
                               cartao_id INT NOT NULL REFERENCES cartao_credito(id) ON DELETE CASCADE,
                               categoria_id INT NOT NULL REFERENCES categoria(id) ON DELETE RESTRICT,
                               usuario_id INT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE
);

CREATE INDEX idx_compra_cartao_usuario_data ON compra_cartao(usuario_id, data_compra);