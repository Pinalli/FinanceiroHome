CREATE TABLE usuarios (
                          id SERIAL PRIMARY KEY,
                          nome VARCHAR(50) NOT NULL,
                          email VARCHAR(100) NOT NULL,
                          senha VARCHAR(255) NOT NULL
);

CREATE TABLE contas (
                        id SERIAL PRIMARY KEY,
                        descricao VARCHAR(50) NOT NULL,
                        valor DECIMAL(10, 2) NOT NULL,
                        tipo BOOLEAN NOT NULL,
                        usuario_id INTEGER NOT NULL,
                        FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE cartoes_credito (
                                 id SERIAL PRIMARY KEY,
                                 descricao VARCHAR(50) NOT NULL,
                                 limite DECIMAL(10, 2) NOT NULL,
                                 valor DECIMAL(10, 2) NOT NULL,
                                 usuario_id INTEGER NOT NULL,
                                 FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
--Essa tabela armazena os relatórios financeiros, incluindo o usuário, a data de início e a data de fim do período do relatório.
CREATE TABLE relatorios_financeiros (
                                        id SERIAL PRIMARY KEY,
                                        usuario_id INTEGER NOT NULL,
                                        data_inicio DATE NOT NULL,
                                        data_fim DATE NOT NULL,
                                        FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE relatorios_financeiros_itens (
                                              id SERIAL PRIMARY KEY,
                                              relatorio_financeiro_id INTEGER NOT NULL,
                                              conta_id INTEGER,
                                              cartao_credito_id INTEGER,
                                              data DATE NOT NULL,
                                              valor DECIMAL(10, 2) NOT NULL,
                                              tipo BOOLEAN NOT NULL,  -- true = receita, false = despesa
                                              FOREIGN KEY (relatorio_financeiro_id) REFERENCES relatorios_financeiros(id),
                                              FOREIGN KEY (conta_id) REFERENCES contas(id),
                                              FOREIGN KEY (cartao_credito_id) REFERENCES cartoes_credito(id)
);

--Essa tabela armazena os itens do relatório financeiro, incluindo a conta ou cartão de crédito relacionado, a data, o valor e o tipo (receita ou despesa).
CREATE TABLE relatorios_financeiros_itens (
                                              id SERIAL PRIMARY KEY,
                                              relatorio_financeiro_id INTEGER NOT NULL,
                                              conta_id INTEGER,
                                              cartao_credito_id INTEGER,
                                              data DATE NOT NULL,
                                              valor DECIMAL(10, 2) NOT NULL,
                                              tipo BOOLEAN NOT NULL,  -- true = receita, false = despesa
                                              FOREIGN KEY (relatorio_financeiro_id) REFERENCES relatorios_financeiros(id),
                                              FOREIGN KEY (conta_id) REFERENCES contas(id),
                                              FOREIGN KEY (cartao_credito_id) REFERENCES cartoes_credito(id)
);

--Essa tabela armazena as notificações enviadas aos usuários, incluindo o usuário, a conta relacionada, o tipo de notificação, a data de envio e a data de vencimento.
--Dessa forma, podemos armazenar e gerenciar as notificações enviadas aos usuários de forma eficiente.

CREATE TABLE notificacoes (
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER NOT NULL,
    conta_id INTEGER NOT NULL,
    tipo notification_type NOT NULL,
    data_envio DATE NOT NULL,
    data_vencimento DATE NOT NULL,
    status BOOLEAN NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (conta_id) REFERENCES contas(id)
);