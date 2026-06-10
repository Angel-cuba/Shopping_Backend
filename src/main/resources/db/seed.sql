-- ============================================================
-- SEED DATA — Shopping Store (Nike-style calzado)
-- ============================================================
-- Contraseñas en texto plano:
--   carlos.garcia  →  Admin2026!   (ADMIN)
--   maria.lopez    →  User2026!    (USER)
--   alex.martin    →  User2026!    (USER)
--   isabel.fern    →  User2026!    (USER)
--   diego.torres   →  User2026!    (USER)
--
-- UUID scheme:
--   categories  → cab00001-0000-0000-0000-0000000000XX
--   users       → cab00002-0000-0000-0000-0000000000XX
--   products    → cab00003-0000-0000-0000-0000000000XX
--   addresses   → cab00004-0000-0000-0000-0000000000XX
--   payments    → cab00005-0000-0000-0000-0000000000XX
--   ord_details → cab00006-0000-0000-0000-0000000000XX
--   orders      → cab00007-0000-0000-0000-0000000000XX
--   wishes      → cab00008-0000-0000-0000-0000000000XX
-- ============================================================

BEGIN;

-- ============================================================
-- 1. CATEGORIES (5)
-- ============================================================
INSERT INTO category (id, name, created_at, updated_at) VALUES
  ('cab00001-0000-0000-0000-000000000001', 'Running',    NOW() - INTERVAL '6 months', NOW()),
  ('cab00001-0000-0000-0000-000000000002', 'Basketball', NOW() - INTERVAL '6 months', NOW()),
  ('cab00001-0000-0000-0000-000000000003', 'Lifestyle',  NOW() - INTERVAL '6 months', NOW()),
  ('cab00001-0000-0000-0000-000000000004', 'Training',   NOW() - INTERVAL '6 months', NOW()),
  ('cab00001-0000-0000-0000-000000000005', 'Football',   NOW() - INTERVAL '6 months', NOW());


-- ============================================================
-- 2. PRODUCTS (20) — precios en EUR enteros
-- ============================================================
INSERT INTO products (id, name, description, price, in_stock, sizes, variants, categories, image, created_at, updated_at) VALUES

-- RUNNING (6)
('cab00003-0000-0000-0000-000000000001',
 'Air Zoom Pegasus 40',
 'Zapatilla de running versátil con amortiguación React. Perfecta para rodajes diarios.',
 130, 50,
 ARRAY['38','39','40','41','42','43','44','45'],
 ARRAY['White/Black','Blue/Silver','Black/Red'],
 'Running', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&q=80&auto=format&fit=crop',
 NOW() - INTERVAL '5 months', NOW()),

('cab00003-0000-0000-0000-000000000002',
 'React Infinity Run FK 3',
 'Diseñada para ayudarte a seguir corriendo. Más amortiguación, más estabilidad.',
 160, 35,
 ARRAY['39','40','41','42','43','44','45'],
 ARRAY['Black/White','Electric Blue','Mint Green'],
 'Running', 'https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=600&q=80&auto=format&fit=crop',
 NOW() - INTERVAL '4 months', NOW()),

('cab00003-0000-0000-0000-000000000003',
 'Vomero 17',
 'El máximo confort para carreras largas. Plataforma de espuma ZoomX.',
 180, 20,
 ARRAY['40','41','42','43','44','45','46'],
 ARRAY['Pale Ivory','Wolf Grey','Black'],
 'Running', 'https://images.unsplash.com/photo-1491553895911-0055eca6402d?w=600&q=80&auto=format&fit=crop',
 NOW() - INTERVAL '3 months', NOW()),

('cab00003-0000-0000-0000-000000000004',
 'Air Max 270',
 'Unidad Air de 270 grados para máximo confort todo el día.',
 150, 45,
 ARRAY['38','39','40','41','42','43','44','45'],
 ARRAY['White','Triple Black','University Red'],
 'Running', 'https://images.unsplash.com/photo-1515955656352-a1fa3ffcd111?w=600&q=80&auto=format&fit=crop',
 NOW() - INTERVAL '5 months', NOW()),

('cab00003-0000-0000-0000-000000000005',
 'Air Zoom Structure 25',
 'Soporte y amortiguación para pronadores. Tecnología Dynamic Support.',
 135, 30,
 ARRAY['39','40','41','42','43','44'],
 ARRAY['Aquarius Blue/White','Black/Volt'],
 'Running', 'https://images.unsplash.com/photo-1539185441755-769473a23570?w=600&q=80&auto=format&fit=crop',
 NOW() - INTERVAL '2 months', NOW()),

('cab00003-0000-0000-0000-000000000006',
 'ZoomX Vaporfly NEXT% 2',
 'La zapatilla de competición más rápida. Placa de fibra de carbono.',
 250, 15,
 ARRAY['40','41','42','43','44','45'],
 ARRAY['Barely Yellow/Black','White/Pink','Electric Green'],
 'Running', 'https://images.unsplash.com/photo-1552346154-21d32810aba3?w=600&q=80&auto=format&fit=crop',
 NOW() - INTERVAL '1 month', NOW()),

