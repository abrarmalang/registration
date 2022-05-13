package com.school.registration.course;

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
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerTest {
    @MockBean
    private CourseService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/v1/course/1 - Found")
    void testGetCourseByIdFound() throws Exception {

        LocalDateTime now = LocalDateTime.now();
        Course mockCourse = new Course(1L, "Course Name", now, now);
        doReturn(Optional.of(mockCourse)).when(service).findById(1L);

        // Execute the GET request
        mockMvc.perform(get("/api/v1/course/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))

            .andExpect(header().string(HttpHeaders.ETAG, String.format("\"%s\"",now.toString())))
            .andExpect(header().string(HttpHeaders.LOCATION, "/course/1"))

            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.courseName", is("Course Name")))
            .andExpect(jsonPath("$.createdDate", is(now.toString())))
            .andExpect(jsonPath("$.updatedDate", is(now.toString())));
    }

}
