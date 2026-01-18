insert into users (name, email, password, role) values
    ('Alice Johnson',   'alice@example.com',   'pass123',    'USER'),
    ('Bob Smith',       'bob@example.com',     'secret456',  'USER'),
    ('Charlie Green',   'charlie@example.com', 'charlie789', 'USER'),
    ('Diana Carter',    'diana@example.com',   'diana111',   'USER'),
    ('Ethan Brooks',    'ethan@example.com',   'ethan222',   'USER'),
    ('Fiona White',     'fiona@example.com',   'fiona333',   'USER'),
    ('George Hill',     'george@example.com',  'george444',  'USER'),
    ('Hannah Reed',     'hannah@example.com',  'hannah555',  'USER'),
    ('Ian Black',       'ian@example.com',     'ian666',     'USER'),
    ('Julia Stone',     'julia@example.com',   'julia777',   'USER'),
    ('Bart',     'bart@example.com',   'admin123',   'ADMIN'),
    ('Fauve',     'fauve@example.com',   'admin123',   'ADMIN'),
    ('Richard',     'richard@example.com',   'admin123',   'ADMIN'),
    ('Yoran',     'yoran@example.com',   'admin123',   'ADMIN');

insert into recipes (title, description, instructions, preptime, cookingtime, difficulty, mealtype, kitchenstyle, favoritescount) values
    ('Spaghetti Bolognese', 'Classic Italian pasta with meat sauce.', 'instructions needed', 15, 45, 'MEDIUM', 'DINNER', 'ITALIAN', 0),
    ('Vegetable Stir Fry',  'Quick and healthy mixed vegetable stir fry.', 'instructions needed', 10, 15, 'EASY', 'LUNCH', 'ASIAN', 0),
    ('Chicken Curry',       'Aromatic curry with chicken and basmati rice.', 'instructions needed', 20, 40, 'MEDIUM', 'DINNER', 'INDIAN', 0),
    ('Avocado Toast',       'Simple toast topped with fresh avocado.', 'instructions needed', 5, 5, 'EASY', 'BREAKFAST', 'AMERICAN', 0),
    ('Beef Tacos',          'Seasoned beef with fresh taco toppings.', 'instructions needed', 20, 10, 'EASY', 'DINNER', 'MEXICAN', 0),
    ('Greek Salad',         'Fresh salad with feta cheese and olives.', 'instructions needed', 10, 0, 'EASY', 'LUNCH', 'GREEK', 0),
    ('Pancakes',            'Fluffy breakfast pancakes with syrup.', 'instructions needed', 10, 15, 'EASY', 'BREAKFAST', 'AMERICAN', 0),
    ('Sushi Rolls',         'Rice rolls with fish and vegetables.', 'instructions needed', 25, 0, 'HARD', 'DINNER', 'JAPANESE', 0),
    ('Lentil Soup',         'Hearty vegetarian lentil soup.', 'instructions needed', 15, 35, 'EASY', 'DINNER', 'MEDITERRANEAN', 0),
    ('Salmon Teriyaki',     'Grilled salmon with teriyaki glaze.', 'instructions needed', 15, 20, 'MEDIUM', 'DINNER', 'JAPANESE', 0);

insert into ingredients (name, description) values
    ('Tomato', 'Fresh red tomatoes.'),
    ('Ground Beef', 'Lean minced beef.'),
    ('Spaghetti', 'Dry pasta noodles.'),
    ('Soy Sauce', 'Fermented soy seasoning.'),
    ('Broccoli', 'Fresh broccoli.'),
    ('Chicken Breast', 'Boneless chicken breast.'),
    ('Curry Powder', 'Aromatic curry mix.'),
    ('Rice', 'Basmati white rice.'),
    ('Avocado', 'Ripe avocado.'),
    ('Bread', 'Whole grain bread.'),
    ('Beef', 'Seasoned beef.'),
    ('Tortilla', 'Corn or wheat tortilla.'),
    ('Feta Cheese', 'Greek white cheese.'),
    ('Cucumber', 'Fresh cucumber.'),
    ('Pancake Mix', 'Flour-based mix.'),
    ('Eggs', 'Chicken eggs.'),
    ('Salmon', 'Fresh salmon fillet.'),
    ('Teriyaki Sauce', 'Sweet soy glaze.'),
    ('Lentils', 'Dried lentils.'),
    ('Carrot', 'Fresh carrot.');

