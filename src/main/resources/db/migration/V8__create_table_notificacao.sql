CREATE TABLE notificacao (
                             id SERIAL PRIMARY KEY,
                             mensagem TEXT NOT NULL,
                             data_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             status VARCHAR(50) NOT NULL,
                             usuario_id INT REFERENCES usuario(id) ON DELETE CASCADE
);