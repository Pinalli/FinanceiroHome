CREATE TABLE compra_cartao (
                               id SERIAL PRIMARY KEY,
                               descricao VARCHAR(255) NOT NULL,
                               valor_total NUMERIC(10,2) NOT NULL,
                               data_compra DATE NOT NULL,
                               quantidade_parcelas INT NOT NULL CHECK (quantidade_parcelas >= 1),
                               usuario_id INT REFERENCES usuario(id) ON DELETE CASCADE,
                               cartao_credito_id INT REFERENCES cartao_credito(id) ON DELETE CASCADE,
                               categoria_id INT REFERENCES categoria(id) ON DELETE RESTRICT -- Garante que a categoria é do tipo DESPESA
);


-- Validações de Negócio-
--     Ao Criar uma Compra no Cartão (compra_cartao):--
--         A categoria.tipo deve ser DESPESA.
.