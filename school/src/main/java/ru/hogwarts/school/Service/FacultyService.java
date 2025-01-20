package ru.hogwarts.school.Service;

import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;

import java.util.Collection;

public interface FacultyService {

    Faculty addFaculty(Faculty faculty);

    void removeFaculty(long id);

    Faculty editFaculty(Long facultyId, Faculty faculty);

    Faculty getFaculty(long id);

    Collection<Faculty> getAllFaculties();

    Faculty findFacultyByNameOrColor(String name, String color);

    Faculty findFacultyByColor(String color);

    Collection<Student> findStudentByFacultyId(Long facultyId);
}