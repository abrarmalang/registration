package com.school.registration.student;

import com.school.registration.student.Student;
import com.school.registration.student.StudentRepository;
import com.school.registration.student.StudentService;
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
class StudentServiceTest {

    /**
     * The service under test.
     */
    @Autowired
    private StudentService service;

    /**
     * A mock version of the StudentRepository for use in tests.
     */
    @MockBean
    private StudentRepository repository;

    @Test
    @DisplayName("Test findById Success")
    void testFindByIdSuccess() {
        // Setup mock
        LocalDateTime now = LocalDateTime.now();
        Student mockStudent = Student.builder().firstName("Nigel").lastName("Erickson").email("tortor@hotmail.edu")
                .phone("(752) 274-3655").address("Ap #240-8521 Non Rd.").build();
        doReturn(Optional.of(mockStudent)).when(repository).findById(1L);

        // Execute the service call
        Optional<Student> returnedStudent = service.findById(1L);

        // Assert the response
        Assertions.assertTrue(returnedStudent.isPresent(), "Student was not found");
        Assertions.assertSame(returnedStudent.get(), mockStudent, "Students should be the same");
    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        // Setup mock
        LocalDateTime now = LocalDateTime.now();

        doReturn(Optional.empty()).when(repository).findById(1L);

        // Execute the service call
        Optional<Student> returnedStudent = service.findById(1L);

        // Assert the response
        Assertions.assertFalse(returnedStudent.isPresent(), "Student was found, when it shouldn't be");
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        // Setup  mock
        LocalDateTime now = LocalDateTime.now();
        Student mockStudent = Student.builder().firstName("Nigel").lastName("Erickson").email("tortor@hotmail.edu")
                .phone("(752) 274-3655").address("Ap #240-8521 Non Rd.").build();

        Student mockStudent2 = Student.builder().firstName("Dale").lastName("Nieves").email("arcu.ac@google.ca")
                .phone("(752) 274-3655").address("Ap #240-8521 Non Rd.").build();


        doReturn(Arrays.asList(mockStudent, mockStudent2)).when(repository).findAll();

        // Execute the service call
        List<Student> students = service.findAll();

        Assertions.assertEquals(2, students.size(), "findAll should return 2 students");
    }

    @Test
    @DisplayName("Test save student")
    void testSave() {
        LocalDateTime now = LocalDateTime.now();
        Student mockStudent = Student.builder().firstName("Dale").lastName("Nieves").email("arcu.ac@google.ca")
                .phone("(752) 274-3655").address("Ap #240-8521 Non Rd.").build();

        doReturn(mockStudent).when(repository).save(any());

        Student returnedStudent = service.save(mockStudent);

        Assertions.assertNotNull(returnedStudent, "The saved student should not be null");
    }
}
