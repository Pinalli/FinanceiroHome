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

CREATE TABLE relatorios_financeiros (
                                        id SERIAL PRIMARY KEY,
                                        data DATE NOT NULL,
                                        valor_total DECIMAL(10, 2) NOT NULL,
                                        usuario_id INTEGER NOT NULL,
                                        FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);