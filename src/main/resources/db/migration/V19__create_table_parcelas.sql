-- Primeiro, cria o tipo ENUM
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'status_parcela_enum') THEN
CREATE TYPE status_parcela_enum AS ENUM ('pendente', 'paga', 'cancelada');
END IF;
END $$;

-- Depois, cria a tabela usando esse tipo
CREATE TABLE parcelas (
                          id SERIAL PRIMARY KEY,
                          valor DECIMAL(10,2) NOT NULL,
                          data_vencimento DATE NOT NULL,
                          data_pagamento DATE,
                          status status_parcela_enum NOT NULL DEFAULT 'pendente',
                          juros DECIMAL(10,2),
                          multa DECIMAL(10,2),
                          observacao TEXT,
                          compra_id INT NOT NULL,
                          FOREIGN KEY (compra_id) REFERENCES compras(id)
);
