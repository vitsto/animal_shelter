-- liquibase formatted sql

-- changeset marina:1

INSERT INTO pet (pet_type_id, pet_name, breed, client_id, volunteer_id)
VALUES (1, 'Шарик', 'лабрадор', null, 1), (1, 'Джек', 'немецкая овчарка', null, 2),
       (1, 'Кай', 'хаски', null, 3), (1, 'Герда', 'гончая', null, 1),
       (2, 'Барсик', 'американская короткошёрстная', null, 4), (2, 'Пушистик', 'персидская', null, 5),
       (2, 'Антино', 'сиамская', null, 5), (2, 'Мурзик', 'мейн-кун', null, 6);