CREATE TYPE tipo_conta AS ENUM ('PAGAR', 'RECEBER');
CREATE TYPE status_conta AS ENUM ('PENDENTE', 'PAGO', 'ATRASADO', 'CANCELADO');

CREATE TABLE contas (
                        id BIGSERIAL PRIMARY KEY,
                        descricao VARCHAR(100) NOT NULL,
                        valor DECIMAL(10,2) NOT NULL,
                        data_vencimento DATE NOT NULL,
                        data_pagamento DATE, -- Para PAGAR
                        data_recebimento DATE, -- Para RECEBER
                        tipo tipo_conta NOT NULL,
                        status status_conta NOT NULL,
                        categoria_id BIGINT  REFERENCES categorias(id),
                        recorrente BOOLEAN DEFAULT FALSE,
                        periodicidade VARCHAR(20) CHECK (periodicidade IN ('MENSAL', 'ANUAL', 'SEMANAL', NULL)),
                        data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        data_atualizacao TIMESTAMP,
                        observacao TEXT,
                        usuario_id BIGINT NOT NULL REFERENCES usuario(id),
                        CONSTRAINT chk_recorrencia_valida CHECK (recorrente = FALSE OR periodicidade IS NOT NULL)
);

