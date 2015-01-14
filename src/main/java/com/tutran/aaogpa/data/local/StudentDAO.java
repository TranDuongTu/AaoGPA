package com.tutran.aaogpa.data.local;

import com.tutran.aaogpa.data.models.Student;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface StudentDAO extends GenericDAO<Student> {
    Student findStudentByID(String stuID);

    List<Student> findStudentsByIDPattern(String pattern);
    int findStudentsCountByIDPattern(String idPattern);

    List<Student> findStudentsByName(String name);
}
