CREATE TABLE usuario (
                         id SERIAL PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         email VARCHAR(100) UNIQUE NOT NULL,
                         senha VARCHAR(255) NOT NULL,
                         data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION update_usuario_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER usuario_update_trigger
    BEFORE UPDATE ON usuario
    FOR EACH ROW
    EXECUTE FUNCTION update_usuario_timestamp();