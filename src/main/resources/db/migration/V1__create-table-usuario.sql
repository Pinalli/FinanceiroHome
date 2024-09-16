CREATE TABLE usuarios (
                          id SERIAL PRIMARY KEY,
                          nome VARCHAR(50) NOT NULL,
                          email VARCHAR(100) NOT NULL,
                          senha VARCHAR(255) NOT NULL
);
