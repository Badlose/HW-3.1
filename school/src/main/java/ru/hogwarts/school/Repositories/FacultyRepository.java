package ru.hogwarts.school.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.Model.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Faculty findFacultyByNameAndColorIgnoreCase(String name, String color);

    Faculty findFacultyByNameIgnoreCase(String name);

    Faculty findFacultyByColorIgnoreCase(String color);

}
