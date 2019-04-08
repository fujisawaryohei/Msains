CREATE TABLE Posts(
 id bigint NOT NULL,
 user_id UUID,
 title varchar(10),
 content varchar(255),
 image_url varchar(10),
 type varchar(10),
 like_flag boolean,
 created_at datetime,
 PRIMARY KEY(id),
 PRIMARY KEY(user_id),
 FOREIGN KEY(user_id)
);

drop table if exists Posts;
