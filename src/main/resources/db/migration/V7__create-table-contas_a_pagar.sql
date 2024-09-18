CREATE TABLE contas_a_pagar (
                                id SERIAL PRIMARY KEY,
                                descricao VARCHAR(50) NOT NULL,
                                valor DECIMAL(10, 2) NOT NULL,
                                data_vencimento DATE NOT NULL,
                                status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE', -- Enum: 'PENDENTE', 'PAGO'
                                categoria VARCHAR(50), -- Ex: 'ALIMENTAÇÃO', 'MORADIA', 'TRANSPORTE'
                                usuario_id INTEGER NOT NULL,
                                FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);