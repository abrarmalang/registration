package com.school.registration.course;

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
public class CourseRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CourseRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;

        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("courses")
                .usingGeneratedKeyColumns("id")
                .usingColumns("course_name");
    }


    public Optional<Course> findById(Long id) {
        try {
            Course course = jdbcTemplate.queryForObject("select * from courses where id = ?",
                    new CourseRowMapper(),
                    id);
            return Optional.of(course);
        } catch (EmptyResultDataAccessException e) {
            log.debug( "Course not found {}",id);
            return Optional.empty();
        }
    }

    public List<Course> findAll() {
        return jdbcTemplate.query("select * from courses",new CourseRowMapper()
                );
    }

    public List<Course> findByStudentId(Long id){
        return jdbcTemplate.query(
       """
                   select c.* from
                    courses c,
                       course_student cs
                       where c.id = cs.course_id and cs.student_id = ?
               """
            ,new CourseRowMapper()
            ,id
        );
    }

    public int getRegisteredStudentCount(Long courseId) {
        return jdbcTemplate.queryForObject(
                """
                        select count(student_id) from course_student where course_id = ?
                        """, Integer.class , courseId
        );
    }


    public List<Course> findUnlinked(){
        return jdbcTemplate.query(
                """
                        select c.* from
                        	courses c where c.id not in (select course_id from course_student)                        
                 """
                ,new CourseRowMapper()
        );

    }


    public boolean update(Course course) {
        return jdbcTemplate.update("update courses set course_name = ? WHERE id = ?",
                course.getCourseName(),
                course.getId()) == 1;
    }




    public Course save(Course course) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("course_name", course.getCourseName());
        Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
        course = findById(newId.longValue()).get();
        return course;
    }


    public boolean delete(Long id) {
        return jdbcTemplate.update("delete from courses where id = ?", id) == 1;
    }

    static class CourseRowMapper implements RowMapper<Course> {
        @Override
        public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Course(
                    rs.getLong("id"),
                    rs.getString("course_name"),
                    rs.getTimestamp("created_date").toLocalDateTime(),
                    rs.getTimestamp("updated_date").toLocalDateTime()
            );
        }
    }


}
