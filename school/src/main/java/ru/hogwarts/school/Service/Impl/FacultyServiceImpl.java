package ru.hogwarts.school.Service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;
import ru.hogwarts.school.Repositories.FacultyRepository;
import ru.hogwarts.school.Service.FacultyService;

import java.util.Collection;
import java.util.Comparator;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);

    @Override
    public Faculty addFaculty(Faculty faculty) {
        logger.info("Adding new faculty: {}", faculty);
        return facultyRepository.save(faculty);
    }

    @Override
    public void removeFaculty(long id) {
        logger.info("Deleting faculty by faculty ID: {}", id);
        facultyRepository.deleteById(id);
    }

    @Override
    public Faculty editFaculty(Long facultyId, Faculty faculty) {
        logger.info("Editing the faculty by id: {}", facultyId);
        Faculty facultyFromDb = facultyRepository.findById(facultyId).orElseThrow(IllegalArgumentException::new);

        facultyFromDb.setName(faculty.getName());
        facultyFromDb.setColor(faculty.getColor());

        logger.info("The faculty has been edited. Now it is: {}", facultyFromDb);
        return facultyRepository.save(facultyFromDb);
    }

    @Override
    public Faculty getFaculty(long id) {
        logger.info("Getting faculty by id: {}", id);
        return facultyRepository.findById(id).get();
    }

    public Collection<Faculty> getAllFaculties() {
        logger.info("Getting all faculties");
        return facultyRepository.findAll();
    }

    @Override
    public Faculty findFacultyByNameOrColor(String name, String color) {
        logger.info("Trying to find faculty by name: {} or color: {}", name, color);
        return facultyRepository.findFacultyByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    @Override
    public Faculty findFacultyByColor(String color) {
        logger.info("Trying to find faculty by color: {}", color);
        return facultyRepository.findFacultyByColorIgnoreCase(color);
    }

    @Override
    public Collection<Student> findStudentByFacultyId(Long facultyId) {
        logger.info("Trying to find student by faculty id: {}", facultyId);
        return getFaculty(facultyId).getStudents();
    }

    @Override
    public String getLongestFacultyName() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length))
                .orElse(null);
    }
}
