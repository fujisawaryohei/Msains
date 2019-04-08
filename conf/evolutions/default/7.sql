CREATE TABLE Relationships(
 following_id uuid NOT NULL,
 follower_id uuid NOT NULL,
 created_at datetime NOT NULL
);

drop table if exists Relationships;
