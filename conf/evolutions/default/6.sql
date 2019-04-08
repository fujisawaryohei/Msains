CREATE TABLE Comment_replies(
 comment_id bigint NOT NULL,
 content varchar(255),
 like_flag Boolean,
 created_at Datetime,
 PRIMARY KEY(comment_id),
 FOREIGN KEY(comment_id)
);

drop table if exists Comment_replies;