-- BASKETBALL (4)
('cab00003-0000-0000-0000-000000000007',
 'Air Jordan 1 Retro High OG',
 'El icónico Jordan 1 de 1985 reimaginado. Cuero de alta calidad.',
 180, 25,
 ARRAY['40','41','42','43','44','45','46'],
 ARRAY['Chicago Red/Black/White','Royal Blue/White','Bred Toe'],
 'Basketball', 'https://images.unsplash.com/photo-1587563871167-1ee9c731aefb?w=600&q=80&auto=format&fit=crop',
 NOW() - INTERVAL '6 months', NOW()),

('cab00003-0000-0000-0000-000000000008',
 'LeBron 21',
 'Potencia tus movimientos en la cancha. Amortiguación Air Max full-length.',
 200, 18,
 ARRAY['41','42','43','44','45','46','47'],
 ARRAY['Violet Frost','Tahitian/Black','California'],
 'Basketball', 'https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?w=600&q=80&auto=format&fit=crop',
 NOW() - INTERVAL '3 months', NOW()),

('cab00003-0000-0000-0000-000000000009',
 'KD 16',
 'Kevin Durant confía en esta zapatilla para dominar el juego interior.',
 185, 22,
 ARRAY['40','41','42','43','44','45'],
 ARRAY['Black/White','University Gold','Aunt Pearl'],
 'Basketball', 'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=600&q=80&auto=format&fit=crop',
 NOW() - INTERVAL '2 months', NOW()),

('cab00003-0000-0000-0000-00000000000a',
 'Cosmic Unity 3',
 'Rendimiento sostenible. Al menos 20% de contenido reciclado.',
 140, 40,
 ARRAY['39','40','41','42','43','44','45'],
 ARRAY['Photon Dust','Black/Barely Green'],
 'Basketball', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&q=80&auto=format&fit=crop',
 NOW() - INTERVAL '1 month', NOW()),

-- LIFESTYLE (6)
('cab00003-0000-0000-0000-00000000000b',
 'Air Force 1 ''07',
 'El clásico eterno. Piel suave y unidad Air. Nunca pasa de moda.',
 110, 100,
 ARRAY['36','37','38','39','40','41','42','43','44','45'],
 ARRAY['White/White','Black/Black','Wheat/Gum'],
 'Lifestyle', 'https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=600&q=80&auto=format&fit=crop',
 NOW() - INTERVAL '6 months', NOW()),

('cab00003-0000-0000-0000-00000000000c',
 'Air Max 90',
 'Icono atemporal desde 1990. La unidad Air Sole más reconocible.',
 135, 60,
 ARRAY['36','37','38','39','40','41','42','43','44','45'],
 ARRAY['White/Grey/Red','Triple White','Black/Infrared'],
 'Lifestyle', 'https://images.unsplash.com/photo-1515955656352-a1fa3ffcd111?w=600&q=80&auto=format&fit=crop',
 NOW() - INTERVAL '5 months', NOW()),

('cab00003-0000-0000-0000-00000000000d',
 'Dunk Low Retro',
 'Llevado de la cancha a las calles. Estilo inconfundible en cada paso.',
 120, 55,
 ARRAY['36','37','38','39','40','41','42','43','44','45'],
 ARRAY['Panda/Black-White','University Red/White','Green/White'],
 'Lifestyle', 'https://images.unsplash.com/photo-1491553895911-0055eca6402d?w=600&q=80&auto=format&fit=crop',
 NOW() - INTERVAL '4 months', NOW()),

('cab00003-0000-0000-0000-00000000000e',
 'Blazer Mid ''77 Vintage',
 'Cuero suave y suela vulcanizada. El básico que nunca falla.',
 105, 45,
 ARRAY['36','37','38','39','40','41','42','43','44'],
 ARRAY['White/Black','White/University Red','Navy/White'],
 'Lifestyle', 'https://images.unsplash.com/photo-1539185441755-769473a23570?w=600&q=80&auto=format&fit=crop',
 NOW() - INTERVAL '3 months', NOW()),

('cab00003-0000-0000-0000-00000000000f',
 'Air Max Plus',
 'El diseño Tuned Air. Inspirado en el sol poniente de Miami.',
 150, 35,
 ARRAY['38','39','40','41','42','43','44','45'],
 ARRAY['Black/Volt','White/Blue Gradient','Triple Black'],
 'Lifestyle', 'https://images.unsplash.com/photo-1552346154-21d32810aba3?w=600&q=80&auto=format&fit=crop',
 NOW() - INTERVAL '2 months', NOW()),

('cab00003-0000-0000-0000-000000000010',
 'Cortez',
 'La silueta de running más icónica de Nike, rediseñada para el lifestyle.',
 95, 70,
 ARRAY['36','37','38','39','40','41','42','43'],
 ARRAY['White/Red/Blue','Black/White','Tan/Gum'],
 'Lifestyle', 'https://images.unsplash.com/photo-1587563871167-1ee9c731aefb?w=600&q=80&auto=format&fit=crop',
 NOW() - INTERVAL '1 month', NOW()),

