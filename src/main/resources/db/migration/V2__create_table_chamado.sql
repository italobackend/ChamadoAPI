CREATE TABLE chamados
(
    id           BIGSERIAL PRIMARY KEY,
    descricao    VARCHAR(256) NOT NULL,
    usuario_id   BIGINT       NOT NULL,
    tipo_chamado VARCHAR(100) NOT NULL,
    criado_em    TIMESTAMP    NOT NULL,

    CONSTRAINT fk_chamados_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
);