CREATE TABLE IF NOT EXISTS authorities (
  username VARCHAR(50) NOT NULL,
  authority VARCHAR(50) NOT NULL,
  FOREIGN KEY (username) REFERENCES users(username)
);

comment on table authorities is 'Роли пользователей';
comment on column authorities.username is 'Ссылка на имя пользователя';
comment on column authorities.authority is 'Роль пользователя';
