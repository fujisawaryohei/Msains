CREATE TABLE Comments(
 id : bigint NOT NULL,
 user_id : UUID,
 post_id : Int,
 content : varchar(255),
 like_flag : Boolean,
 created_at : Datetime,
 PRIMARY KEY(id),
 PRIMARY KEY(user_id),
 PRIMARY KEY(post_id),
 FOREIGN KEY(user_id),
 FOREIGN KEY(post_id)
);

drop table if exists Comments;
