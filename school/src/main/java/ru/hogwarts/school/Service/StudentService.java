package ru.hogwarts.school.Service;

import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentService {

    Student addStudent(Student student);

    void removeStudent(long id);

    Student editStudent(Long studentId, Student student);

    Student getStudent(long id);

    Collection<Student> getAllStudents();

    List<Student> getAllStudentsByAge(int age);

    Collection<Student> findStudentsAgeBetween(int min, int max);

    Faculty findFacultyByStudentId(long id);

    Long getStudentsCount();

    Long getStudentsAverageAge();

    Collection<Student> getLastFiveStudents();

    List<String> getSortedStudentsNameStartsWithA();

    Double getStudentsAverageAgeByUsingStream();

    void getStudentsNameByUsingStream();

    void getStudentsNameBySynchronizedStream();

}
