create table if not exists categories (
   id serial primary key,
   name varchar
);

CREATE TABLE if not exists tasks_categories (
    id serial primary key,
    task_id int not null references tasks,
    categories_id int not null references categories
);

INSERT INTO categories(name) values ('Other');
INSERT INTO categories(name) values ('Work');
INSERT INTO categories(name) values ('Hobby');
INSERT INTO categories(name) values ('Sport');
