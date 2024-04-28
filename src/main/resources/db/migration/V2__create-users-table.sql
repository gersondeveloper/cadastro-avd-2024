CREATE TABLE users (
  id bigint IDENTITY (1, 1) NOT NULL,
   login varchar(255),
   name varchar(255),
   password varchar(255),
   CONSTRAINT pk_users PRIMARY KEY (id)
)
GO


ALTER TABLE users ADD CONSTRAINT uc_users_login UNIQUE (login)
GO