package ru.hogwarts.school.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;
import ru.hogwarts.school.Service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable long id) {
        Student student = studentService.getStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> editStudent(@PathVariable Long id, @RequestBody Student student) {
        Student foundStudent = studentService.editStudent(id, student);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable long id) {
        studentService.removeStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public Collection<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/age/{age}")
    public ResponseEntity<Collection<Student>> getStudentsByAge(@PathVariable int age) {
        List<Student> foundedStudents = studentService.getAllStudentsByAge(age);
        if (foundedStudents == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundedStudents);
    }

    @GetMapping("/age")
    public Collection<Student> findStudentsAgeBetween(@RequestParam(value = "min") int min,
                                                      @RequestParam(value = "max") int max) {
        return studentService.findStudentsAgeBetween(min, max);
    }

    @GetMapping("/faculty/{id}")
    public Faculty getFacultyByStudentId(@PathVariable long id) {
        return studentService.findFacultyByStudentId(id);
    }

    @GetMapping("/count")
    public Long getStudentsAmount() {
        return studentService.getStudentsCount();
    }

    @GetMapping("/averageAge")
    public Long getStudentsAverageAge() {
        return studentService.getStudentsAverageAge();
    }

    @GetMapping("/lastFive")
    public Collection<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }

    @GetMapping("/nameStartsWithA")
    public List<String> getSortedStudentsNameStartsWithA() {
        return studentService.getSortedStudentsNameStartsWithA();
    }

    @GetMapping("/streamAverageAge")
    public Double getStudentsAverageAgeByUsingStream() {
        return studentService.getStudentsAverageAgeByUsingStream();
    }


    @GetMapping("/print-parallel")
    public void getStudentsNameByUsingStream() {
        studentService.getStudentsNameByUsingStream();
    }

    @GetMapping("/print-synchronized")
    public void getStudentsNameBySynchronizedStream() {
        studentService.getStudentsNameBySynchronizedStream();
    }
}
