DROP TABLE IF EXISTS pets;
DROP TABLE IF EXISTS owners;

CREATE TABLE owners
(
    ID SERIAL PRIMARY KEY,
    phoneNumber    BIGINT,
    name      VARCHAR(512)
);

CREATE TABLE pets
(
    ID        SERIAL PRIMARY KEY,
    weight    REAL,
    name      VARCHAR(512),
    species   VARCHAR(512),
    birth_date DATE,
    owner_id BIGINT NOT NULL
);

ALTER TABLE pets ADD CONSTRAINT pet_fk1 FOREIGN KEY (owner_id) REFERENCES owners(id);

INSERT INTO owners (phoneNumber, name)
VALUES (913051482, 'Joao'),
       (910548231, 'Leandro'),
       (912594123, 'Rodrigo');

INSERT INTO pets (weight, name, species, birth_date, owner_id)
VALUES (21.1, 'nit', 'bird', '2023-08-17', 1),
       (13.4, 'roger', 'dog', '2021-06-30', 2),
       (6.8, 'sun', 'cat', '2020-02-08', 2),
       (33.9, 'mike', 'dog', '2019-05-09', 3),
       (5.5, 'light', 'dog', '2018-08-14', 1),
       (7.1, 'wolf', 'cat', '2017-11-17', 1),
       (12.1, 'spike', 'cat', '2016-01-11', 1),
       (17.7, 'adolf', 'cat', '2015-01-25', 3),
       (24.5, 'joe', 'dog', '2013-04-02', 1),
       (3.2, 'white', 'chicken', '2015-07-27', 3),
       (6.3, 'night', 'dog', '2021-08-04', 1),
       (8.7, 'jan', 'cat', '2022-09-05', 3);