insert into allergens (allergen_id, name, displayname, description) values
    (1,  'gluten',       'Gluten',        'Found in wheat, barley, rye.'),
    (2,  'crustaceans',  'Crustaceans',   'Shrimp, crab, lobster.'),
    (3,  'eggs',         'Eggs',          'Egg proteins.'),
    (4,  'fish',         'Fish',          'Fish proteins.'),
    (5,  'peanuts',      'Peanuts',       'Peanut allergy.'),
    (6,  'soy',          'Soy',           'Soy products.'),
    (7,  'milk',         'Milk',          'Dairy & lactose.'),
    (8,  'tree_nuts',    'Tree Nuts',     'Almonds, walnuts, hazelnuts.'),
    (9,  'celery',       'Celery',        'Celery and root.'),
    (10, 'mustard',      'Mustard',       'Mustard seeds.'),
    (11, 'sesame',       'Sesame',        'Sesame seeds and oils.'),
    (12, 'sulphites',    'Sulphites',     'Sulphur dioxide preservatives.'),
    (13, 'lupin',        'Lupin',         'Lupin flour and beans.'),
    (14, 'molluscs',     'Molluscs',      'Clams, mussels, oysters.'),
    (15, 'corn',         'Corn',          'Corn and derivatives.');

insert into diets (diet_id, displayname, description) values
(1,  'Vegan',          'No animal products.'),
(2,  'Vegetarian',     'No meat or fish.'),
(3,  'Gluten free',    'No gluten-containing grains.'),
(4,  'Lactose free',   'No lactose.'),
(5,  'Nut free',       'No nuts or traces of nuts.'),
(6,  'Dairy free',     'No dairy products.'),
(7,  'Low sugar',      'Low in added sugars.'),
(8,  'Low sodium',     'Low in sodium.'),
(9,  'Halal',          'Meets Islamic dietary requirements.'),
(10, 'Kosher',         'Meets Jewish dietary requirements.'),
(11, 'Paleo',          'No grains, dairy, or processed foods.'),
(12, 'Keto',           'Low in carbohydrates, high in fats.'),
(13, 'Raw food',       'Raw or minimally heated foods.'),
(14, 'Flexitarian',    'Mostly vegetarian.');


insert into user_fridge (user_id, ingredient_name) values
    ('alice@example.com', 'Tomato'),
    ('alice@example.com', 'Spaghetti'),
    ('bob@example.com', 'Chicken Breast'),
    ('charlie@example.com', 'Broccoli'),
    ('diana@example.com', 'Avocado'),
    ('ethan@example.com', 'Rice'),
    ('fiona@example.com', 'Feta Cheese'),
    ('george@example.com', 'Beef'),
    ('hannah@example.com', 'Eggs'),
    ('julia@example.com', 'Salmon');

insert into user_favourites (user_id, recipe_id) values
    ('alice@example.com', 1),
    ('bob@example.com', 10),
    ('charlie@example.com', 2),
    ('diana@example.com', 4),
    ('ethan@example.com', 7),
    ('fiona@example.com', 6),
    ('george@example.com', 5),
    ('hannah@example.com', 9),
    ('ian@example.com', 3),
    ('julia@example.com', 8);

insert into user_allergens (user_id, allergen_id) values
    ('alice@example.com', 6),
    ('charlie@example.com', 1),
    ('diana@example.com', 8),
    ('george@example.com', 1),
    ('julia@example.com', 4);

insert into user_diets (user_id, diet_id) values
    ('alice@example.com', 2),
    ('charlie@example.com', 3),
    ('diana@example.com', 1),
    ('ethan@example.com', 12),
    ('fiona@example.com', 2),
    ('hannah@example.com', 3);

