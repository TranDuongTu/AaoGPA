package com.tutran.aaogpa.data.local;

import com.tutran.aaogpa.data.models.Student;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface StudentDAO extends GenericDAO<Student> {
    Student getStudentByID(String stuID);
    Map<String, Integer> getStudentIDs();

    List<Student> findStudentsByIDPattern(String pattern);
    int countStudentsByIDPattern(String pattern);

    List<Student> findStudentsByName(String name);
    int countStudentsByName(String name);
}
