-- Tabela contas (ajustada)
CREATE TABLE contas (
                        id SERIAL PRIMARY KEY,
                        descricao VARCHAR(255) NOT NULL,
                        valor NUMERIC(10, 2) NOT NULL,
                        data_vencimento DATE NOT NULL,
                        data_pagamento DATE, -- Novo campo
                        status VARCHAR(10) CHECK (status IN ('PENDENTE', 'PAGA', 'RECEBIDA')),
                        tipo VARCHAR(10) CHECK (tipo IN ('PAGAR', 'RECEBER')),
                        usuario_id INT REFERENCES usuario(id) ON DELETE CASCADE,
                        categoria_id INT REFERENCES categoria(id) ON DELETE RESTRICT -- Não permite excluir categoria em uso
);


CREATE INDEX idx_contas_data ON contas(data);
CREATE INDEX idx_compra_cartao_usuario ON compra_cartao(usuario_id);

-- Validações de Negócio--
--     Ao Criar uma Conta (contas):--
--         Se tipo = PAGAR, a categoria.tipo deve ser DESPESA.--
--         Se tipo = RECEBER, a categoria.tipo deve ser RECEITA.