package com.school.registration.course;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class CourseService {

    @Autowired
    private final CourseRepository courseRepository;

    public Optional<Course> findById(Long id) {
        log.info("Find course with id: {}", id);
        return courseRepository.findById(id);
    }

    public List<Course> findAll() {
        log.info("Find all courses");
        return courseRepository.findAll();
    }

    public List<Course> findByStudentId(Long id) {
        log.info("Find all courses");
        return courseRepository.findByStudentId(id);
    }

    public List<Course> findUnlinked() {
        log.info("Find unlinked courses");
        return courseRepository.findUnlinked();
    }


    public boolean update(Course course){
        log.info("Update course: {}", course);
        return courseRepository.update(course);
    }

    Course save(Course course){
        return courseRepository.save(course);
    }

    public boolean delete(Long id) {
        log.info("Delete course with id: {}", id);
        return courseRepository.delete(id);
    }

}
