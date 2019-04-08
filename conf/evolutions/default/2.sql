CREATE TABLE Timetables (
  user_id uuid NOT NULL,
  subject_id int NOT NULL,
  FOREIGN KEY(user_id),
  FOREIGN KEY(subject_id)
);

drop table if exists Timetalbes;
