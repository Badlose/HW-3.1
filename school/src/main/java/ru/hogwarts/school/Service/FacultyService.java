package ru.hogwarts.school.Service;

import ru.hogwarts.school.Model.Faculty;

import java.util.Collection;
import java.util.List;

public interface FacultyService {

    Faculty addFaculty(Faculty faculty);


    void removeFaculty(long id);


    Faculty editFaculty(Faculty faculty);

    Faculty getFaculty(long id);

    Collection<Faculty> getAllFaculties();

    List<Faculty> getAllFacultiesByColor(String color);
}