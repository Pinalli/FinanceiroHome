-- Migração V20__create_table_status_parcelas.sql

-- Create the enum type first
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'status_parcelas_enum') THEN
CREATE TYPE status_parcelas_enum AS ENUM ('pendente', 'paga', 'cancelada');
END IF;
END $$;

-- Now create the table using the enum type
CREATE TABLE status_parcelas (
                                 id SERIAL PRIMARY KEY,
                                 parcela_id INT NOT NULL,
                                 status status_parcelas_enum NOT NULL DEFAULT 'pendente',
                                 data_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 FOREIGN KEY (parcela_id) REFERENCES parcelas(id)
);