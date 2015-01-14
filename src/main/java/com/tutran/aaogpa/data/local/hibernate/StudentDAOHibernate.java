package com.tutran.aaogpa.data.local.hibernate;

import com.tutran.aaogpa.data.local.StudentDAO;
import com.tutran.aaogpa.data.models.Student;
import org.hibernate.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentDAOHibernate
        extends GenericDAOHibernate<Student> implements StudentDAO {

    public StudentDAOHibernate() {
        super(Student.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Student> findStudentsByName(String name) {
        Query query = getSessionFactory().getCurrentSession()
                .createQuery("FROM " + getType().getName()
                        + " S WHERE S.name LIKE :name");
        query.setParameter("name", "%" + name + "%");
        return query.list();
    }

    @Override
    public Student findStudentByID(String stuID) {
        List result = getSessionFactory().getCurrentSession()
                .createQuery("FROM Student S WHERE S.studentId = :stuID")
                .setParameter("stuID", stuID)
                .list();
        if (result != null && result.size() == 1)
            return (Student) result.get(0);
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Student> findStudentsByIDPattern(String pattern) {
        return (List<Student>) getSessionFactory().getCurrentSession()
                .createQuery("FROM Student S WHERE S.studentId LIKE :pattern")
                .setParameter("pattern", pattern)
                .list();
    }

    @Override
    public int findStudentsCountByIDPattern(String pattern) {
        List result = getSessionFactory().getCurrentSession()
                .createQuery("SELECT count(*) FROM Student S WHERE S.studentId LIKE :pattern")
                .setParameter("pattern", pattern + "%")
                .list();
        if (result != null && result.size() == 1)
            return ((Long) result.get(0)).intValue();
        return 0;
    }
}
