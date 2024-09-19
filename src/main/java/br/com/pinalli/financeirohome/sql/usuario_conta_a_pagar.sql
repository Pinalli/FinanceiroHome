SELECT
    u.id AS usuario_id,
    u.nome AS nome_usuario,
    u.email AS email_usuario,
    cp.id AS conta_id,
    cp.descricao AS descricao_conta,
    cp.valor AS valor_conta,
    cp.data_vencimento,
    cp.status,
    cp.categoria
FROM
    usuarios u
        INNER JOIN
    contas_a_pagar cp
    ON
        u.id = cp.usuario_id;
