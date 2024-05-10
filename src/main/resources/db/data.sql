CREATE TABLE "user" (
                        id SERIAL PRIMARY KEY,
                        first_name VARCHAR(255),
                        last_name VARCHAR(255),
                        login VARCHAR(255) UNIQUE NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        "group" VARCHAR(255)
);
CREATE TABLE users_telegram (
                                id SERIAL PRIMARY KEY,
                                user_id INTEGER NOT NULL,
                                chat_id BIGINT UNIQUE NOT NULL ,
                                FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE
);
INSERT INTO "user" (first_name, last_name, login, password, "group")
VALUES
    ('Иван', 'Иванов', 'ivan123', 'password123', 'Группа А'),
    ('Елена', 'Петрова', 'elena456', 'securepass', 'Группа В'),
    ('Михаил', 'Смирнов', 'mikhail789', 'strongpass', 'Группа А'),
    ('Анастасия', 'Козлова', 'anastasia987', 'password123', 'Группа В'),
    ('Сергей', 'Морозов', 'sergey654', 'mypassword', 'Группа А'),
    ('Ольга', 'Волкова', 'olga321', 'password321', 'Группа В'),
    ('Дмитрий', 'Новиков', 'dmitry123', 'secretpass', 'Группа А'),
    ('Анна', 'Федорова', 'anna456', 'qwerty123', 'Группа В'),
    ('Александр', 'Морозов', 'alex789', 'password', 'Группа А'),
    ('Екатерина', 'Соколова', 'ekaterina987', 'mypassword', 'Группа В'),
    ('Admin', 'Admin', 'admin', 'root', '');
