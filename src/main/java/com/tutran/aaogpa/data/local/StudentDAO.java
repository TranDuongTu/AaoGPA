package com.tutran.aaogpa.data.local;

import com.tutran.aaogpa.data.models.Student;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface StudentDAO extends GenericDAO<Student> {
    Student findStudentByExactID(String stuID);
    List<Student> findStudentsByIDPrefixPattern(String idPattern);
    int findStudentsCountByIDPrefixPattern(String idPattern);
    List<Student> findStudentsByName(String name);
}
