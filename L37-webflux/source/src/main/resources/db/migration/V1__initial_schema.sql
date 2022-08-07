create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

create table address
(
    client_id  bigint not null primary key,
    street varchar(50),
        CONSTRAINT client_id_FK
            FOREIGN KEY (client_id) REFERENCES client (id)
);

create table phone
(
    id   bigserial not null primary key,
    client_id bigint not null,
    number varchar(50),
    CONSTRAINT client_id_FK
         FOREIGN KEY (client_id) REFERENCES client (id)
);
