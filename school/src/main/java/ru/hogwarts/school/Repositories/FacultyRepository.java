package ru.hogwarts.school.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.Model.Faculty;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Faculty findFacultyByNameIgnoreCaseOrColorIgnoreCase(String name, String color);

    Faculty findFacultyByNameIgnoreCase(String name);

    Faculty findFacultyByColorIgnoreCase(String color);

    Faculty findFacultyByColor(String color);

}
