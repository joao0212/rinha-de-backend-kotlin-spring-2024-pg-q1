create table if not exists cliente (
    id int,
    limite int,
    saldo int,
    primary key(id)
);

create table if not exists transacao (
    id serial,
    valor float,
    tipo varchar(1),
    descricao varchar(10),
    id_cliente int,
    realizada_em timestamp,
    primary key(id)
);

insert into cliente(id, limite, saldo) values (1, 100000, 0);
insert into cliente(id, limite, saldo) values (2, 80000, 0);
insert into cliente(id, limite, saldo) values (3, 1000000, 0);
insert into cliente(id, limite, saldo) values (4, 10000000, 0);
insert into cliente(id, limite, saldo) values (5, 500000, 0);