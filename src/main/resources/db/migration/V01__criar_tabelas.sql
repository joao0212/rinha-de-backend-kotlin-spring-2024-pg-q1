create table if not exists cliente (
    id int,
    limite int,
    saldo int,
    primary key(id)
);

create table if not exists transacao (
    valor float,
    tipo varchar(1),
    descricao varchar(10),
    id_cliente  int not null references cliente (id),
    realizada_em timestamp
);

insert into cliente(id, limite, saldo) values (1, 100000, 0);
insert into cliente(id, limite, saldo) values (2, 80000, 0);
insert into cliente(id, limite, saldo) values (3, 1000000, 0);
insert into cliente(id, limite, saldo) values (4, 10000000, 0);
insert into cliente(id, limite, saldo) values (5, 500000, 0);