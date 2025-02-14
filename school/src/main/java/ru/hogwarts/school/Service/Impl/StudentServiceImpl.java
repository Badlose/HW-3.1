package ru.hogwarts.school.Service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;
import ru.hogwarts.school.Repositories.StudentRepository;
import ru.hogwarts.school.Service.StudentService;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Override
    public Student addStudent(Student student) {
        logger.info("Adding the student: {}", student);
        return studentRepository.save(student);
    }

    @Override
    public void removeStudent(long id) {
        logger.info("Deleting the student by ID: {}", id);
        studentRepository.deleteById(id);
    }

    @Override
    public Student editStudent(Long studentId, Student student) {
        logger.info("Editing the student by ID: {}", studentId);
        Student studentFromDb = studentRepository.findById(studentId).orElseThrow(IllegalArgumentException::new);

        studentFromDb.setName(student.getName());
        studentFromDb.setAge(student.getAge());

        logger.info("The student has been edited. Now it is: {}", studentFromDb);

        return studentRepository.save(studentFromDb);
    }

    @Override
    public Student getStudent(long id) {
        logger.info("Trying to find student by id: {}", id);
        return studentRepository.findById(id).get();
    }

    @Override
    public Collection<Student> getAllStudents() {
        logger.info("Getting all students");
        return studentRepository.findAll();
    }

    @Override
    public List<Student> getAllStudentsByAge(int age) {
        logger.info("Getting all students by age: {}", age);
        return studentRepository.findStudentsByAge(age);
    }

    @Override
    public Collection<Student> findStudentsAgeBetween(int min, int max) {
        logger.info("Trying to get all students by age between: {}, {}", min, max);
        return studentRepository.findStudentsByAgeGreaterThanAndAgeLessThan(min, max);
    }

    @Override
    public Faculty findFacultyByStudentId(long id) {
        logger.info("Trying to find Faculty by student id: {}", id);
        Faculty faculty = getStudent(id).getFaculty();
        return faculty;
    }

    @Override
    public Long getStudentsCount() {
        logger.info("Getting students total count");
        return studentRepository.getStudentsCount();
    }

    @Override
    public Long getStudentsAverageAge() {
        logger.info("Getting students average age");
        return studentRepository.getStudentsAverageAge();
    }

    @Override
    public Collection<Student> getLastFiveStudents() {
        logger.info("Getting last five students");
        return studentRepository.getLastFiveStudents();
    }

    @Override
    public List<String> getSortedStudentsNameStartsWithA() {
        return studentRepository.findAll().stream()
                .filter(s -> s.getName().startsWith("A") || s.getName().startsWith("a"))
                .map(s -> s.getName().toLowerCase())
                .sorted(Comparator.comparingInt(String::length))
                .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1))
                .sorted()
                .toList();
    }

    @Override
    public Double getStudentsAverageAgeByUsingStream() {
        return studentRepository.findAll().stream()
                .collect(Collectors.averagingInt(Student::getAge));
    }
}