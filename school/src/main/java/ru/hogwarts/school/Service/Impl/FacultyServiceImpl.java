package ru.hogwarts.school.Service.Impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;
import ru.hogwarts.school.Repositories.FacultyRepository;
import ru.hogwarts.school.Service.FacultyService;

import java.util.Collection;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public void removeFaculty(long id) {
        facultyRepository.deleteById(id);
    }

    @Override
    public Faculty editFaculty(Long facultyId, Faculty faculty) {
        Faculty facultyFromDb = facultyRepository.findById(facultyId).orElseThrow(IllegalArgumentException::new);

        facultyFromDb.setName(faculty.getName());
        facultyFromDb.setColor(faculty.getColor());

        return facultyRepository.save(facultyFromDb);
    }

    @Override
    public Faculty getFaculty(long id) {
        return facultyRepository.findById(id).get();
    }

    public Collection<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    @Override
    public Faculty findFacultyByNameOrColor(String name, String color) {
        return facultyRepository.findFacultyByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    @Override
    public Faculty findFacultyByColor(String color) {
        return facultyRepository.findFacultyByColorIgnoreCase(color);
    }

    @Override
    public Collection<Student> findStudentByFacultyId(Long facultyId) {
        return getFaculty(facultyId).getStudents();
    }
}
