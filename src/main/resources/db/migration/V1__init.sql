CREATE TABLE recipes
(
    id                      BIGINT NOT NULL CONSTRAINT recipe_primary PRIMARY KEY,
    name                    VARCHAR(50) NOT NULL,
    servings                SMALLINT NOT NULL,
    vegetarian              BOOLEAN NOT NULL DEFAULT FALSE,
    creation_date           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cooking_instructions    TEXT NOT NULL
);

CREATE TABLE ingredients
(
    id                      BIGINT NOT NULL CONSTRAINT ingredient_primary PRIMARY KEY,
    name                    VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE ingredient_recipe
(
    recipe_id               BIGINT NOT NULL,
    ingredient_id           BIGINT NOT NULL,
    unit                    VARCHAR(10) NOT NULL,
    amount                  VARCHAR(50) NOT NULL,
    PRIMARY KEY (recipe_id, ingredient_id),
    CONSTRAINT fk_recipe FOREIGN KEY(recipe_id) REFERENCES recipes(id),
    CONSTRAINT fk_ingredient FOREIGN KEY(ingredient_id) REFERENCES ingredients(id)
);

CREATE SEQUENCE hibernate_sequence
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

CREATE SEQUENCE recipe_id_sequence
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

CREATE SEQUENCE ingredient_id_sequence
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;