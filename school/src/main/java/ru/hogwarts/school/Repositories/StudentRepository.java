package ru.hogwarts.school.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.Model.Student;

import java.util.Collection;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findStudentsByAgeGreaterThanAndAgeLessThan(int min, int max);

    List<Student> findStudentsByAge(int age);

    Student findStudentById(long id);

    @Query(value = "SELECT Count(*) FROM student", nativeQuery = true)
    Long getStudentsCount();

    @Query(value = "SELECT AVG(age) FROM student", nativeQuery = true)
    Long getStudentsAverageAge();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    Collection<Student> getLastFiveStudents();
}
