-- V1: add order_id FK to order_details + state field to address
-- Applied on top of the baseline schema (existing tables already exist).

-- 1. order_details: add order_id FK (nullable — populated below for existing rows)
ALTER TABLE order_details
    ADD COLUMN IF NOT EXISTS order_id UUID REFERENCES orders(id) ON DELETE CASCADE;

-- 2. Backfill order_id for rows that were created before this migration.
--    orders.order_details is a text[] of UUID strings — match by casting.
UPDATE order_details od
SET order_id = o.id
FROM orders o
WHERE od.id::text = ANY(o.order_details);

-- 3. address: add state (nullable, not all existing rows have it)
ALTER TABLE address
    ADD COLUMN IF NOT EXISTS state VARCHAR(255);
