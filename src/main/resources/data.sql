INSERT INTO administrator (password, username) VALUES ('$2a$12$KKmVDy8EPN26afVwYj/Ho.4tcziJwwiu8F6IjhT3W8Il3mzKDFisO', 'admin');

ALTER SEQUENCE client_id_seq RESTART WITH 1;

INSERT INTO client (address, date_of_birth, name, password, username) VALUES
                    ('Taberei 32', '2000-05-21', 'Ionut Ionescu', '$2a$12$rmpO8NeJlaE8Wxg5bIWJxeddGzca4qjp/77kvs81T7UArIHyQyBJ6', 'ionut'),
                    ('Brutarilor 44', '2002-08-22', 'Andrei Popescu', '$2a$12$rmpO8NeJlaE8Wxg5bIWJxeddGzca4qjp/77kvs81T7UArIHyQyBJ6', 'andrei');

ALTER SEQUENCE device_id_seq RESTART WITH 1;
INSERT INTO device (average_energy_consumption, description, location, maximum_energy_consumption, sensor_description, sensor_maximum_value, sensor_name, client_id) VALUES
                    (22, 'device1', 'kitchen', 35, 'sensor1', 50, 'sensor1', 1),
                    (12, 'device2', 'bathroom', 20, 'sensor2', 40, 'sensor2', 1),
                    (15, 'device3', 'kitchen', 35, 'sensor3', 60, 'sensor3', 1),
                    (32, 'device4', 'bedroom', 55, null, null, null, 1),
                    (21, 'device5', 'living room', 45, null, null, null, 1),
                    (20, 'device6', 'bedroom', 34, 'sensor4', 45, 'sensor4', 2),
                    (16, 'device7', 'kitchen', 35, 'sensor5', 50, 'sensor5', 2),
                    (20, 'device8', 'bathroom', 35, 'sensor6', 50, 'sensor6', 2),
                    (26, 'device9', 'bathroom', 35, null, null, null, 2),
                    (30, 'device10', 'living room', 35, null, null, null, 2),
                    (9, 'device11', 'bedroom', 35, null, null, null, null),
                    (40, 'device12', 'garage', 75, null, null, null, null),
                    (27, 'device13', 'bathroom', 35, null, null, null, null),
                    (28, 'device14', 'kitchen', 35, null, null, null, null);

DELETE FROM measurement WHERE true=true;
DELETE FROM device WHERE true=true;