-- TRAINING (2)
('cab00003-0000-0000-0000-000000000011',
 'Metcon 9',
 'Para el WOD más duro. Estabilidad y tracción en cualquier superficie.',
 140, 30,
 ARRAY['39','40','41','42','43','44','45'],
 ARRAY['Black/White','Gym Blue/White','Volt/Black'],
 'Training', 'https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?w=600&q=80&auto=format&fit=crop',
 NOW() - INTERVAL '4 months', NOW()),

('cab00003-0000-0000-0000-000000000012',
 'Free Metcon 5',
 'Combina flexibilidad de running con estabilidad de entrenamiento.',
 125, 25,
 ARRAY['39','40','41','42','43','44','45'],
 ARRAY['Blue/Gold','Black/Pink','White/Purple'],
 'Training', 'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=600&q=80&auto=format&fit=crop',
 NOW() - INTERVAL '2 months', NOW()),

-- FOOTBALL (2)
('cab00003-0000-0000-0000-000000000013',
 'Mercurial Vapor 15 Elite FG',
 'La velocidad al límite. Placa de carbono y suela FG para césped natural.',
 230, 20,
 ARRAY['39','40','41','42','43','44','45'],
 ARRAY['Chrome/Black','Summit White/Gold','Pink Blast/Black'],
 'Football', 'https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=600&q=80&auto=format&fit=crop',
 NOW() - INTERVAL '3 months', NOW()),

('cab00003-0000-0000-0000-000000000014',
 'Phantom GX Elite FG',
 'Precisión y tacto superior. Tecnología Gripknit para mejor control del balón.',
 220, 15,
 ARRAY['39','40','41','42','43','44','45'],
 ARRAY['Black/Summit White','Blue Void/Silver','Volt/Black'],
 'Football', 'https://images.unsplash.com/photo-1491553895911-0055eca6402d?w=600&q=80&auto=format&fit=crop',
 NOW() - INTERVAL '1 month', NOW());


-- ============================================================
-- 3. USERS (5)
-- Passwords BCrypt $2a$10$ (compatibles con Spring Security)
-- ============================================================
INSERT INTO users (id, username, email, firstname, lastname, password, phone, role) VALUES

-- ADMIN
('cab00002-0000-0000-0000-000000000001',
 'carlos.garcia', 'carlos.garcia@integrify.com',
 'Carlos', 'García',
 '$2a$10$hDV.LruohZ4sPWHhVUWcPuhLv1dsVYGBUSn0S16UX.12spql5nQK.',  -- Admin2026!
 '+34 600 111 222', 'ADMIN'),

-- USERS
('cab00002-0000-0000-0000-000000000002',
 'maria.lopez', 'maria.lopez@gmail.com',
 'María', 'López',
 '$2a$10$tszc5Gk.yS5En4PZa8rn2eQySC.nh7q5pDe8/0DBNFf/KBmEQ7O8e',  -- User2026!
 '+34 612 334 556', 'USER'),

('cab00002-0000-0000-0000-000000000003',
 'alex.martin', 'alex.martin@hotmail.com',
 'Alejandro', 'Martín',
 '$2a$10$tszc5Gk.yS5En4PZa8rn2eQySC.nh7q5pDe8/0DBNFf/KBmEQ7O8e',  -- User2026!
 '+34 623 445 667', 'USER'),

('cab00002-0000-0000-0000-000000000004',
 'isabel.fern', 'isabel.fernandez@gmail.com',
 'Isabel', 'Fernández',
 '$2a$10$tszc5Gk.yS5En4PZa8rn2eQySC.nh7q5pDe8/0DBNFf/KBmEQ7O8e',  -- User2026!
 '+34 634 556 778', 'USER'),

('cab00002-0000-0000-0000-000000000005',
 'diego.torres', 'diego.torres@outlook.com',
 'Diego', 'Torres',
 '$2a$10$tszc5Gk.yS5En4PZa8rn2eQySC.nh7q5pDe8/0DBNFf/KBmEQ7O8e',  -- User2026!
 '+34 645 667 889', 'USER');


-- ============================================================
-- 4. ADDRESSES
-- ============================================================
INSERT INTO address (id, user_id, address, city, postal_code, country, created_at, updated_at) VALUES

('cab00004-0000-0000-0000-000000000001', 'cab00002-0000-0000-0000-000000000001',
 'Calle Mayor 15, 3ºA', 'Madrid', '28001', 'Spain',
 NOW() - INTERVAL '5 months', NOW()),

('cab00004-0000-0000-0000-000000000002', 'cab00002-0000-0000-0000-000000000002',
 'Avda. Diagonal 200, 5ºB', 'Barcelona', '08013', 'Spain',
 NOW() - INTERVAL '4 months', NOW()),

('cab00004-0000-0000-0000-000000000003', 'cab00002-0000-0000-0000-000000000003',
 'Calle Serrano 45, 2ºD', 'Madrid', '28006', 'Spain',
 NOW() - INTERVAL '5 months', NOW()),

('cab00004-0000-0000-0000-000000000004', 'cab00002-0000-0000-0000-000000000003',
 'Paseo de Gracia 88, 4ºA', 'Barcelona', '08008', 'Spain',
 NOW() - INTERVAL '2 months', NOW()),

