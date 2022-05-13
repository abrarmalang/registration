
   
create table students (
  id                bigint not null auto_increment primary key,
  first_name        varchar(50) not null,
  last_name         varchar(50),
  email             varchar(255) null,
  phone             varchar(255) null,
  address           varchar(255) null,
  created_date      datetime DEFAULT CURRENT_TIMESTAMP,
  updated_date      datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table courses (
  id                 bigint not null auto_increment primary key,
  course_name        varchar(255),
  created_date       datetime DEFAULT CURRENT_TIMESTAMP,
  updated_date       datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table course_student (
  course_id         bigint not null, 
  student_id        bigint not null
);

alter table course_student add constraint fk_course_student_1
  foreign key (course_id) references courses(id);

alter table course_student add constraint fk_course_student_2
  foreign key (student_id) references students(id);

