package com.school.registration.student;

import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;


}
