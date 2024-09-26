drop table flyway_schema_history

DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

SELECT table_name FROM information_schema.tables WHERE table_schema = 'public';

SELECT * FROM flyway_schema_history;

CREATE TYPE notification_type AS ENUM ('EMAIL', 'SMS');