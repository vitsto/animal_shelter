-- liquibase formatted sql

-- changeset marina:1

INSERT INTO pet_type (id, type_name)
VALUES (1, 'dog'), (2, 'cat'), (3, 'dog-cat');

-- changeset marina:2

INSERT INTO recommendation (recommendation_name, description, pet_type_id)
VALUES ('Рекомендации по технике безопасности на территории приюта', 'https://clck.ru/34GgpW', 3);

-- changeset marina:3

INSERT INTO recommendation (recommendation_name, description, pet_type_id)
VALUES ('Правила знакомства с животным до того, как забрать его из приюта', 'https://clck.ru/34Gguo', 3);

-- changeset marina:4

INSERT INTO recommendation (recommendation_name, description, pet_type_id)
VALUES ('Список документов, необходимых для того, чтобы взять животное из приюта', 'https://clck.ru/34Ggwt', 3);

-- changeset marina:5

INSERT INTO recommendation (recommendation_name, description, pet_type_id)
VALUES ('Список рекомендаций по транспортировке животного', 'https://clck.ru/34Ggyv', 3);

-- changeset marina:6

INSERT INTO recommendation (recommendation_name, description, pet_type_id)
VALUES ('Список рекомендаций по обустройству дома для щенка', 'https://clck.ru/34Gh34', 1);

-- changeset marina:7

INSERT INTO recommendation (recommendation_name, description, pet_type_id)
VALUES ('Список рекомендаций по обустройству дома для котёнка', 'https://clck.ru/34Gh4G', 2);

-- changeset marina:8

INSERT INTO recommendation (recommendation_name, description, pet_type_id)
VALUES ('Список рекомендаций по обустройству дома для взрослого животного', 'https://clck.ru/34Gh6D', 3);

-- changeset marina:9

INSERT INTO recommendation (recommendation_name, description, pet_type_id)
VALUES ('Список рекомендаций по обустройству дома для животного с ограниченными возможностями (зрение, передвижение)',
        'https://clck.ru/34GhcR', 3);

-- changeset marina:10

INSERT INTO recommendation (recommendation_name, description, pet_type_id)
VALUES ('Советы кинолога по первичному общению с собакой', 'https://clck.ru/34GhfW', 1);

-- changeset marina:11

INSERT INTO recommendation (recommendation_name, description, pet_type_id)
VALUES ('Рекомендации по проверенным кинологам для дальнейшего обращения к ним', 'https://clck.ru/34Ghox', 1);

-- changeset marina:12

INSERT INTO recommendation (recommendation_name, description, pet_type_id)
VALUES ('Список причин, почему могут отказать и не дать забрать собаку из приюта', 'https://clck.ru/34Ghs9', 1);

-- changeset marina:13

INSERT INTO shelter (name, address, schedule, location_map, pet_type_id)
VALUES ('Верный друг', 'Московская область, г. Реутов, ул. Сиреневая, д.5/9', 'Пн-Пт с 12:00 до 20:00, Сб-Вс с 14:00 до 21:00',
        'Первый съезд с МКАД. На ул. Сиреневой третье здание от гаражного кооператива.', 1),
    ('Счастливый кот', 'Московская область, г. Балашиха, ул. Южная, д.96', 'Пн-Пт с 12:00 до 20:00, Сб-Вс с 14:00 до 21:00',
     'Второй съезд с МКАД. На ул. Южной двухэтажное зелёное здание после автобусного депо.', 2);