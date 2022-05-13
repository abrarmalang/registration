package com.school.registration.course;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    private Long id;

    private String courseName;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;


}
