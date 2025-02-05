alter TABLE student
ADD CONSTRAINT age_constraint CHECK ( age > 16 ),
alter column age set default 20 ,
add constraint name_unique_constraint unique (name),
alter column name set not null;

alter table faculty
add constraint unique_faculty_color unique (name, color);