CREATE TABLE if not exists items (
    id SERIAL PRIMARY KEY,
    name TEXT,
    description TEXT,
    created Timestamp,
    done boolean
);