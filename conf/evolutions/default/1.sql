CREATE TABLE Users (
	id uuid NOT NULL,
	hased_password varchar(10),
	email varchar(10),
	first_name varchar(10),
	last_name varchar(10),
	birthday datetime,
	departure varchar(10),
	profile varchar(255),
	admin_flag boolean
	PRIMARY KEY(id)
);

drop table if exists Users;
