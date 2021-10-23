DROP TABLE IF EXISTS manager;
CREATE TABLE manager
(
    no bigserial not null primary key,
    label varchar(50),
    param1 varchar(50)
);

