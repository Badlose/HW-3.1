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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test-postgres.properties")
public class FacultyControllerTest {
    private final String facultyName = "Test Faculty";
    private final String facultyColor = "red";
    private long facultyId = 1L;
    HttpStatusCode httpStatusCode = HttpStatusCode.valueOf(200);
    String student1Name = "Test student 1";
    String student2Name = "Test student 2";
    int student1Age = 1;
    int student2Age = 2;


    @LocalServerPort
    private int port;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void cleanUp() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    void shouldPostFaculty() {

        Faculty faculty = new Faculty(facultyName, facultyColor);

        ResponseEntity<Faculty> facultyRepositoryEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/faculties",
                faculty,
                Faculty.class);

        assertNotNull(facultyRepositoryEntity);
        assertEquals(httpStatusCode, facultyRepositoryEntity.getStatusCode());

        Faculty actualFaculty = facultyRepositoryEntity.getBody();
        assertNotNull(actualFaculty.getId());
        assertEquals(faculty.getName(), actualFaculty.getName());
        assertEquals(faculty.getColor(), actualFaculty.getColor());
    }

    @Test
    void shouldGetFaculty() {

        Faculty faculty = new Faculty(facultyName, facultyColor);
        facultyRepository.save(faculty);

        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculties/" + faculty.getId(), Faculty.class);

        assertNotNull(facultyResponseEntity);
        assertEquals(httpStatusCode, facultyResponseEntity.getStatusCode());

        Faculty actualFaculty = facultyResponseEntity.getBody();
        assertNotNull(actualFaculty.getId());
        assertEquals(faculty.getName(), actualFaculty.getName());
        assertEquals(faculty.getColor(), actualFaculty.getColor());
    }

    @Test
    void shouldResponseStatusNotFoundWhenGetFaculty() {

        Faculty faculty = new Faculty(facultyName, facultyColor);
        facultyRepository.save(faculty);

        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculties/" + -1, Faculty.class);

        assertNotNull(facultyResponseEntity);
        assertEquals(HttpStatusCode.valueOf(404), facultyResponseEntity.getStatusCode());

        Faculty actualFaculty = facultyResponseEntity.getBody();
        assertNull(actualFaculty);
    }

    @Test
    void shouldPutFaculty() {

        Faculty faculty = new Faculty(facultyName, facultyColor);
        facultyRepository.save(faculty);

        Faculty newFaculty = new Faculty("New TestFaculty", "new test color");

        HttpEntity<Faculty> entity = new HttpEntity<>(newFaculty);
        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/faculties/" + faculty.getId(),
                HttpMethod.PUT,
                entity,
                Faculty.class);

        assertNotNull(facultyResponseEntity);
        assertEquals(httpStatusCode, facultyResponseEntity.getStatusCode());

        Faculty actualFaculty = facultyResponseEntity.getBody();
        assertEquals(newFaculty.getName(), actualFaculty.getName());
        assertEquals(newFaculty.getColor(), actualFaculty.getColor());
    }

    @Test
    void shouldDeleteFaculty() {

        Faculty faculty = new Faculty(facultyName, facultyColor);
        facultyRepository.save(faculty);

        ResponseEntity<Void> facultyResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/faculties/" + faculty.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class);

        assertNotNull(facultyResponseEntity);
        assertEquals(httpStatusCode, facultyResponseEntity.getStatusCode());
    }

    @Test
    void shouldGetFacultyByColor() {

        Faculty faculty = new Faculty(facultyName, facultyColor);
        facultyRepository.save(faculty);

        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculties/color/" + faculty.getColor(), Faculty.class);

        assertNotNull(facultyResponseEntity);
        assertEquals(httpStatusCode, facultyResponseEntity.getStatusCode());

        Faculty actualFaculty = facultyResponseEntity.getBody();
        assertEquals(faculty.getColor(), actualFaculty.getColor());
        assertEquals(faculty.getName(), actualFaculty.getName());
    }

    @Test
    void shouldGetFacultyByNameOrColor() {
        Faculty faculty = new Faculty(facultyName, facultyColor);
        facultyRepository.save(faculty);
        String testName = "red";

        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculties?query=" + testName, Faculty.class);

        assertNotNull(facultyResponseEntity);
        assertEquals(httpStatusCode, facultyResponseEntity.getStatusCode());

        System.out.println(faculty);
        System.out.println(facultyResponseEntity.getBody());

        Faculty actualFaculty = facultyResponseEntity.getBody();
        assertEquals(faculty.getColor(), actualFaculty.getColor());
        assertEquals(faculty.getName(), actualFaculty.getName());
    }

    @Test
    void shouldGetStudentsByFaculty() {
        Faculty faculty = new Faculty(facultyName, facultyColor);
        facultyRepository.save(faculty);
        Student student1 = new Student(student1Name, student1Age, faculty);
        Student student2 = new Student(student2Name, student2Age, faculty);
        studentRepository.save(student1);
        studentRepository.save(student2);

        ResponseEntity<Collection<Student>> facultyResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/faculties/students/" + faculty.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertNotNull(facultyResponseEntity);
        assertEquals(httpStatusCode, facultyResponseEntity.getStatusCode());

        List<Student> studentsList = Objects.requireNonNull(facultyResponseEntity.getBody()).stream().toList();
        assertTrue(studentsList.size() > 0);
        assertTrue(studentsList.stream().anyMatch(student -> student.getName().equals(student1.getName())
                && student.getAge() == student1.getAge()));
    }
}