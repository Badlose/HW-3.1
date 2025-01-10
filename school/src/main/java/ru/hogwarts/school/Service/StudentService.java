package ru.hogwarts.school.Service;

import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentService {

    Student addStudent(Student student);

    void removeStudent(long id);

    Student editStudent(Student student);

    Student getStudent(long id);

    Collection<Student> getAllStudents();

    List<Student> getAllStudentsByAge(int age);

    Collection<Student> findStudentsAgeBetween(int min, int max);

    Faculty getFacultyByStudentId(long id);
}
