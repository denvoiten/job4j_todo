create table users (
   id serial primary key,
   name varchar UNIQUE,
   email varchar UNIQUE,
   password TEXT
);

CREATE TABLE if not exists tasks (
    id SERIAL PRIMARY KEY,
    name TEXT,
    description TEXT,
    created Timestamp,
    done boolean,
    user_id INTEGER NOT NULL REFERENCES users (id)
);
