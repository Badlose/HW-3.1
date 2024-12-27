package ru.hogwarts.school.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;
import ru.hogwarts.school.Service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("faculties")
public class FacultyController {
    public final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }


    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable long id) {
        Faculty faculty = facultyService.getFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PostMapping
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @PutMapping("{id}")
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultyService.editFaculty(faculty);
        if (foundFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable(required = false) long id) {
        facultyService.removeFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("all")
    public ResponseEntity<Collection<Faculty>> getAllFaculties() {
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }

    @GetMapping("color/{color}")
    public ResponseEntity<Faculty> getFacultyByColor(@PathVariable String color) {
        Faculty foundedFaculty = facultyService.getFacultyByColor(color);
        if (foundedFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundedFaculty);
    }

    @GetMapping()
    public ResponseEntity<Faculty> getFacultyByNameOrColor(@RequestParam(required = false) String name,
                                                           @RequestParam(required = false) String color) {
        if ((name != null && !name.isBlank()) &&
                (color != null && !color.isBlank())) {
            return ResponseEntity.ok(facultyService.findFacultyByNameAndColor(name, color));
        }

        if (name != null && !name.isBlank()) {

            return ResponseEntity.ok(facultyService.findFacultyByName(name));
        }

        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(facultyService.findFacultyByColor(color));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("students/{facultyName}")
    public ResponseEntity<Collection<Student>> getStudentsByFaculty(@PathVariable String facultyName) {
        Collection<Student> students = facultyService.getStudentsByFacultyName(facultyName);
        if (students == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }
}
