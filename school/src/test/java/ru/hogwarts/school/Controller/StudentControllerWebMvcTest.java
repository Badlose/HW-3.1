package ru.hogwarts.school.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;
import ru.hogwarts.school.Service.AvatarService;
import ru.hogwarts.school.Service.StudentService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {
    private final Long studentId = 1L;
    private final String studentName = "Test mvc student name";
    private final int studentAge = 70;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private AvatarService avatarService;

    @Test
    void shouldPostStudent() throws Exception {
        Student student = new Student(studentName, studentAge);
        Student savedStudent = new Student(studentName, studentAge);
        savedStudent.setId(studentId);

        when(studentService.addStudent(student)).thenReturn(savedStudent);


        ResultActions perform = mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));

        perform
                .andExpect(jsonPath("$.id").value(savedStudent.getId()))
                .andExpect(jsonPath("$.name").value(savedStudent.getName()))
                .andExpect(jsonPath("$.age").value(savedStudent.getAge()))
                .andDo(print());
    }

    @Test
    void shouldGetStudent() throws Exception {
        Student student = new Student(studentName, studentAge);

        when(studentService.getStudent(studentId)).thenReturn(student);

        ResultActions perform = mockMvc.perform(get("/students/{id}", studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));

        perform
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()))
                .andDo(print());
    }

    @Test
    void shouldPutStudent() throws Exception {
        Student student = new Student(studentName, studentAge);
        Student newStudent = new Student(studentName, studentAge + 2);

        when(studentService.editStudent(studentId, newStudent)).thenReturn(newStudent);

        ResultActions perform = mockMvc.perform((put("/students/{id}", studentId, newStudent)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newStudent))));

        perform
                .andExpect(jsonPath("$.name").value(newStudent.getName()))
                .andExpect(jsonPath("$.age").value(newStudent.getAge()))
                .andExpect(jsonPath("$.id").value(newStudent.getId()))
                .andDo(print());
    }

    @Test
    void shouldDeleteStudent() throws Exception {
        Student student = new Student(studentId, studentName, studentAge);
        when(studentService.getStudent(studentId)).thenReturn(student);


        ResultActions perform = mockMvc.perform(delete("/students/{id}", studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));

        perform
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldGetAllStudents() throws Exception {
        Student student1 = new Student(studentName, studentAge);
        Student student2 = new Student(studentName, studentAge + 2);
        List<Student> studentsList = List.of(student1, student2);

        when(studentService.getAllStudents()).thenReturn(studentsList);

        ResultActions perform = mockMvc.perform(get("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentsList)));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(student1.getName()))
                .andExpect((jsonPath("$[1].name").value(student2.getName())))
                .andDo(print());
    }

    @Test
    void shouldGetStudentByAge() throws Exception {
        Student student = new Student(studentName, studentAge);
        List<Student> studentList = List.of(student);

        when(studentService.getAllStudentsByAge(student.getAge())).thenReturn(studentList);

        ResultActions perform = mockMvc.perform(get("/students/age/{age}", student.getAge())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentList)));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(student.getName()))
                .andExpect(jsonPath("$[0].age").value(student.getAge()))
                .andDo(print());
    }

    @Test
    void shouldFindStudentsAgeBetween() throws Exception {
        Student student = new Student(studentName, studentAge);
        List<Student> studentList = List.of(student);

        when(studentService.findStudentsAgeBetween(studentAge - 1, studentAge + 1)).thenReturn(studentList);

        ResultActions perform = mockMvc.perform(get("/students/age")
                .contentType(MediaType.APPLICATION_JSON)
                .param("min", "69")
                .param("max", "71")
                .content(objectMapper.writeValueAsString(studentList)));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(student.getName()))
                .andExpect(jsonPath("$[0].age").value(student.getAge()))
                .andDo(print());
    }

    @Test
    void shouldGetFacultyByStudentId() throws Exception {
        String facultyName = "Test fac1";
        String facultyColor = "red";
        Faculty faculty = new Faculty(facultyName, facultyColor);

        when(studentService.findFacultyByStudentId(studentId)).thenReturn(faculty);

        ResultActions perform = mockMvc.perform(get("/students/faculty/{id}", studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faculty)));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());
    }

}

