
CREATE TABLE notificacao (
                             id SERIAL PRIMARY KEY,
                             usuario_id INT NOT NULL,
                             mensagem TEXT NOT NULL,
                             data_criacao TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                             lida BOOLEAN DEFAULT FALSE,
                             FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);