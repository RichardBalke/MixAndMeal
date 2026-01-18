drop table ingredient_units;
drop table recipe_diets;
drop table recipe_images;
drop table recipe_allergens;
drop table user_diets;
drop table user_fridge;
drop table user_allergens;
drop table user_favourites;
drop table ingredients;
drop table allergens;
drop table diets;
drop table recipes;
drop table users;

create table users
(
    name     varchar,
    email    varchar not null
        constraint users_pk
            primary key,
    password varchar not null,
    role     varchar not null
);

create table recipes
(
    recipe_id    integer generated always AS identity primary key,
    title        varchar,
    description  varchar,
    instructions text,
    preptime     integer,
    cookingtime  integer,
    difficulty   varchar,
    mealtype     varchar,
    kitchenstyle varchar,
    favoritescount integer
);

create table ingredients
(
    name        varchar not null
        constraint ingredients_pk
            primary key,
    description text
);

create table diets
(
    diet_id     integer not null
        primary key,
    displayname varchar not null,
    description text
);

create table allergens
(
    allergen_id integer not null
        primary key,
    name        varchar not null,
    displayname varchar,
    description text
);

create table user_fridge
(
    user_id       varchar not null references users(email),
    ingredient_name varchar not null references ingredients(name),
    primary key (user_id, ingredient_name)
);

create table user_favourites
(
    user_id   varchar not null references users(email),
    recipe_id integer not null references recipes(recipe_id),
    primary key (user_id, recipe_id)
);

create table user_allergens
(
    user_id     varchar not null references users(email),
    allergen_id integer not null references allergens(allergen_id),
    primary key (user_id, allergen_id)
);


create table user_diets
(
    user_id     varchar not null references users(email),
    diet_id     integer not null references diets(diet_id),
    primary key (user_id, diet_id)
);

create table recipe_diets
(
    recipe_id integer not null references recipes(recipe_id),
    diet_id   integer not null references diets(diet_id),
    primary key (recipe_id, diet_id)
);

create table ingredient_units
(
    recipe_id       integer not null references recipes(recipe_id),
    ingredient_name varchar not null references ingredients(name),
    amount          numeric,
    unittype        varchar,
    primary key (recipe_id, ingredient_name)
);

create table recipe_allergens
(
    recipe_id integer not null references recipes(recipe_id),
    allergen_id integer not null references allergens(allergen_id),
    primary key (recipe_id, allergen_id)
);

create table recipe_images
(
    recipe_image_id integer generated always as identity primary key,
    recipe_id integer not null references recipes(recipe_id),
    image_url varchar not null
)







