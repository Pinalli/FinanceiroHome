CREATE TABLE relatorios_financeiros (
                                     id SERIAL PRIMARY KEY,
                                     tipo_relatorio VARCHAR(100) NOT NULL,
                                     periodo VARCHAR(50) NOT NULL,
                                     dados_relatorio TEXT,
                                     usuario_id INT REFERENCES usuario(id) ON DELETE CASCADE
);