('cab00004-0000-0000-0000-000000000005', 'cab00002-0000-0000-0000-000000000004',
 'Calle Sierpes 22, 1ºC', 'Sevilla', '41001', 'Spain',
 NOW() - INTERVAL '3 months', NOW()),

('cab00004-0000-0000-0000-000000000006', 'cab00002-0000-0000-0000-000000000005',
 'Gran Vía 100, 6ºB', 'Bilbao', '48001', 'Spain',
 NOW() - INTERVAL '3 months', NOW());


-- ============================================================
-- 5. PAYMENTS
-- ============================================================
INSERT INTO payments (id, user_id, payment_type, provider, card_holder_name, card_number, expiration_date, created_at, updated_at) VALUES

('cab00005-0000-0000-0000-000000000001', 'cab00002-0000-0000-0000-000000000001',
 'CREDIT_CARD', 'VISA', 'Carlos García', '**** **** **** 4242', '12/26',
 NOW() - INTERVAL '5 months', NOW()),

('cab00005-0000-0000-0000-000000000002', 'cab00002-0000-0000-0000-000000000002',
 'CREDIT_CARD', 'MASTERCARD', 'María López', '**** **** **** 5678', '08/25',
 NOW() - INTERVAL '4 months', NOW()),

('cab00005-0000-0000-0000-000000000003', 'cab00002-0000-0000-0000-000000000003',
 'CREDIT_CARD', 'VISA', 'Alejandro Martín', '**** **** **** 1234', '05/27',
 NOW() - INTERVAL '5 months', NOW()),

('cab00005-0000-0000-0000-000000000004', 'cab00002-0000-0000-0000-000000000003',
 'PAYPAL', 'PAYPAL', 'Alejandro Martín', 'alex.martin@hotmail.com', 'N/A',
 NOW() - INTERVAL '2 months', NOW()),

('cab00005-0000-0000-0000-000000000005', 'cab00002-0000-0000-0000-000000000004',
 'CREDIT_CARD', 'AMEX', 'Isabel Fernández', '**** ****** *3782', '11/26',
 NOW() - INTERVAL '3 months', NOW()),

('cab00005-0000-0000-0000-000000000006', 'cab00002-0000-0000-0000-000000000005',
 'DEBIT_CARD', 'VISA', 'Diego Torres', '**** **** **** 9999', '03/28',
 NOW() - INTERVAL '3 months', NOW());


-- ============================================================
-- 6. ORDER DETAILS (24 líneas de pedido)
-- product_id = UUID del producto como texto
-- ============================================================
INSERT INTO order_details (id, user_id, product_id, image, price, quantity, size, variant, created_at, updated_at) VALUES

-- Carlos: Pedido 1 (DELIVERED, -3 meses) — Jordan 1 + Air Max 90
('cab00006-0000-0000-0000-000000000001', 'cab00002-0000-0000-0000-000000000001',
 'cab00003-0000-0000-0000-000000000007', 'https://images.unsplash.com/photo-1587563871167-1ee9c731aefb?w=600&q=80&auto=format&fit=crop',
 180, 1, '43', 'Chicago Red/Black/White',
 NOW() - INTERVAL '3 months', NOW() - INTERVAL '3 months'),

('cab00006-0000-0000-0000-000000000002', 'cab00002-0000-0000-0000-000000000001',
 'cab00003-0000-0000-0000-00000000000c', 'https://images.unsplash.com/photo-1515955656352-a1fa3ffcd111?w=600&q=80&auto=format&fit=crop',
 135, 1, '43', 'Triple White',
 NOW() - INTERVAL '3 months', NOW() - INTERVAL '3 months'),

-- Carlos: Pedido 2 (DELIVERED, -2 meses) — Air Force 1
('cab00006-0000-0000-0000-000000000003', 'cab00002-0000-0000-0000-000000000001',
 'cab00003-0000-0000-0000-00000000000b', 'https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=600&q=80&auto=format&fit=crop',
 110, 1, '43', 'White/White',
 NOW() - INTERVAL '2 months', NOW() - INTERVAL '2 months'),

-- Carlos: Pedido 3 (SHIPPED, -3 semanas) — ZoomX Vaporfly
('cab00006-0000-0000-0000-000000000004', 'cab00002-0000-0000-0000-000000000001',
 'cab00003-0000-0000-0000-000000000006', 'https://images.unsplash.com/photo-1552346154-21d32810aba3?w=600&q=80&auto=format&fit=crop',
 250, 1, '44', 'Electric Green',
 NOW() - INTERVAL '3 weeks', NOW() - INTERVAL '3 weeks'),

-- Carlos: Pedido 4 (PROCESSING, -1 semana) — Mercurial + Phantom
('cab00006-0000-0000-0000-000000000005', 'cab00002-0000-0000-0000-000000000001',
 'cab00003-0000-0000-0000-000000000013', 'https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=600&q=80&auto=format&fit=crop',
 230, 1, '43', 'Chrome/Black',
 NOW() - INTERVAL '1 week', NOW() - INTERVAL '1 week'),

