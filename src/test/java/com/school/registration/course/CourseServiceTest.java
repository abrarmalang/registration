package com.school.registration.course;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CourseServiceTest {

    /**
     * The service under test.
     */
    @Autowired
    private CourseService service;

    /**
     * A mock version of the CourseRepository for use in tests.
     */
    @MockBean
    private CourseRepository repository;

    @Test
    @DisplayName("Test findById Success")
    void testFindByIdSuccess() {
        // Setup mock
        LocalDateTime now = LocalDateTime.now();
        Course mockCourse = new Course(1L, "Course Name", now, now);
        doReturn(Optional.of(mockCourse)).when(repository).findById(1L);

        // Execute the service call
        Optional<Course> returnedCourse = service.findById(1L);

        // Assert the response
        Assertions.assertTrue(returnedCourse.isPresent(), "Course was not found");
        Assertions.assertSame(returnedCourse.get(), mockCourse, "Courses should be the same");
    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        // Setup mock
        LocalDateTime now = LocalDateTime.now();
        Course mockCourse = new Course(1L, "Course Name", now, now);
        doReturn(Optional.empty()).when(repository).findById(1L);

        // Execute the service call
        Optional<Course> returnedCourse = service.findById(1L);

        // Assert the response
        Assertions.assertFalse(returnedCourse.isPresent(), "Course was found, when it shouldn't be");
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        // Setup  mock
        LocalDateTime now = LocalDateTime.now();
        Course mockCourse = new Course(1L, "Course Name", now, now);
        Course mockCourse2 = new Course(2L, "Course Name 2", now, now);
        doReturn(Arrays.asList(mockCourse, mockCourse2)).when(repository).findAll();

        // Execute the service call
        List<Course> courses = service.findAll();

        Assertions.assertEquals(2, courses.size(), "findAll should return 2 courses");
    }

    @Test
    @DisplayName("Test save course")
    void testSave() {
        LocalDateTime now = LocalDateTime.now();
        Course mockCourse = new Course(1L, "Course Name", now, now);
        doReturn(mockCourse).when(repository).save(any());

        Course returnedCourse = service.save(mockCourse);

        Assertions.assertNotNull(returnedCourse, "The saved course should not be null");
    }
}
