CREATE TABLE conta_receber (
                              id SERIAL PRIMARY KEY,
                              descricao VARCHAR(255) NOT NULL,
                              valor DECIMAL(10, 2) NOT NULL,
                              data_recebimento DATE NOT NULL,
                              status VARCHAR(50) NOT NULL,
                              categoria VARCHAR(100),
                              usuario_id INT REFERENCES usuario(id) ON DELETE CASCADE
);