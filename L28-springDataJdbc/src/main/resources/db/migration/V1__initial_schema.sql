create table address
(
    id  bigserial not null primary key,
    street varchar(50)
);

create table client
(
    id   bigserial not null primary key,
    name varchar(50),
    address_id bigint null,
    CONSTRAINT address_id_FK
         FOREIGN KEY (address_id) REFERENCES address (id)
);

create table phone
(
    id   bigserial not null primary key,
    client_id bigint not null,
    number varchar(50),
    CONSTRAINT client_id_FK
         FOREIGN KEY (client_id) REFERENCES client (id)
);
