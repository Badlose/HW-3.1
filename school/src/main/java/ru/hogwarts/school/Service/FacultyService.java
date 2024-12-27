package ru.hogwarts.school.Service;

import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;

import java.util.Collection;

public interface FacultyService {

    Faculty addFaculty(Faculty faculty);

    void removeFaculty(long id);

    Faculty editFaculty(Faculty faculty);

    Faculty getFaculty(long id);

    Collection<Faculty> getAllFaculties();

    Faculty getFacultyByColor(String color);

    Faculty findFacultyByNameAndColor(String name, String color);

    Faculty findFacultyByName(String name);

    Faculty findFacultyByColor(String color);

    Collection<Student> getStudentsByFacultyName(String facultyName);
}