
--cartao/usuario
SELECT
    u.id,
    u.nome,
    u.email,
    c.id,
    c.descricao,
    c.limite,
    c.valor
FROM
    usuarios u
        JOIN cartoes_credito c ON u.id = c.usuario_id;

-----------------------------------------------------------------