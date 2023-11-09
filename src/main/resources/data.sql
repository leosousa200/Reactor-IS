DROP TABLE IF EXISTS pets;
DROP TABLE IF EXISTS owners;

CREATE TABLE owners
(
    ID SERIAL PRIMARY KEY,
    phone_number    BIGINT NOT NULL,
    name      VARCHAR(512) NOT NULL
);

CREATE TABLE pets
(
    ID        SERIAL PRIMARY KEY,
    weight    NUMERIC(4, 2) NOT NULL,
    name      VARCHAR(512) NOT NULL,
    species   VARCHAR(512) NOT NULL,
    birth_date DATE NOT NULL,
    owner_id BIGINT NOT NULL
);

ALTER TABLE pets ADD CONSTRAINT pet_fk1 FOREIGN KEY (owner_id) REFERENCES owners(id);

INSERT INTO owners (phone_number, name)
VALUES (913051482, 'Joao'),
       (910548231, 'Carla'),
       (912951239, 'Rodrigo'),
       (948496604, 'Ricardo'),
       (912516391, 'Joana'),
       (971540879, 'Celeste'),
       (967428763, 'Miguel'),
       (978924477, 'Artur'),
       (986241282, 'Marcelo'),
       (968419716, 'Beatriz'),
       (974840399, 'Ana'),
       (922054732, 'Marco'),
       (944380186, 'Salvador'),
       (941283811, 'Mariana'),
       (963423738, 'Dulce'),
       (912594123, 'Maria');

INSERT INTO pets (weight, name, species, birth_date, owner_id)
VALUES (6.5, 'Zeda', 'bird', '2013-05-12', 1),
       (3.3, 'Igor', 'dog', '2013-08-25', 1),
       (6.8, 'Idobe', 'cat', '2013-10-02', 3),
       (8.4, 'Black Jack', 'dog', '2014-01-08', 6),
       (13.0, 'Jamila', 'dog', '2014-10-26', 7),
       (19.3, 'Yarni', 'cat', '2015-01-01', 15),
       (6.0, 'Baleen', 'cat', '2015-08-07', 16),
       (5.0, 'Rune', 'cat', '2019-12-28', 16),
       (3.7, 'Indy', 'dog', '2021-06-29', 7),
       (7.3, 'Genda', 'chicken', '2018-08-09', 3),
       (21.1, 'Zora', 'dog', '2021-08-04', 12),
       (35.1, 'Queenie', 'dog', '2016-09-12', 2),
       (12.0, 'Yumi', 'cat', '2022-04-11', 2),
       (6.5, 'Axel', 'dog', '2023-07-20', 8),
       (9.7, 'Zami', 'dog', '2019-02-25', 9),
       (5.9, 'Figo', 'cat', '2020-07-03', 8),
       (12.5, 'Wade', 'cat', '2015-03-13', 5),
       (17.8, 'Dolina', 'cat', '2016-10-01', 3),
       (13.4, 'Travis', 'dog', '2023-04-03', 1),
       (5.4, 'Rylie', 'chicken', '2020-12-05', 10),
       (2.2, 'Jamie', 'dog', '2021-02-24', 11),
       (8.8, 'Kelly', 'dog', '2019-03-04', 11),
       (8.5, 'Mazie', 'cat', '2017-02-25', 6),
       (21.4, 'Tahnee', 'dog', '2014-03-01', 5),
       (6.8, 'Curtis', 'dog', '2013-01-13', 5),
       (17.2, 'Tricky', 'cat', '2020-01-09', 5),
       (14.2, 'Trip', 'cat', '2016-01-13', 12),
       (6.6, 'Hutch', 'cat', '2017-08-05', 12),
       (4.8, 'Keane', 'dog', '2019-02-28', 7),
       (9.2, 'Tesla', 'chicken', '2019-03-16', 3),
       (19.3, 'Xarex', 'dog', '2019-12-17', 1),
       (5.7, 'Frank', 'dog', '2022-08-01', 14),
       (7.2, 'Yeska', 'cat', '2022-08-17', 7),
       (9.6, 'Wade', 'cat', '2016-07-27', 3),
       (3.8, 'Zoe', 'cat', '2017-10-11', 15),
       (14.2, 'Xenos', 'dog', '2013-12-09', 16),
       (13.1, 'Venus', 'chicken', '2022-01-21', 16),
       (16.9, 'Pablo', 'dog', '2020-09-23', 9),
       (18.7, 'Hammi', 'cat', '2019-06-27', 10);
