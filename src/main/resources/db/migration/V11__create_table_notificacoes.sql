
CREATE TYPE notification_type AS ENUM ('EMAIL', 'MENSAGEM_TEXTO');

CREATE TABLE notificacoes (
                              id SERIAL PRIMARY KEY,
                              usuario_id INTEGER NOT NULL,
                              conta_id INTEGER NOT NULL,
                              tipo notification_type NOT NULL,
                              data_envio DATE NOT NULL,
                              data_vencimento DATE NOT NULL,
                              status BOOLEAN NOT NULL,
                              FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
                              FOREIGN KEY (conta_id) REFERENCES contas(id)
);