package com.school.registration.student;


import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class StudentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public StudentRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;

        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("students")
            .usingGeneratedKeyColumns("id")
            .usingColumns("first_name","last_name","email","phone","address");
    }


    public Optional<Student> findById(Long id) {
        try {
            Student student = jdbcTemplate.queryForObject("select * from students where id = ?",
                    new StudentRowMapper(),
                    id);
            return Optional.of(student);
        } catch (EmptyResultDataAccessException e) {
            log.debug( "Student not found {}",id);
            return Optional.empty();
        }
    }

    public List<Student> findAll() {
        return jdbcTemplate.query("select * from students",new StudentRowMapper()
                );
    }

    public List<Student> findByCourseId(Long id){
        return jdbcTemplate.query(
       """
               select s.* from
                students s,
                   course_student cs
                   where s.id = cs.student_id and cs.course_id = ?
               """
            ,new StudentRowMapper()
            ,id
        );
    }

    public List<Student> findUnlinked(){
        return jdbcTemplate.query(
                """
                        select s.* from
                        	students s where s.id not in (select student_id from course_student)                        
                 """
                ,new StudentRowMapper()
        );
    }


    public boolean update(Student student) {
        return jdbcTemplate.update("""
                update students set first_name = ? , last_name = ?
                 , email = ? , phone = ? , address = ? WHERE id = ?
            """,
                student.getFirstName(), student.getLastName()
                , student.getEmail(), student.getPhone(), student.getAddress()
                , student.getId()) == 1;
    }


    public boolean register(Long courseId, Long studentId){
        return jdbcTemplate.update(
                """
                    insert into  course_student (course_id,student_id)
                        values ( ? , ? )    
                        
                """ , courseId, studentId ) == 1;
    }




    public Student save(Student student) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("first_name", student.getFirstName());
        parameters.put("last_name", student.getLastName());
        parameters.put("email" , student.getEmail());
        parameters.put("phone", student.getPhone());
        parameters.put("address", student.getAddress());
        Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
        student = findById(newId.longValue()).get();
        return student;
    }


    public boolean delete(Long id) {
        return jdbcTemplate.update("delete from students where id = ?", id) == 1;
    }

    public int getRegisteredCourseCount(Long studentId) {
        return jdbcTemplate.queryForObject(
    """
            select count(course_id) from course_student where student_id = ?
            """, Integer.class , studentId
        );
    }

    static class StudentRowMapper implements RowMapper<Student> {
        @Override
        public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Student(
                    rs.getLong("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getTimestamp("created_date").toLocalDateTime(),
                    rs.getTimestamp("updated_date").toLocalDateTime()
            );
        }
    }


}
