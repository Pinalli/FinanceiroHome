CREATE TABLE categoria (
                           id SERIAL PRIMARY KEY,
                           nome VARCHAR(50) NOT NULL,
                           tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('DESPESA', 'RECEITA')), -- Define o tipo da categoria
                           usuario_id INT REFERENCES usuario(id) ON DELETE CASCADE, -- Categorias do usuário (NULL = categoria padrão)
                           CONSTRAINT categoria_unica_por_usuario UNIQUE (nome, tipo, usuario_id) -- Evita duplicatas
);

INSERT INTO categoria (nome, tipo, usuario_id) VALUES
                                                   ('Alimentação', 'DESPESA', NULL),
                                                   ('Transporte', 'DESPESA', NULL),
                                                   ('Salário', 'RECEITA', NULL);



-- Campo	Descrição
-- nome   	Nome da categoria (ex: "Aluguel", "Salário").
-- tipo  	Define se a categoria é para DESPESA (contas a pagar) ou RECEITA (contas a receber).

-- usuario_id	NULL = categoria padrão do sistema (ex: "Alimentação").
--               Preenchido = categoria personalizada do usuário.

--     Ao Criar uma Categoria:--
--        O usuário só pode criar categorias com tipo compatível
--        com o contexto (ex: não criar "Salário" como DESPESA)