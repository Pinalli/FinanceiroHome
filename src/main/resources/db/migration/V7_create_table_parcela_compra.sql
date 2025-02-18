CREATE TABLE parcela_compra (
                                id SERIAL PRIMARY KEY,
                                valor DECIMAL(10,2) NOT NULL CHECK (valor > 0),
                                data_vencimento DATE NOT NULL,
                                status VARCHAR(10) NOT NULL CHECK (status IN ('PENDENTE', 'PAGA', 'ATRASADA')),
                                compra_id INT NOT NULL REFERENCES compra_cartao(id) ON DELETE CASCADE
)