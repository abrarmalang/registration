package com.school.registration.student;

import com.school.registration.student.Student;
import com.school.registration.student.StudentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerTest {
    @MockBean
    private StudentService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/v1/student/1 - Found")
    void testGetStudentByIdFound() throws Exception {

        LocalDateTime now = LocalDateTime.now();
        Student mockStudent = new Student(1L, "Kenyon", "Baldwin", "praesent.eu@yahoo.net", "(897) 241-132", "Ap #816-7438 Nunc St", now, now);
        doReturn(Optional.of(mockStudent)).when(service).findById(1L);

        // Execute the GET request
        mockMvc.perform(get("/api/v1/student/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))

            .andExpect(header().string(HttpHeaders.ETAG, String.format("\"%s\"",now.toString())))
            .andExpect(header().string(HttpHeaders.LOCATION, "/student/1"))

            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.firstName", is("Kenyon")))
            .andExpect(jsonPath("$.createdDate", is(now.toString())))
            .andExpect(jsonPath("$.updatedDate", is(now.toString())));
    }

}
