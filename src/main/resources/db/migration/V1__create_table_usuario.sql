CREATE TABLE usuarios
(
    id           BIGSERIAL PRIMARY KEY,
    nome         VARCHAR(256) NOT NULL,
    login        VARCHAR(50)  NOT NULL,
    senha        VARCHAR(256) NOT NULL,
    tipo_usuario VARCHAR(50)  NOT NULL,
    criado_em    TIMESTAMP    NOT NULL
);