('cab00006-0000-0000-0000-000000000006', 'cab00002-0000-0000-0000-000000000001',
 'cab00003-0000-0000-0000-000000000014', 'https://images.unsplash.com/photo-1491553895911-0055eca6402d?w=600&q=80&auto=format&fit=crop',
 220, 1, '43', 'Black/Summit White',
 NOW() - INTERVAL '1 week', NOW() - INTERVAL '1 week'),

-- María: Pedido 5 (DELIVERED, -4 meses) — Air Max 270 x2
('cab00006-0000-0000-0000-000000000007', 'cab00002-0000-0000-0000-000000000002',
 'cab00003-0000-0000-0000-000000000004', 'https://images.unsplash.com/photo-1515955656352-a1fa3ffcd111?w=600&q=80&auto=format&fit=crop',
 150, 2, '38', 'White',
 NOW() - INTERVAL '4 months', NOW() - INTERVAL '4 months'),

-- María: Pedido 6 (DELIVERED, -2 meses) — Dunk Low + Cortez
('cab00006-0000-0000-0000-000000000008', 'cab00002-0000-0000-0000-000000000002',
 'cab00003-0000-0000-0000-00000000000d', 'https://images.unsplash.com/photo-1491553895911-0055eca6402d?w=600&q=80&auto=format&fit=crop',
 120, 1, '39', 'Panda/Black-White',
 NOW() - INTERVAL '2 months', NOW() - INTERVAL '2 months'),

('cab00006-0000-0000-0000-000000000009', 'cab00002-0000-0000-0000-000000000002',
 'cab00003-0000-0000-0000-000000000010', 'https://images.unsplash.com/photo-1587563871167-1ee9c731aefb?w=600&q=80&auto=format&fit=crop',
 95, 1, '38', 'White/Red/Blue',
 NOW() - INTERVAL '2 months', NOW() - INTERVAL '2 months'),

-- María: Pedido 7 (CANCELLED, -1 mes) — Blazer Mid
('cab00006-0000-0000-0000-000000000010', 'cab00002-0000-0000-0000-000000000002',
 'cab00003-0000-0000-0000-00000000000e', 'https://images.unsplash.com/photo-1539185441755-769473a23570?w=600&q=80&auto=format&fit=crop',
 105, 1, '38', 'Navy/White',
 NOW() - INTERVAL '1 month', NOW() - INTERVAL '1 month'),

-- Alex: Pedido 8 (DELIVERED, -5 meses) — React Infinity
('cab00006-0000-0000-0000-000000000011', 'cab00002-0000-0000-0000-000000000003',
 'cab00003-0000-0000-0000-000000000002', 'https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=600&q=80&auto=format&fit=crop',
 160, 1, '42', 'Black/White',
 NOW() - INTERVAL '5 months', NOW() - INTERVAL '5 months'),

-- Alex: Pedido 9 (DELIVERED, -4 meses) — Pegasus + Metcon 9
('cab00006-0000-0000-0000-000000000012', 'cab00002-0000-0000-0000-000000000003',
 'cab00003-0000-0000-0000-000000000001', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&q=80&auto=format&fit=crop',
 130, 1, '42', 'Blue/Silver',
 NOW() - INTERVAL '4 months', NOW() - INTERVAL '4 months'),

('cab00006-0000-0000-0000-000000000013', 'cab00002-0000-0000-0000-000000000003',
 'cab00003-0000-0000-0000-000000000011', 'https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?w=600&q=80&auto=format&fit=crop',
 140, 1, '42', 'Black/White',
 NOW() - INTERVAL '4 months', NOW() - INTERVAL '4 months'),

-- Alex: Pedido 10 (DELIVERED, -3 meses) — Structure 25
('cab00006-0000-0000-0000-000000000014', 'cab00002-0000-0000-0000-000000000003',
 'cab00003-0000-0000-0000-000000000005', 'https://images.unsplash.com/photo-1539185441755-769473a23570?w=600&q=80&auto=format&fit=crop',
 135, 1, '42', 'Aquarius Blue/White',
 NOW() - INTERVAL '3 months', NOW() - INTERVAL '3 months'),

-- Alex: Pedido 11 (SHIPPED, -2 semanas) — Vomero 17
('cab00006-0000-0000-0000-000000000015', 'cab00002-0000-0000-0000-000000000003',
 'cab00003-0000-0000-0000-000000000003', 'https://images.unsplash.com/photo-1491553895911-0055eca6402d?w=600&q=80&auto=format&fit=crop',
 180, 1, '42', 'Pale Ivory',
 NOW() - INTERVAL '2 weeks', NOW() - INTERVAL '2 weeks'),

-- Alex: Pedido 12 (PROCESSING, -3 días) — Free Metcon 5 + ZoomX Vaporfly
('cab00006-0000-0000-0000-000000000016', 'cab00002-0000-0000-0000-000000000003',
 'cab00003-0000-0000-0000-000000000012', 'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=600&q=80&auto=format&fit=crop',
 125, 1, '42', 'Blue/Gold',
 NOW() - INTERVAL '3 days', NOW() - INTERVAL '3 days'),

