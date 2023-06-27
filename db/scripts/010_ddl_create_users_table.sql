CREATE TABLE IF NOT EXISTS users (
id serial primary key,
username VARCHAR(50) NOT NULL unique,
password VARCHAR(100) NOT NULL,
enabled boolean default true,
authority_id int not null references authorities(id)
);

comment on table users is 'Пользователи сервиса';
comment on column users.id is 'Идентификатор пользователя';
comment on column users.username is 'Имя пользователя';
comment on column users.password is 'Пароль пользователя';
comment on column users.enabled is 'Пароль пользователя';
comment on column users.authority_id is 'Идентификтор роли пользователя';
