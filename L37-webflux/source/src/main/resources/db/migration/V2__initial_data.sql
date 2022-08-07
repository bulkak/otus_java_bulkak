insert into client (name)
values
    ('Vasya'),
    ('Petya'),
    ('Igor'),
    ('Chupakabra'),
    ('Mr. Smith');

insert into address (client_id, street)
    values
    (1, 'any address 1'),
    (2, 'any address 2'),
    (3, 'any address 3'),
    (4, 'any address 3'),
    (5, 'any address 3');

insert into phone (client_id, number)
    values
    (1, '+75006060581'),
    (1, '+75006060582'),
    (2, '+75006060583'),
    (2, '+75006060584'),
    (3, '+75006060585'),
    (3, '+75006060586'),
    (4, '+75006060587'),
    (5, '+75006060588'),
    (5, '+75006060589')
    ;