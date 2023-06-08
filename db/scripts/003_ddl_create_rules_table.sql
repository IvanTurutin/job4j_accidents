CREATE TABLE IF NOT EXISTS rules
(
  id SERIAL PRIMARY KEY,
  name VARCHAR NOT NULL
);

comment on table rules is 'Статьи нарушений';
comment on column rules.id is 'Идентификатор статьи';
comment on column rules.name is 'Название статьи';
