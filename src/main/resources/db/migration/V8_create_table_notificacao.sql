
CREATE TABLE notificacao (
                             id SERIAL PRIMARY KEY,
                             mensagem VARCHAR(255) NOT NULL,
                             data_notificacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             lida BOOLEAN NOT NULL DEFAULT false,
                             data_leitura TIMESTAMP, -- Novo campo para registrar quando a notificação foi lida
                             usuario_id INT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
                             conta_id INT REFERENCES contas(id) ON DELETE SET NULL,
                             compra_cartao_id INT REFERENCES compra_cartao(id) ON DELETE SET NULL, -- Novo campo para compras no cartão
                             parcela_compra_id INT REFERENCES parcela_compra(id) ON DELETE SET NULL,-- Novo campo para parcelas de compras no cartão
                             tipo_notificacao VARCHAR(50) NOT NULL -- Novo campo para o tipo de notificação
);

CREATE INDEX idx_notificacao_usuario_lida ON notificacao(usuario_id, lida);


-- CREATE TABLE notificacao (
--                              id SERIAL PRIMARY KEY,
--                              mensagem VARCHAR(255) NOT NULL,
--                              data_notificacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--                              lida BOOLEAN NOT NULL DEFAULT false,
--                              usuario_id INT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
--                              conta_id INT REFERENCES contas(id) ON DELETE SET NULL
-- );