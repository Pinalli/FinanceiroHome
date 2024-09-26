SELECT
    u.id AS usuario_id,
    u.nome AS nome_usuario,
    u.email AS email_usuario,
    cc.id AS cartao_id,
    cc.descricao AS descricao_cartao_credito,
    cc.valor AS valor_cartao_credito,
    cc.limite AS liimte_cartao
FROM
    usuarios u
        INNER JOIN
    cartoes_credito cc
    ON
        u.id = cc.usuario_id;