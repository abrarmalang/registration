package com.school.registration.student;

import com.school.registration.course.Course;
import com.school.registration.util.FilterParam;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class StudentController {

    @Autowired
    private final StudentService studentService;

    @GetMapping("/students")
    @ApiOperation(value = "Get list of Students in the System ", response = Iterable.class, tags = "getStudents")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK")})
    public Iterable<Student> getStudents() {
        return studentService.findAll();
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<?> getStudent (@PathVariable Long id) {
        return studentService.findById(id)
                .map(student -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .eTag(student.getCreatedDate().toString())
                                .location(new URI("/student/" + student.getId()))
                                .body(student);
                    } catch (URISyntaxException e ) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());

    }

    @GetMapping("/course/{id}/students")
    public Iterable<Student> getStudentsByCourse (@PathVariable Long id) {
        return studentService.findByCourseId(id);
    }


    @GetMapping("/filter/students")
    public Iterable<Student> getStudentsFilter2(FilterParam model) {

        if ( model.getFilter().get("unlinked") != null && model.getFilter().get("unlinked").equals("true") )
            return studentService.findUnlinked();
        return studentService.findAll();
    }


    @PostMapping("/student")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student newStudent = studentService.save(student);
        try {
            return ResponseEntity
                    .created(new URI("/student/" + newStudent.getId()))
                    .eTag(newStudent.getCreatedDate().toString())
                    .body(newStudent);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/student/{id}")
    public ResponseEntity<Student> update(@RequestBody Student student, @PathVariable Long id) {
        Optional<Student> optional = studentService.findById(id);
        if ( optional.isPresent()){
            Student existing = optional.get();
            existing.setFirstName(student.getFirstName());
            existing.setLastName(student.getLastName());
            existing.setEmail(student.getEmail());
            existing.setPhone(student.getPhone());
            existing.setAddress(student.getAddress());
            studentService.update(existing);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<Student> register(@PathVariable Long studentId,@PathVariable Long courseId) {
        if ( studentService.register(studentId,courseId) )
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/student{id}")
    public ResponseEntity<?> studentCourse (@PathVariable Long id) {
        log.debug("Deleting student with ID {}", id);

        Optional<Student> existingStudent = studentService.findById(id);

        return existingStudent.map(s -> {
            if (studentService.delete(s.getId())) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }).orElse(ResponseEntity.notFound().build());
    }

}
