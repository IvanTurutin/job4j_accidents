CREATE TABLE IF NOT EXISTS accident_types
(
  id SERIAL PRIMARY KEY,
  name VARCHAR NOT NULL
);

comment on table accident_types is 'Типы нарушений';
comment on column accident_types.id is 'Идентификатор типа нарушения';
comment on column accident_types.name is 'Название типа нарушения';
