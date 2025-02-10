CREATE TABLE notificacao (
                             id SERIAL PRIMARY KEY,
                             mensagem VARCHAR(255) NOT NULL,
                             data_notificacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             lida BOOLEAN NOT NULL DEFAULT false,
                             usuario_id INT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
                             conta_id INT REFERENCES contas(id) ON DELETE SET NULL
);