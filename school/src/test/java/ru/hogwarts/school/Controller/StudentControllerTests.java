package ru.hogwarts.school.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;
import ru.hogwarts.school.Repositories.FacultyRepository;
import ru.hogwarts.school.Repositories.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test-postgres.properties")
public class StudentControllerTests {
    private final String name = "Test Student_2";
    private final int age = 123;
    private long id = 1L;
    HttpStatusCode httpStatusCode = HttpStatusCode.valueOf(200);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @BeforeEach
    public void cleanUp() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    void shouldPostStudent() {

        Student student = new Student(name, age);

        ResponseEntity<Student> studentResponseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/students",
                student,
                Student.class);

        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Student actualStudent = studentResponseEntity.getBody();
        assertNotNull(actualStudent.getId());
        assertEquals(actualStudent.getName(), student.getName());
        assertThat(actualStudent.getAge()).isEqualTo(student.getAge());
    }

    @Test
    void shouldGetStudent() {
        Student student = new Student(name, age);
        studentRepository.save(student);

        ResponseEntity<Student> studentRepositoryEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/students/" + student.getId(), Student.class);


        assertNotNull(studentRepositoryEntity);
        assertEquals(studentRepositoryEntity.getStatusCode(), httpStatusCode);

        Student actualStudent = studentRepositoryEntity.getBody();
        assertEquals(actualStudent.getId(), student.getId());
        assertEquals(actualStudent.getName(), student.getName());
        assertEquals(actualStudent.getAge(), student.getAge());
    }

    @Test
    void shouldResponseNotFoundWhenGetStudent() {
        Student student = new Student(name, age);
        studentRepository.save(student);

        ResponseEntity<Student> studentRepositoryEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/students/" + -1, Student.class);

        assertNotNull(studentRepositoryEntity);
        assertEquals(HttpStatusCode.valueOf(404), studentRepositoryEntity.getStatusCode());

        Student actualStudent = studentRepositoryEntity.getBody();
        assertNull(actualStudent);
    }

    @Test
    void shouldPutStudent() {
        Student student = new Student(name, age);
        studentRepository.save(student);

        Student newStudent = new Student("newStudent", 321);

        HttpEntity<Student> entity = new HttpEntity<>(newStudent);
        ResponseEntity<Student> studentResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/students/" + student.getId(),
                HttpMethod.PUT,
                entity,
                Student.class);

        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), httpStatusCode);

        Student actualStudent = studentResponseEntity.getBody();
        assertEquals(actualStudent.getId(), student.getId());
        assertEquals(actualStudent.getName(), newStudent.getName());
        assertThat(actualStudent.getAge()).isEqualTo(newStudent.getAge());
    }

    @Test
    void shouldDeleteStudent() {
        Student student = new Student(name, age);
        studentRepository.save(student);

        ResponseEntity<Void> studentResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/students/" + student.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class);

        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), httpStatusCode);
    }

    @Test
    void shouldGetAllStudents() {
        Student student1 = new Student(name, age);
        Student student2 = new Student(name, age + 2);
        studentRepository.save(student1);
        studentRepository.save(student2);

        ResponseEntity<Collection<Student>> studentResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/students",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertNotNull(studentResponseEntity);
        assertEquals(httpStatusCode, studentResponseEntity.getStatusCode());

        List<Student> studentsList = Objects.requireNonNull(studentResponseEntity.getBody()).stream().toList();
        assertTrue(studentsList.size() > 0);
        assertTrue(studentsList.stream().anyMatch(student -> student.getName().equals(student1.getName()) &&
                student.getAge() == student1.getAge()));
    }

    @Test
    void shouldGetStudentByAge() {
        Student myStudent = new Student(name, age + 333);
        studentRepository.save(myStudent);

        ResponseEntity<Collection<Student>> studentResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/students/age/" + myStudent.getAge(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertNotNull(studentResponseEntity);
        assertEquals(httpStatusCode, studentResponseEntity.getStatusCode());

        List<Student> studentsList = Objects.requireNonNull(studentResponseEntity.getBody()).stream().toList();
        assertTrue(studentsList.size() > 0);
        assertTrue(studentsList.stream().anyMatch(student ->
                student.getName().equals(myStudent.getName()) &&
                        student.getAge() == myStudent.getAge()));
    }

    @Test
    void shouldGetStudentsByAgeBetween() {
        Student student1 = new Student(name, age);
        Student student2 = new Student(name, age + 2);
        studentRepository.save(student1);
        studentRepository.save(student2);
        int ageMin = 122;
        int ageMax = 130;

        ResponseEntity<Collection<Student>> studentResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/students/age?min=" + ageMin + "&max=" + ageMax,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });


        assertNotNull(studentResponseEntity);
        assertEquals(httpStatusCode, studentResponseEntity.getStatusCode());

        List<Student> studentsList = Objects.requireNonNull(studentResponseEntity.getBody()).stream().toList();
        assertTrue(studentsList.size() > 0);
        assertTrue(studentsList.stream().anyMatch(s -> s.getName().equals(student1.getName()) &&
                s.getAge() == student2.getAge()));
    }

    @Test
    void shouldGetFacultyByStudentId() {
        String facultyName = "Test fac1";
        String facultyColor = "red";
        Faculty faculty = new Faculty(facultyName, facultyColor);
        facultyRepository.save(faculty);
        Student student = new Student(name, age, faculty);
        studentRepository.save(student);

        ResponseEntity<Faculty> studentResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/students/faculty/" + student.getId(),
                HttpMethod.GET,
                null,
                Faculty.class);

        assertNotNull(studentResponseEntity);
        assertEquals(httpStatusCode, studentResponseEntity.getStatusCode());

        Faculty actualFaculty = studentResponseEntity.getBody();
        assertEquals(faculty.getName(), actualFaculty.getName());
        assertEquals(faculty.getColor(), actualFaculty.getColor());
    }
}
