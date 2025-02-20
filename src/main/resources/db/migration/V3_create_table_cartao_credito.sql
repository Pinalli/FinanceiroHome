CREATE TABLE cartao_credito (
                                id SERIAL PRIMARY KEY,
                                bandeiraCartao VARCHAR(100) NOT NULL,
                                numero VARCHAR(20) NOT NULL,
                                dia_fechamento INT NOT NULL CHECK (dia_fechamento BETWEEN 1 AND 31),
                                dia_vencimento INT NOT NULL CHECK (dia_vencimento BETWEEN 1 AND 31),
                                limite_total DECIMAL(10,2) NOT NULL CHECK (limite_total > 0),
                                limite_disponivel DECIMAL(10,2) NOT NULL CHECK (limite_disponivel >= 0),
                                usuario_id INT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE
);

-- View para cálculo automático
CREATE VIEW view_limite_cartao AS
SELECT
    cc.id,
    cc.limite_total,
    (cc.limite_total - COALESCE(SUM(c.valor_total), 0)) AS limite_disponivel
FROM cartao_credito cc
         LEFT JOIN compra_cartao c ON cc.id = c.cartao_id
GROUP BY cc.id;