('cab00006-0000-0000-0000-000000000017', 'cab00002-0000-0000-0000-000000000003',
 'cab00003-0000-0000-0000-000000000006', 'https://images.unsplash.com/photo-1552346154-21d32810aba3?w=600&q=80&auto=format&fit=crop',
 250, 1, '42', 'White/Pink',
 NOW() - INTERVAL '3 days', NOW() - INTERVAL '3 days'),

-- Isabel: Pedido 13 (DELIVERED, -2 meses) — Air Force 1 + Blazer Mid
('cab00006-0000-0000-0000-000000000018', 'cab00002-0000-0000-0000-000000000004',
 'cab00003-0000-0000-0000-00000000000b', 'https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=600&q=80&auto=format&fit=crop',
 110, 1, '37', 'Black/Black',
 NOW() - INTERVAL '2 months', NOW() - INTERVAL '2 months'),

('cab00006-0000-0000-0000-000000000019', 'cab00002-0000-0000-0000-000000000004',
 'cab00003-0000-0000-0000-00000000000e', 'https://images.unsplash.com/photo-1539185441755-769473a23570?w=600&q=80&auto=format&fit=crop',
 105, 1, '37', 'White/Black',
 NOW() - INTERVAL '2 months', NOW() - INTERVAL '2 months'),

-- Isabel: Pedido 14 (PROCESSING, -5 días) — Air Max 90
('cab00006-0000-0000-0000-000000000020', 'cab00002-0000-0000-0000-000000000004',
 'cab00003-0000-0000-0000-00000000000c', 'https://images.unsplash.com/photo-1515955656352-a1fa3ffcd111?w=600&q=80&auto=format&fit=crop',
 135, 1, '37', 'White/Grey/Red',
 NOW() - INTERVAL '5 days', NOW() - INTERVAL '5 days'),

-- Diego: Pedido 15 (DELIVERED, -3 meses) — Mercurial Vapor
('cab00006-0000-0000-0000-000000000021', 'cab00002-0000-0000-0000-000000000005',
 'cab00003-0000-0000-0000-000000000013', 'https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=600&q=80&auto=format&fit=crop',
 230, 1, '44', 'Summit White/Gold',
 NOW() - INTERVAL '3 months', NOW() - INTERVAL '3 months'),

-- Diego: Pedido 16 (DELIVERED, -6 semanas) — Jordan 1 + LeBron 21
('cab00006-0000-0000-0000-000000000022', 'cab00002-0000-0000-0000-000000000005',
 'cab00003-0000-0000-0000-000000000007', 'https://images.unsplash.com/photo-1587563871167-1ee9c731aefb?w=600&q=80&auto=format&fit=crop',
 180, 1, '44', 'Bred Toe',
 NOW() - INTERVAL '6 weeks', NOW() - INTERVAL '6 weeks'),

('cab00006-0000-0000-0000-000000000023', 'cab00002-0000-0000-0000-000000000005',
 'cab00003-0000-0000-0000-000000000008', 'https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?w=600&q=80&auto=format&fit=crop',
 200, 1, '44', 'California',
 NOW() - INTERVAL '6 weeks', NOW() - INTERVAL '6 weeks'),

-- Diego: Pedido 17 (SHIPPED, -1 semana) — Phantom GX Elite
('cab00006-0000-0000-0000-000000000024', 'cab00002-0000-0000-0000-000000000005',
 'cab00003-0000-0000-0000-000000000014', 'https://images.unsplash.com/photo-1491553895911-0055eca6402d?w=600&q=80&auto=format&fit=crop',
 220, 1, '44', 'Blue Void/Silver',
 NOW() - INTERVAL '1 week', NOW() - INTERVAL '1 week');


-- ============================================================
-- 7. ORDERS (17 pedidos)
-- order_details: text[] con los IDs de order_details del pedido
-- total: suma de (price × qty) de cada línea + shipping_fee
-- ============================================================
INSERT INTO orders (id, user_id, status, payment_type, shipping_method, shipping_fee, shipping_address, total, order_details, created_at, updated_at) VALUES

-- ===== CARLOS (4 pedidos) =====
('cab00007-0000-0000-0000-000000000001',
 'cab00002-0000-0000-0000-000000000001',
 'DELIVERED', 'CREDIT_CARD', 'Free Shipping', 0,
 'Calle Mayor 15, 3ºA, Madrid 28001, Spain',
 315,  -- 180 + 135
 ARRAY['cab00006-0000-0000-0000-000000000001','cab00006-0000-0000-0000-000000000002'],
 NOW() - INTERVAL '3 months', NOW() - INTERVAL '2 months 20 days'),

('cab00007-0000-0000-0000-000000000002',
 'cab00002-0000-0000-0000-000000000001',
 'DELIVERED', 'CREDIT_CARD', 'Standard Shipping', 6,
 'Calle Mayor 15, 3ºA, Madrid 28001, Spain',
 116,  -- 110 + 6
 ARRAY['cab00006-0000-0000-0000-000000000003'],
 NOW() - INTERVAL '2 months', NOW() - INTERVAL '1 month 20 days'),

