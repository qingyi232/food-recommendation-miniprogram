USE campus_food;
SET NAMES utf8mb4;

-- 修复店铺图片
UPDATE shop SET logo = 'https://picsum.photos/seed/shop1/400/300' WHERE id = 1;
UPDATE shop SET logo = 'https://picsum.photos/seed/shop2/400/300' WHERE id = 2;
UPDATE shop SET logo = 'https://picsum.photos/seed/shop3/400/300' WHERE id = 3;
UPDATE shop SET logo = 'https://picsum.photos/seed/shop4/400/300' WHERE id = 4;

-- 修复菜品图片
UPDATE dish SET image = 'https://picsum.photos/seed/dish1/400/300' WHERE id = 1;
UPDATE dish SET image = 'https://picsum.photos/seed/dish2/400/300' WHERE id = 2;
UPDATE dish SET image = 'https://picsum.photos/seed/dish3/400/300' WHERE id = 3;
UPDATE dish SET image = 'https://picsum.photos/seed/dish4/400/300' WHERE id = 4;
UPDATE dish SET image = 'https://picsum.photos/seed/dish5/400/300' WHERE id = 5;
UPDATE dish SET image = 'https://picsum.photos/seed/dish6/400/300' WHERE id = 6;
UPDATE dish SET image = 'https://picsum.photos/seed/dish7/400/300' WHERE id = 7;
UPDATE dish SET image = 'https://picsum.photos/seed/dish8/400/300' WHERE id = 8;
UPDATE dish SET image = 'https://picsum.photos/seed/dish9/400/300' WHERE id = 9;
