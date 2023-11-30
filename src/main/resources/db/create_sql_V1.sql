CREATE TABLE cars
(
    id UUID DEFAULT gen_random_uuid()  PRIMARY KEY,
    name VARCHAR(10) NOT NULL,
    description VARCHAR(30),
    price NUMERIC NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO cars (name, description, price, created)
VALUES ('Audi', 'Хорошая машина!', 50000.0, '2023-11-14 14:00:00+03'),
('BMW', 'Быстрая машина!', 80000.0, '2023-11-13 12:00:00+03');

