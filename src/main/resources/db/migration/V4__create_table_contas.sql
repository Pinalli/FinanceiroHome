-- Tabela contas (ajustada)
CREATE TABLE contas (
                        id SERIAL PRIMARY KEY,
                        descricao VARCHAR(255) NOT NULL,
                        valor NUMERIC(10, 2) NOT NULL,
                        data_vencimento DATE NOT NULL,
                        data_pagamento DATE CHECK (data_pagamento >= data_vencimento OR data_pagamento IS NULL),
                        status VARCHAR(10) CHECK (status IN ('PENDENTE', 'PAGA', 'RECEBIDA')),
                        tipo VARCHAR(10) CHECK (tipo IN ('PAGAR', 'RECEBER')),
                        observacao TEXT,
                        usuario_id INT REFERENCES usuario(id) ON DELETE CASCADE,
                        categoria_id INT REFERENCES categoria(id) ON DELETE RESTRICT -- Não permite excluir categoria em uso
);

CREATE INDEX idx_contas_usuario_data ON contas(usuario_id, data_vencimento);

-- Remover a restrição atual na coluna data_pagamento
ALTER TABLE contas
ALTER COLUMN data_pagamento DROP CONSTRAINT;

-- Opcionalmente, adicionar uma nova restrição que apenas verifica se o status está consistente
-- com a presença de data_pagamento, sem verificar a relacao com data_vencimento
ALTER TABLE contas
    ADD CONSTRAINT check_pagamento_status
        CHECK ((status IN ('PAGA', 'RECEBIDA') AND data_pagamento IS NOT NULL) OR
               (status = 'PENDENTE' AND data_pagamento IS NULL));

-- Validações de Negócio--
--     Ao Criar uma Conta (contas):--
--         Se tipo = PAGAR, a categoria.tipo deve ser DESPESA.--
--         Se tipo = RECEBER, a categoria.tipo deve ser RECEITA.