CREATE TABLE contas (
                        id SERIAL PRIMARY KEY,
                        descricao VARCHAR(255) NOT NULL,
                        valor DECIMAL(10,2) NOT NULL CHECK (valor > 0),
                        data DATE NOT NULL,
                        tipo VARCHAR(8) NOT NULL CHECK (tipo IN ('PAGAR', 'RECEBER')),
                        status VARCHAR(10) NOT NULL CHECK (status IN ('PENDENTE', 'PAGO', 'RECEBIDO')),
                        categoria_id INT NOT NULL REFERENCES categories(id),
                        usuario_id INT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_contas_data ON contas(data);
CREATE INDEX idx_compra_cartao_usuario ON compra_cartao(usuario_id);