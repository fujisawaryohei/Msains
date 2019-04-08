CREATE TABLE Subjects(
 id bigint,
 name varchar(10),
 teacher_name varchar(10),
 room_number int,
 unit int,
 week_code int,
 start_period datetime,
 end_period datetime,
 PRIMARY KEY(id)
);

drop table if exists Subjects;
