package com.tutran.aaogpa.data.local.hibernate;

import com.tutran.aaogpa.data.local.StudentDAO;
import com.tutran.aaogpa.data.models.Student;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentDAOHibernate
        extends GenericDAOHibernate<Student> implements StudentDAO {

    public StudentDAOHibernate() {
        super(Student.class);
    }

    @Override
    public Student getStudentByID(String stuID) {
        List result = getSessionFactory().getCurrentSession()
                .createQuery("FROM " + getType().getName()
                        + " S WHERE S.studentId = :stuID")
                .setParameter("stuID", stuID)
                .list();
        return result != null && result.size() == 1 ?
                (Student) result.get(0) : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Student> findStudentsByIDPattern(String pattern) {
        return (List<Student>) getSessionFactory().getCurrentSession()
                .createQuery("FROM " + getType().getName()
                        + " S WHERE S.studentId LIKE :pattern")
                .setParameter("pattern", pattern)
                .list();
    }

    @Override
    public int countStudentsByIDPattern(String pattern) {
        List result = getSessionFactory().getCurrentSession()
                .createQuery("SELECT count(*) FROM " + getType().getName()
                        + " S " + "WHERE S.studentId LIKE :pattern")
                .setParameter("pattern", pattern)
                .list();
        return result != null && result.size() == 1 ?
            ((Long) result.get(0)).intValue() : 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Student> findStudentsByName(String name) {
        return getSessionFactory().getCurrentSession()
                .createQuery("FROM " + getType().getName()
                        + " S " + "WHERE S.name LIKE :name")
                .setParameter("name", "%" + name + "%")
                .list();
    }

    @Override
    public int countStudentsByName(String name) {
        List result = getSessionFactory().getCurrentSession()
                .createQuery("SELECT count(*) FROM " + getType().getName() + " S "
                        + "WHERE S.name LIKE :name")
                .setParameter("name", "%" + name + "%")
                .list();
        return result != null && result.size() == 1 ?
                ((Long) result.get(0)).intValue() : 0;
    }
}
