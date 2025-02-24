-- Criação da tabela usuario
CREATE TABLE usuario (
                         id SERIAL PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         email VARCHAR(100) UNIQUE NOT NULL,
                         senha VARCHAR(255) NOT NULL,
                         data_criacao TIMESTAMP WITH TIME ZONE,
                         data_atualizacao TIMESTAMP WITH TIME ZONE,
                         ativo BOOLEAN DEFAULT TRUE
);

-- Função para atualizar data_atualizacao
CREATE OR REPLACE FUNCTION update_usuario_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    -- Atualiza data_atualizacao apenas durante operações de UPDATE
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para chamar a função durante operações de UPDATE
CREATE TRIGGER usuario_update_trigger
    BEFORE UPDATE ON usuario
    FOR EACH ROW
    EXECUTE FUNCTION update_usuario_timestamp();