package ru.hogwarts.school.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.hogwarts.school.Model.Student;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("postgres-test")
public class StudentControllerTests {
    private final String name = "Test Student_2";
    private final int age = 123;
    private long id = 999L;

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void shouldPostStudent() {
        Student student = new Student(id, name, age);

        System.out.println(port);
        System.out.println(student);
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/students", student, Stringz.class))
                .isNotNull();
    }

}
