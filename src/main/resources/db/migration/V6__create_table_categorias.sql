CREATE TABLE categorias (
                            id SERIAL PRIMARY KEY,
                            nome VARCHAR(50) NOT NULL,
                            tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('DESPESA', 'RECEITA', 'CARTAO'))
);

-- Para contas_pagar e contas_receber:
ALTER TABLE conta_pagar ADD COLUMN categoria_id INT REFERENCES categorias(id);
ALTER TABLE conta_receber ADD COLUMN categoria_id INT REFERENCES categorias(id);
-- Para compra_cartao:
ALTER TABLE compra_cartao ADD COLUMN categoria_id INT REFERENCES categorias(id);