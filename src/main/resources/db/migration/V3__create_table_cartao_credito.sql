CREATE TABLE cartao_credito (
                                id SERIAL PRIMARY KEY,
                                nome VARCHAR(100), -- Nome personalizado do cartão (ex: "Cartão Nubank")
                                bandeira_cartao VARCHAR(100) NOT NULL, -- Bandeira do cartão (ex: "Visa", "Mastercard")
                                numero VARCHAR(20) NOT NULL,
                                numero_criptografado VARCHAR(255),
                                limite_total NUMERIC(10, 2) NOT NULL,
                                limite_disponivel NUMERIC(10, 2) NOT NULL,
                                total_compras_abertas NUMERIC(10, 2) NOT NULL,
                                dia_fechamento INT NOT NULL CHECK (dia_fechamento BETWEEN 1 AND 31),
                                dia_vencimento INT NOT NULL CHECK (dia_vencimento BETWEEN 1 AND 31),
                                usuario_id INT NOT NULL REFERENCES usuario(id),
                                ativo BOOLEAN DEFAULT TRUE, -- Para desativar cartões sem excluí-los
                                CONSTRAINT limite_disponivel_check CHECK (limite_disponivel <= limite_total)
);
