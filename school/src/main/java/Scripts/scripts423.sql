select student.name, student.age, faculty.name
from student inner join faculty on student.faculty = faculty.id;

select student.name, student.age
from avatar inner join student on avatar.student_id = student.id;