('cab00007-0000-0000-0000-000000000003',
 'cab00002-0000-0000-0000-000000000001',
 'SHIPPED', 'CREDIT_CARD', 'Express Shipping', 10,
 'Calle Mayor 15, 3ºA, Madrid 28001, Spain',
 260,  -- 250 + 10
 ARRAY['cab00006-0000-0000-0000-000000000004'],
 NOW() - INTERVAL '3 weeks', NOW() - INTERVAL '2 weeks'),

('cab00007-0000-0000-0000-000000000004',
 'cab00002-0000-0000-0000-000000000001',
 'PROCESSING', 'CREDIT_CARD', 'Free Shipping', 0,
 'Calle Mayor 15, 3ºA, Madrid 28001, Spain',
 450,  -- 230 + 220
 ARRAY['cab00006-0000-0000-0000-000000000005','cab00006-0000-0000-0000-000000000006'],
 NOW() - INTERVAL '1 week', NOW() - INTERVAL '1 week'),

-- ===== MARÍA (3 pedidos) =====
('cab00007-0000-0000-0000-000000000005',
 'cab00002-0000-0000-0000-000000000002',
 'DELIVERED', 'CREDIT_CARD', 'Free Shipping', 0,
 'Avda. Diagonal 200, 5ºB, Barcelona 08013, Spain',
 300,  -- 150×2
 ARRAY['cab00006-0000-0000-0000-000000000007'],
 NOW() - INTERVAL '4 months', NOW() - INTERVAL '3 months 20 days'),

('cab00007-0000-0000-0000-000000000006',
 'cab00002-0000-0000-0000-000000000002',
 'DELIVERED', 'CREDIT_CARD', 'Free Shipping', 0,
 'Avda. Diagonal 200, 5ºB, Barcelona 08013, Spain',
 215,  -- 120 + 95
 ARRAY['cab00006-0000-0000-0000-000000000008','cab00006-0000-0000-0000-000000000009'],
 NOW() - INTERVAL '2 months', NOW() - INTERVAL '1 month 20 days'),

('cab00007-0000-0000-0000-000000000007',
 'cab00002-0000-0000-0000-000000000002',
 'CANCELLED', 'CREDIT_CARD', 'Standard Shipping', 6,
 'Avda. Diagonal 200, 5ºB, Barcelona 08013, Spain',
 111,  -- 105 + 6
 ARRAY['cab00006-0000-0000-0000-000000000010'],
 NOW() - INTERVAL '1 month', NOW() - INTERVAL '3 weeks 5 days'),

-- ===== ALEJANDRO (5 pedidos) =====
('cab00007-0000-0000-0000-000000000008',
 'cab00002-0000-0000-0000-000000000003',
 'DELIVERED', 'CREDIT_CARD', 'Standard Shipping', 6,
 'Calle Serrano 45, 2ºD, Madrid 28006, Spain',
 166,  -- 160 + 6
 ARRAY['cab00006-0000-0000-0000-000000000011'],
 NOW() - INTERVAL '5 months', NOW() - INTERVAL '4 months 20 days'),

('cab00007-0000-0000-0000-000000000009',
 'cab00002-0000-0000-0000-000000000003',
 'DELIVERED', 'CREDIT_CARD', 'Free Shipping', 0,
 'Calle Serrano 45, 2ºD, Madrid 28006, Spain',
 270,  -- 130 + 140
 ARRAY['cab00006-0000-0000-0000-000000000012','cab00006-0000-0000-0000-000000000013'],
 NOW() - INTERVAL '4 months', NOW() - INTERVAL '3 months 20 days'),

('cab00007-0000-0000-0000-000000000010',
 'cab00002-0000-0000-0000-000000000003',
 'DELIVERED', 'CREDIT_CARD', 'Standard Shipping', 6,
 'Paseo de Gracia 88, 4ºA, Barcelona 08008, Spain',
 141,  -- 135 + 6
 ARRAY['cab00006-0000-0000-0000-000000000014'],
 NOW() - INTERVAL '3 months', NOW() - INTERVAL '2 months 20 days'),

('cab00007-0000-0000-0000-000000000011',
 'cab00002-0000-0000-0000-000000000003',
 'SHIPPED', 'PAYPAL', 'Express Shipping', 10,
 'Paseo de Gracia 88, 4ºA, Barcelona 08008, Spain',
 190,  -- 180 + 10
 ARRAY['cab00006-0000-0000-0000-000000000015'],
 NOW() - INTERVAL '2 weeks', NOW() - INTERVAL '10 days'),

('cab00007-0000-0000-0000-000000000012',
 'cab00002-0000-0000-0000-000000000003',
 'PROCESSING', 'PAYPAL', 'Free Shipping', 0,
 'Paseo de Gracia 88, 4ºA, Barcelona 08008, Spain',
 375,  -- 125 + 250
 ARRAY['cab00006-0000-0000-0000-000000000016','cab00006-0000-0000-0000-000000000017'],
 NOW() - INTERVAL '3 days', NOW() - INTERVAL '3 days'),

