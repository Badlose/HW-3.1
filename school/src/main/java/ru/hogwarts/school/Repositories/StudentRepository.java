package ru.hogwarts.school.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.Model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
