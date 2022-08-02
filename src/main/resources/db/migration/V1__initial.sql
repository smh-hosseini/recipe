CREATE SEQUENCE hibernate_sequence START 1;

CREATE TABLE recipe (
  id bigserial NOT NULL,
  name VARCHAR(255),
  vegan BOOLEAN,
  servings INTEGER,
  instructions TEXT,
  CONSTRAINT pk_recipe PRIMARY KEY (id)
);

ALTER TABLE Recipe ADD CONSTRAINT recipe_name_unique UNIQUE (name);

CREATE TABLE recipe_ingredients (
  recipe_id BIGINT NOT NULL,
  ingredient VARCHAR(255) NOT NULL,
  CONSTRAINT pk_recipe_ingredients PRIMARY KEY (recipe_id, ingredient)
);

ALTER TABLE recipe_ingredients ADD CONSTRAINT fk_recing_on_recipe FOREIGN KEY (recipe_id) REFERENCES recipe (id);