-- ===== ISABEL (2 pedidos) =====
('cab00007-0000-0000-0000-000000000013',
 'cab00002-0000-0000-0000-000000000004',
 'DELIVERED', 'CREDIT_CARD', 'Free Shipping', 0,
 'Calle Sierpes 22, 1ºC, Sevilla 41001, Spain',
 215,  -- 110 + 105
 ARRAY['cab00006-0000-0000-0000-000000000018','cab00006-0000-0000-0000-000000000019'],
 NOW() - INTERVAL '2 months', NOW() - INTERVAL '1 month 20 days'),

('cab00007-0000-0000-0000-000000000014',
 'cab00002-0000-0000-0000-000000000004',
 'PROCESSING', 'CREDIT_CARD', 'Standard Shipping', 6,
 'Calle Sierpes 22, 1ºC, Sevilla 41001, Spain',
 141,  -- 135 + 6
 ARRAY['cab00006-0000-0000-0000-000000000020'],
 NOW() - INTERVAL '5 days', NOW() - INTERVAL '5 days'),

-- ===== DIEGO (3 pedidos) =====
('cab00007-0000-0000-0000-000000000015',
 'cab00002-0000-0000-0000-000000000005',
 'DELIVERED', 'DEBIT_CARD', 'Standard Shipping', 6,
 'Gran Vía 100, 6ºB, Bilbao 48001, Spain',
 236,  -- 230 + 6
 ARRAY['cab00006-0000-0000-0000-000000000021'],
 NOW() - INTERVAL '3 months', NOW() - INTERVAL '2 months 20 days'),

('cab00007-0000-0000-0000-000000000016',
 'cab00002-0000-0000-0000-000000000005',
 'DELIVERED', 'DEBIT_CARD', 'Free Shipping', 0,
 'Gran Vía 100, 6ºB, Bilbao 48001, Spain',
 380,  -- 180 + 200
 ARRAY['cab00006-0000-0000-0000-000000000022','cab00006-0000-0000-0000-000000000023'],
 NOW() - INTERVAL '6 weeks', NOW() - INTERVAL '5 weeks'),

('cab00007-0000-0000-0000-000000000017',
 'cab00002-0000-0000-0000-000000000005',
 'SHIPPED', 'DEBIT_CARD', 'Standard Shipping', 6,
 'Gran Vía 100, 6ºB, Bilbao 48001, Spain',
 226,  -- 220 + 6
 ARRAY['cab00006-0000-0000-0000-000000000024'],
 NOW() - INTERVAL '1 week', NOW() - INTERVAL '5 days');


-- ============================================================
-- 8. WISHES (listas de deseos por usuario)
-- user_wishes: text[] con product_ids
-- ============================================================
INSERT INTO wishes (id, user_id, total_of_items, user_wishes, created_at, modified_at) VALUES

('cab00008-0000-0000-0000-000000000001',
 'cab00002-0000-0000-0000-000000000001',
 3,
 ARRAY[
   'cab00003-0000-0000-0000-00000000000d',  -- Dunk Low
   'cab00003-0000-0000-0000-000000000008',  -- LeBron 21
   'cab00003-0000-0000-0000-000000000009'   -- KD 16
 ],
 NOW() - INTERVAL '4 months', NOW() - INTERVAL '1 week'),

('cab00008-0000-0000-0000-000000000002',
 'cab00002-0000-0000-0000-000000000002',
 4,
 ARRAY[
   'cab00003-0000-0000-0000-000000000007',  -- Air Jordan 1
   'cab00003-0000-0000-0000-000000000003',  -- Vomero 17
   'cab00003-0000-0000-0000-00000000000f',  -- Air Max Plus
   'cab00003-0000-0000-0000-000000000011'   -- Metcon 9
 ],
 NOW() - INTERVAL '3 months', NOW() - INTERVAL '2 weeks'),

('cab00008-0000-0000-0000-000000000003',
 'cab00002-0000-0000-0000-000000000003',
 2,
 ARRAY[
   'cab00003-0000-0000-0000-000000000007',  -- Air Jordan 1
   'cab00003-0000-0000-0000-00000000000a'   -- Cosmic Unity 3
 ],
 NOW() - INTERVAL '2 months', NOW() - INTERVAL '1 month'),

('cab00008-0000-0000-0000-000000000004',
 'cab00002-0000-0000-0000-000000000004',
 4,
 ARRAY[
   'cab00003-0000-0000-0000-00000000000d',  -- Dunk Low
   'cab00003-0000-0000-0000-00000000000f',  -- Air Max Plus
   'cab00003-0000-0000-0000-000000000004',  -- Air Max 270
   'cab00003-0000-0000-0000-000000000001'   -- Air Zoom Pegasus
 ],
 NOW() - INTERVAL '2 months', NOW() - INTERVAL '3 days'),

('cab00008-0000-0000-0000-000000000005',
 'cab00002-0000-0000-0000-000000000005',
 3,
 ARRAY[
   'cab00003-0000-0000-0000-000000000009',  -- KD 16
   'cab00003-0000-0000-0000-00000000000a',  -- Cosmic Unity 3
   'cab00003-0000-0000-0000-000000000006'   -- ZoomX Vaporfly
 ],
 NOW() - INTERVAL '2 months', NOW() - INTERVAL '2 weeks');


COMMIT;
