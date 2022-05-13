package com.school.registration.student;

import com.school.registration.course.CourseRepository;
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
public class StudentService {

    @Autowired
    private final StudentRepository studentRepository;

    @Autowired
    private final CourseRepository courseRepository;

    public Optional<Student> findById(Long id) {
        log.info("Find student with id: {}", id);
        return studentRepository.findById(id);
    }

    public List<Student> findAll() {
        log.info("Find all students");
        return studentRepository.findAll();
    }

    public List<Student> findByCourseId(Long id) {
        log.info("Find all students");
        return studentRepository.findByCourseId(id);
    }

    public List<Student> findUnlinked() {
        log.info("Find students with no courses");
        return studentRepository.findUnlinked();
    }


    public boolean update(Student student){
        log.info("Update student: {}", student);
        return studentRepository.update(student);
    }

    Student save(Student student){
        return studentRepository.save(student);
    }

    public boolean delete(Long id) {
        log.info("Delete student with id: {}", id);
        return studentRepository.delete(id);
    }

    public boolean register(Long studentId, Long courseId) {
        if ( studentRepository.getRegisteredCourseCount(studentId) < 5
            && courseRepository.getRegisteredStudentCount(courseId) < 50){
            return studentRepository.register(courseId,studentId);
        }
        return false;
    }
}
