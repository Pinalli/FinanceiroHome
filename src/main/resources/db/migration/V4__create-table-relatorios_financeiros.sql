CREATE TABLE relatorios_financeiros (
                                        id SERIAL PRIMARY KEY,
                                        usuario_id INTEGER NOT NULL,
                                        data_inicio DATE NOT NULL,
                                        data_fim DATE NOT NULL,
                                        FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);