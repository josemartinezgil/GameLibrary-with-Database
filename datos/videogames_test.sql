-- Create the database
CREATE DATABASE IF NOT EXISTS gamelibrary_test;
USE gamelibrary_test;


-- Create videogames table
CREATE TABLE IF NOT EXISTS videogames (
                                          id_videogame INT AUTO_INCREMENT PRIMARY KEY,
                                          title VARCHAR(150) NOT NULL,
    genre VARCHAR(100) NOT NULL,
    year INT NOT NULL,
    rating DOUBLE NOT NULL
    ) ENGINE=InnoDB;

-- Create players table
CREATE TABLE IF NOT EXISTS players (
                                       id_player INT AUTO_INCREMENT PRIMARY KEY,
                                       name VARCHAR(100) NOT NULL
    ) ENGINE=InnoDB;

-- Create collections table
CREATE TABLE IF NOT EXISTS collections (
                                           id_collection INT AUTO_INCREMENT PRIMARY KEY,
                                           name VARCHAR(100) NOT NULL,
    id_player INT NOT NULL,

    FOREIGN KEY (id_player)
    REFERENCES players(id_player)
    ON DELETE CASCADE
    ) ENGINE=InnoDB;

-- Create videogame_collection table
CREATE TABLE IF NOT EXISTS videogame_collection (
                                                    id_videogame INT NOT NULL,
                                                    id_collection INT NOT NULL,
                                                    date_added DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                                    PRIMARY KEY (id_videogame, id_collection),

    FOREIGN KEY (id_videogame)
    REFERENCES videogames(id_videogame)
    ON DELETE CASCADE,

    FOREIGN KEY (id_collection)
    REFERENCES collections(id_collection)
    ON DELETE CASCADE
    ) ENGINE=InnoDB;


-- Insert videogames
INSERT INTO videogames
(id_videogame, title, genre, year, rating)
VALUES
    (1, 'The Legend of Zelda: Breath of the Wild', 'Aventura', 2017, 9.5),
    (2, 'Red Dead Redemption 2', 'Acción', 2018, 9.7),
    (3, 'Hades', 'Roguelike', 2020, 9.3),
    (4, 'Hollow Knight', 'Plataformas', 2017, 9.1),
    (5, 'Celeste', 'Plataformas', 2018, 8.9);


-- Insert players
INSERT INTO players (id_player, name)
VALUES
    (1, 'Kyo'),
    (2, 'Jose'),
    (3, 'Blade');

-- Insert collections
INSERT INTO collections (id_collection, name, id_player)
VALUES
    (1, 'Favorites', 1),
    (2, 'Currently Playing', 2),
    (3, 'Completed Games', 3);

-- Insert videogames into collections
INSERT INTO videogame_collection
(id_videogame, id_collection)
VALUES
    (1, 1),
    (2, 1),
    (1, 2),
    (3, 2),
    (4, 3),
    (5, 3);
