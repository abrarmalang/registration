package com.school.registration.course;

import com.school.registration.util.FilterParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class CourseController {

    @Autowired
    private final CourseService courseService;

    @GetMapping("/courses")
    @Operation(summary = "Get list of Courses in the System ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK")})

    public Iterable<Course> getCourses() {
        return courseService.findAll();
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<?> getCourse (@PathVariable Long id) {
        return courseService.findById(id)
                .map(course -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .eTag(course.getCreatedDate().toString())
                                .location(new URI("/course/" + course.getId()))
                                .body(course);
                    } catch (URISyntaxException e ) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());

    }

    @GetMapping("/student/{id}/courses")
    public Iterable<Course> getCourseByStudent (@PathVariable Long id) {
        return courseService.findByStudentId(id);
    }


    @GetMapping("/filter/courses")
    @Operation
    public Iterable<Course> getCoursesFilter2(@Parameter(name = "filter[unlinked]", in = ParameterIn.QUERY , schema = @Schema(
            type = "string",
            allowableValues = "true",
            description = "Gets courses without any students"))FilterParam model) {

        if ( model.getFilter().get("unlinked") != null && model.getFilter().get("unlinked").equals("true") )
            return courseService.findUnlinked();
        return courseService.findAll();
    }


    @PostMapping("/course")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course newCourse = courseService.save(course);
        try {
            return ResponseEntity
                    .created(new URI("/course/" + newCourse.getId()))
                    .eTag(newCourse.getCreatedDate().toString())
                    .body(newCourse);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/course/{id}")
    public ResponseEntity<Course> update(@RequestBody Course course, @PathVariable Long id) {
        Optional<Course> optional = courseService.findById(id);
        if ( optional.isPresent()){
            Course existing = optional.get();
            existing.setCourseName(course.getCourseName());
            courseService.update(existing);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/course{id}")
    public ResponseEntity<?> deleteCourse (@PathVariable Long id) {
        log.debug("Deleting course with ID {}", id);

        Optional<Course> existingProduct = courseService.findById(id);

        return existingProduct.map(c -> {
            if (courseService.delete(c.getId())) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }).orElse(ResponseEntity.notFound().build());
    }
}
