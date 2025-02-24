
CREATE TABLE parcela_compra (
                                id SERIAL PRIMARY KEY,
                                valor DECIMAL(10,2) NOT NULL CHECK (valor > 0),
                                data_vencimento DATE NOT NULL,
                                status_id INT NOT NULL REFERENCES status_parcela_compra(id) ON DELETE RESTRICT,
                                compra_id INT NOT NULL REFERENCES compra_cartao(id) ON DELETE CASCADE,
                                numero_parcela INT NOT NULL CHECK (numero_parcela >= 1),
                                data_pagamento DATE CHECK (data_pagamento >= data_vencimento OR data_pagamento IS NULL)
);

-- CREATE TABLE parcela_compra (


--                                 id SERIAL PRIMARY KEY,
--                                 valor DECIMAL(10,2) NOT NULL CHECK (valor > 0),
--                                 data_vencimento DATE NOT NULL,
--                                 status VARCHAR(10) NOT NULL CHECK (status IN ('PENDENTE', 'PAGA', 'ATRASADA')),
--                                 compra_id INT NOT NULL REFERENCES compra_cartao(id) ON DELETE CASCADE
-- )
--
-- UPDATE parcela_compra pc
-- SET status = (SELECT nome FROM status_parcela_compra spc WHERE spc.id = pc.status_id);