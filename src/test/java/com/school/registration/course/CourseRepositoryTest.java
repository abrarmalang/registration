package com.school.registration.course;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CourseRepositoryTest {


    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CourseRepository courseRepository;

    @Test
    void testFindAll(){
       List<Course> course = courseRepository.findAll();
       assertThat(course.size() ).isGreaterThan(7);
    }

    @Test
    void testFindById() {
        Course expected = jdbcTemplate.queryForObject("select * from courses order by id desc limit 1",new CourseRepository.CourseRowMapper());
        Optional<Course> actual = courseRepository.findById(expected.getId());
        assertThat(actual.isPresent());
        assertThat(actual.get().getCourseName()).isEqualTo(expected.getCourseName());
        assertThat(actual.get().getCreatedDate()).isNotNull();
        assertThat(actual.get().getUpdatedDate()).isNotNull();
    }

    @Test
    void testFindByStudentId() {
        Course expected = jdbcTemplate.queryForObject("select * from courses where course_name = 'Introduction to Algorithms'",new CourseRepository.CourseRowMapper());
        Optional<Course> actual = courseRepository.findById(expected.getId());
        assertThat(actual.isPresent());
        assertThat(actual.get().getCourseName()).isEqualTo("Introduction to Algorithms");

    }

    @Test
    void testFindByIdNotFound() {
        Optional<Course> actual = courseRepository.findById(9999L);
        assertThat(actual.isEmpty());
    }

    @Test
    void testSave() {
        Course created = Course.builder().courseName("Test Save Course").build();
        Course saved = courseRepository.save(created);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCourseName()).isEqualTo(created.getCourseName());
        assertThat(saved.getCreatedDate()).isNotNull();
    }

    @Test
    void testDeleteById() {
        Course saved = courseRepository.save(Course.builder().courseName("Test Delete Course").build());
        courseRepository.delete(saved.getId());
        Optional<Course> deleted = courseRepository.findById(saved.getId());
        assertThat(deleted.isEmpty()).isTrue();
    }
}
