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
import ru.hogwarts.school.Service.FacultyService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {
    private final Long facultyId = 1L;
    private final String facultyName = "Test mvc faculty name";
    private final String facultyColor = "red";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FacultyService facultyService;

    @Test
    void shouldGetFaculty() throws Exception {
        Faculty faculty = new Faculty(facultyName, facultyColor);

        when(facultyService.getFaculty(facultyId)).thenReturn(faculty);

        ResultActions perform = mockMvc.perform(get("/faculties/{id}", facultyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faculty)));

        perform
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());
    }

    @Test
    void shouldResponseStatusNotFoundWhenGetFaculty() throws Exception {
        when(facultyService.getFaculty(facultyId)).thenReturn(null);

        ResultActions perform = mockMvc.perform(get("/faculties/{id}", facultyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null)));

        perform
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldPostFaculty() throws Exception {
        Faculty faculty = new Faculty(facultyName, facultyColor);
        Faculty newFaculty = new Faculty("newFaculty", facultyColor);

        when(facultyService.addFaculty(faculty)).thenReturn(newFaculty);

        ResultActions perform = mockMvc.perform(post("/faculties")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faculty)));

        perform
                .andExpect(jsonPath("$.id").value(newFaculty.getId()))
                .andExpect(jsonPath("$.name").value(newFaculty.getName()))
                .andExpect(jsonPath("$.color").value(newFaculty.getColor()))
                .andDo(print());
    }

    @Test
    void shouldPutFaculty() throws Exception {
        Faculty faculty = new Faculty(facultyName, facultyColor);
        Faculty newFaculty = new Faculty("newFaculty", facultyColor);

        when(facultyService.editFaculty(facultyId, newFaculty)).thenReturn(newFaculty);

        ResultActions perform = mockMvc.perform(put("/faculties/{id}", facultyId, newFaculty)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newFaculty)));

        perform
                .andExpect(jsonPath("$.name").value(newFaculty.getName()))
                .andExpect(jsonPath("$.color").value(newFaculty.getColor()))
                .andExpect(jsonPath("$.id").value(newFaculty.getId()))
                .andDo(print());
    }

    @Test
    void shouldDeleteFaculty() throws Exception {
        Faculty faculty = new Faculty(facultyName, facultyColor);

        when(facultyService.getFaculty(facultyId)).thenReturn(faculty);

        ResultActions perform = mockMvc.perform(delete("/faculties/{id}", facultyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faculty)));

        perform
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldGetAllFaculties() throws Exception {
        Faculty faculty1 = new Faculty(facultyName, facultyColor);
        Faculty faculty2 = new Faculty("newFaculty", facultyColor);
        List<Faculty> faculties = List.of(faculty1, faculty2);

        when(facultyService.getAllFaculties()).thenReturn(faculties);

        ResultActions perform = mockMvc.perform(get("/faculties/all")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faculties)));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(faculty1.getName()))
                .andExpect((jsonPath("$[1].name").value(faculty2.getName())))
                .andDo(print());
    }

    @Test
    void shouldGetFacultyByColor() throws Exception {
        Faculty faculty = new Faculty(facultyName, facultyColor);

        when(facultyService.findFacultyByColor(faculty.getColor())).thenReturn(faculty);

        ResultActions perform = mockMvc.perform(get("/faculties/color/{color}", faculty.getColor())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faculty)));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());
    }

    @Test
    void shouldGetFacultyByNameOrColor() throws Exception {
        Faculty faculty = new Faculty(facultyName, facultyColor);

        when(facultyService.findFacultyByNameOrColor(faculty.getColor(), faculty.getColor())).thenReturn(faculty);

        ResultActions perform = mockMvc.perform(get("/faculties")
                .contentType(MediaType.APPLICATION_JSON)
                .param("query", faculty.getColor())
                .content(objectMapper.writeValueAsString(faculty)));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());
    }

    @Test
    void shouldGetStudentsByFaculty() throws Exception {
//        Faculty faculty = new Faculty(facultyName, facultyColor);
//        faculty.setId(facultyId);
        String studentName = "Test mvc student name";
        int studentAge = 70;
        Student student1 = new Student(studentName, studentAge);
        Student student2 = new Student(studentName, studentAge + 2);
        List<Student> studentsList = List.of(student1, student2);

        when(facultyService.findStudentByFacultyId(facultyId)).thenReturn(studentsList);

        ResultActions perform = mockMvc.perform(get("/faculties/students/{facultyId}", facultyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentsList)));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(student1.getName()))
                .andExpect(jsonPath("$[1].age").value(student2.getAge()))
                .andDo(print());
    }

}