insert into recipe_diets (recipe_id, diet_id) values
    -- 1 Spaghetti Bolognese
    (1, 7), (1, 14), (1, 9), (1, 10),

    -- 2 Vegetable Stir Fry
    (2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6), (2, 7),

    -- 3 Chicken Curry
    (3, 4), (3, 5), (3, 6), (3, 7), (3, 9), (3, 14),

    -- 4 Avocado Toast
    (4, 2), (4, 7), (4, 14), (4, 10),

    -- 5 Beef Tacos
    (5, 7), (5, 14), (5, 9), (5, 10),

    -- 6 Greek Salad
    (6, 2), (6, 7), (6, 14),

    -- 7 Pancakes
    (7, 7), (7, 14),

    -- 8 Sushi Rolls
    (8, 3), (8, 4), (8, 5), (8, 6), (8, 7), (8, 9), (8, 10),

    -- 9 Lentil Soup
    (9, 1), (9, 2), (9, 3), (9, 4), (9, 5), (9, 6), (9, 7),

    -- 10 Salmon Teriyaki
    (10, 4), (10, 5), (10, 6), (10, 7), (10, 9), (10, 10), (10, 14);


insert into ingredient_units (recipe_id, ingredient_name, amount, unittype) values
    -- 1 Spaghetti Bolognese
    (1, 'Tomato', 3, 'pieces'),
    (1, 'Ground Beef', 250, 'grams'),
    (1, 'Spaghetti', 200, 'grams'),

    -- 2 Stir Fry
    (2, 'Broccoli', 150, 'grams'),
    (2, 'Soy Sauce', 2, 'tbsp'),

    -- 3 Chicken Curry
    (3, 'Chicken Breast', 300, 'grams'),
    (3, 'Curry Powder', 2, 'tbsp'),
    (3, 'Rice', 200, 'grams'),

    -- 4 Avocado Toast
    (4, 'Avocado', 1, 'piece'),
    (4, 'Bread', 2, 'slices'),

    -- 5 Beef Tacos
    (5, 'Beef', 200, 'grams'),
    (5, 'Tortilla', 3, 'pieces'),

    -- 6 Greek Salad
    (6, 'Feta Cheese', 100, 'grams'),
    (6, 'Cucumber', 1, 'piece'),
    (6, 'Tomato', 2, 'pieces'),

    -- 7 Pancakes
    (7, 'Pancake Mix', 150, 'grams'),
    (7, 'Eggs', 2, 'pieces'),

    -- 8 Sushi Rolls
    (8, 'Rice', 200, 'grams'),
    (8, 'Salmon', 100, 'grams'),
    (8, 'Cucumber', 1, 'piece'),

    -- 9 Lentil Soup
    (9, 'Lentils', 200, 'grams'),
    (9, 'Carrot', 2, 'pieces'),
    (9, 'Tomato', 2, 'pieces'),

    -- 10 Salmon Teriyaki
    (10, 'Salmon', 200, 'grams'),
    (10, 'Teriyaki Sauce', 3, 'tbsp'),
    (10, 'Rice', 150, 'grams');

insert into recipe_allergens (recipe_id, allergen_id) values
    (1, 1),
    (4, 1),
    (3, 6),
    (2, 7),
    (1, 3),
    (2, 4),
    (7, 1),
    (5, 1);

insert into recipe_images (recipe_id, image_url) values
    (1, 'https://dumpvanplaatjes.nl/mix-and-meal/default-image.jpg'),
    (2, 'https://dumpvanplaatjes.nl/mix-and-meal/default-image.jpg'),
    (3, 'https://dumpvanplaatjes.nl/mix-and-meal/default-image.jpg'),
    (4, 'https://dumpvanplaatjes.nl/mix-and-meal/default-image.jpg'),
    (5, 'https://dumpvanplaatjes.nl/mix-and-meal/default-image.jpg'),
    (6, 'https://dumpvanplaatjes.nl/mix-and-meal/default-image.jpg'),
    (7, 'https://dumpvanplaatjes.nl/mix-and-meal/default-image.jpg'),
    (8, 'https://dumpvanplaatjes.nl/mix-and-meal/default-image.jpg'),
    (9, 'https://dumpvanplaatjes.nl/mix-and-meal/default-image.jpg'),
    (10, 'https://dumpvanplaatjes.nl/mix-and-meal/default-image.jpg');
