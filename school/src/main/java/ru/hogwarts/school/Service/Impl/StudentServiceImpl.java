package ru.hogwarts.school.Service.Impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;
import ru.hogwarts.school.Repositories.StudentRepository;
import ru.hogwarts.school.Service.StudentService;

import java.util.Collection;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void removeStudent(long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student getStudent(long id) {
        return studentRepository.findById(id).get();
    }

    @Override
    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> getAllStudentsByAge(int age) {
        return studentRepository.findStudentsByAge(age);
    }

    @Override
    public Collection<Student> findStudentsAgeBetween(int min, int max) {
        return studentRepository.findStudentsByAgeGreaterThanAndAgeLessThan(min, max);
    }

    @Override
    public Faculty getFacultyByStudentId(long id) {
        return getStudent(id).getFaculty();
    }
}