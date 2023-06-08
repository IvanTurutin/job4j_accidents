CREATE TABLE IF NOT EXISTS accidents
(
  id SERIAL PRIMARY KEY,
  name VARCHAR NOT NULL,
  text VARCHAR NOT NULL,
  address VARCHAR NOT NULL,
  accident_type_id INT NOT NULL REFERENCES accident_types(id)
);

comment on table accidents is 'Нарушения';
comment on column accidents.id is 'Идентификатор нарушения';
comment on column accidents.name is 'Название нарушения';
comment on column accidents.text is 'Описание нарушения';
comment on column accidents.address is 'Адрес нарушения';
comment on column accidents.accident_type_id is 'Идентификатор типа нарушения';
