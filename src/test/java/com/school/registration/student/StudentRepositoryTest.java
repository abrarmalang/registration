package com.school.registration.student;

import com.school.registration.student.Student;
import com.school.registration.student.StudentRepository;
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
public class StudentRepositoryTest {


    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StudentRepository studentRepository;

    @Test
    void testFindAll(){
       List<Student> student = studentRepository.findAll();
       assertThat(student.size() ).isGreaterThan(7);
    }

    @Test
    void testFindById() {
        Student expected = jdbcTemplate.queryForObject("select * from students order by id desc limit 1",new StudentRepository.StudentRowMapper());
        Optional<Student> actual = studentRepository.findById(expected.getId());
        assertThat(actual.isPresent());
        assertThat(actual.get().getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.get().getCreatedDate()).isNotNull();
        assertThat(actual.get().getUpdatedDate()).isNotNull();
    }

    @Test
    void testFindByStudentId() {
        Student expected = jdbcTemplate.queryForObject("select * from students where email = 'Cedrick_Bingham6417@gembat.biz'",new StudentRepository.StudentRowMapper());
        Optional<Student> actual = studentRepository.findById(expected.getId());
        assertThat(actual.isPresent());
        assertThat(actual.get().getEmail()).isEqualTo("Cedrick_Bingham6417@gembat.biz");

    }

    @Test
    void testFindByIdNotFound() {
        Optional<Student> actual = studentRepository.findById(9999L);
        assertThat(actual.isEmpty());
    }

    @Test
    void testSave() {
        Student created = Student.builder().firstName("Nigel").lastName("Erickson").email("tortor@hotmail.edu")
                .phone("(752) 274-3655").address("Ap #240-8521 Non Rd.").build();
        Student saved = studentRepository.save(created);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getFirstName()).isEqualTo(created.getFirstName());
        assertThat(saved.getCreatedDate()).isNotNull();
    }

    @Test
    void testDeleteById() {
        Student saved = studentRepository.save(Student.builder().firstName("Dale").lastName("Nieves").email("arcu.ac@google.ca")
                .phone("(752) 274-3655").address("Ap #240-8521 Non Rd.").build());
        studentRepository.delete(saved.getId());
        Optional<Student> deleted = studentRepository.findById(saved.getId());
        assertThat(deleted.isEmpty()).isTrue();
    }
}
