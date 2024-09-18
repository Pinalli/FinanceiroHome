CREATE TABLE contas_a_receber (
                                  id SERIAL PRIMARY KEY,
                                  descricao VARCHAR(50) NOT NULL,
                                  valor DECIMAL(10, 2) NOT NULL,
                                  data_recebimento DATE NOT NULL,
                                  status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE', -- Enum: 'PENDENTE', 'RECEBIDO'
                                  categoria VARCHAR(50), -- Ex: 'SALÁRIO', 'ALUGUEL', 'INVESTIMENTOS'
                                  usuario_id INTEGER NOT NULL,
                                  FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);