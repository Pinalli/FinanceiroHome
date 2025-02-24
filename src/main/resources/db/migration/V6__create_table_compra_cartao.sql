CREATE TABLE compra_cartao (
                               id SERIAL PRIMARY KEY,
                               descricao VARCHAR(255) NOT NULL,
                               valor_total DECIMAL(10,2) NOT NULL CHECK (valor_total > 0),
                               quantidade_parcelas INT NOT NULL CHECK (quantidade_parcelas >= 1),
                               data_compra DATE NOT NULL,
                               observacao TEXT,
                               cartao_id INT NOT NULL REFERENCES cartao_credito(id) ON DELETE SET NULL,
                               categoria_id INT NOT NULL REFERENCES categoria(id) ON DELETE RESTRICT,
                               usuario_id INT NOT NULL REFERENCES usuario(id) ON DELETE SET NULL
);

ALTER TABLE compra_cartao
    ALTER COLUMN cartao_id SET DATA TYPE BIGINT;

-- Remove a view temporariamente
DROP VIEW IF EXISTS view_limite_cartao;

-- Altera a coluna
ALTER TABLE cartao_credito ALTER COLUMN id SET DATA TYPE BIGINT;

-- Recria a view
CREATE VIEW view_limite_cartao AS
SELECT
    cc.id,
    cc.limite_total,
    (cc.limite_total - COALESCE(SUM(c.valor_total), 0)) AS limite_disponivel
FROM cartao_credito cc
         LEFT JOIN compra_cartao c ON cc.id = c.cartao_id
GROUP BY cc.id;
