INSERT INTO
    USUARIOS (id, username, password, role)
VALUES (
        101, 'admin@email.com', '$2a$12$7QTKp8HZHeokXLlV3NU/f.MEeuh2ij7cT3c.QdhBChf.h.gkXGfc.', 'ROLE_ADMIN'
    );

INSERT INTO
    USUARIOS (id, username, password, role)
VALUES (
        102, 'client@email.com', '$2a$12$7QTKp8HZHeokXLlV3NU/f.MEeuh2ij7cT3c.QdhBChf.h.gkXGfc.', 'ROLE_CLIENTE'
    );

INSERT INTO
    USUARIOS (id, username, password, role)
VALUES (
        103, 'rafael@email.com', '$2a$12$7QTKp8HZHeokXLlV3NU/f.MEeuh2ij7cT3c.QdhBChf.h.gkXGfc.', 'ROLE_CLIENTE'
    );


INSERT INTO
    CLIENTES (id, nome, cpf, id_usuario) VALUES (100, 'Ricardo Pereira', '32268430014', 102);

INSERT INTO
    CLIENTES (id, nome, cpf, id_usuario) VALUES (200, 'Rafael Silva', '25811504080', 103);



INSERT INTO
    VAGAS (id, codigo, status) VALUES (10, 'T-01', 'LIVRE');

INSERT INTO
    VAGAS (id, codigo, status) VALUES (20, 'T-02', 'LIVRE');

INSERT INTO
    VAGAS (id, codigo, status) VALUES (30, 'T-03', 'LIVRE');

INSERT INTO
    VAGAS (id, codigo, status) VALUES (40, 'T-04', 'OCUPADA');

INSERT INTO
    VAGAS (id, codigo, status) VALUES (50, 'T-05', 'OCUPADA');


INSERT INTO
    CLIENTES_TEM_VAGAS (numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
VALUES (
        '20240501-095400', 'FRD-1234', 'FORD','ECOSPORT', 'Preto', '2024-05-01 09:54:00', 100, 40
    );

INSERT INTO
    CLIENTES_TEM_VAGAS (numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
VALUES (
        '20240501-095500', 'PLP-1234', 'FIAT', 'ARGO', 'BRANCO', '2024-05-01 09:55:00', 200, 50
    );