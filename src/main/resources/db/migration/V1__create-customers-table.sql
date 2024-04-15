CREATE TABLE customers (
  id bigint IDENTITY (1, 1) NOT NULL,
   creation_date datetime,
   created_by varchar(255),
   modification_date datetime,
   modified_by varchar(255),
   name varchar(255),
   email varchar(255),
   phone varchar(255),
   is_active bit NOT NULL,
   CONSTRAINT pk_customers PRIMARY KEY (id)
)
GO

ALTER TABLE customers ADD CONSTRAINT uc_customers_email UNIQUE (email)
GO

ALTER TABLE customers ADD CONSTRAINT uc_customers_name UNIQUE (name)
GO