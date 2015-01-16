package com.tutran.aaogpa.data.local.hibernate;

import com.tutran.aaogpa.data.local.CourseDAO;
import com.tutran.aaogpa.data.models.Course;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseDAOHibernate extends GenericDAOHibernate<Course>
        implements CourseDAO {

    public CourseDAOHibernate() {
        super(Course.class);
    }

    @Override
    public Course getCourseByID(String id) {
        List result = getSessionFactory().getCurrentSession()
                .createQuery("FROM " + getType().getName()
                        + " C WHERE C.courseId = :id")
                .setParameter("id", id)
                .list();
        return result != null && result.size() == 1 ?
            (Course) result.get(0) : null;
    }

    @Override
    public Map<String, Integer> getCourseIDs() {
        List<Course> courses = getAll();
        Map<String, Integer> result = new HashMap<String, Integer>();
        for (Course course : courses)
            result.put(course.getCourseId(), course.getId());
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Course> findCoursesByIdPattern(String pattern) {
        return getSessionFactory().getCurrentSession()
                .createQuery("FROM " + getType().getName()
                        + " C WHERE C.courseId LIKE :pattern")
                .setParameter("pattern", pattern)
                .list();
    }

    @Override
    public int countCoursesByIDPattern(String pattern) {
        List result = getSessionFactory().getCurrentSession()
                .createQuery("SELECT count(*) FROM " + getType().getName()
                        + " C " + "WHERE C.courseId LIKE :pattern")
                .setParameter("pattern", pattern)
                .list();
        return result != null && result.size() == 1 ?
            ((Long) result.get(0)).intValue() : 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Course> findCoursesByName(String name) {
        return getSessionFactory().getCurrentSession()
                .createQuery("FROM " + getType().getName()
                        + " C WHERE C.name LIKE :name")
                .setParameter("name", "%" + name + "%")
                .list();
    }

    @Override
    public int countCoursesByName(String name) {
        List result = getSessionFactory().getCurrentSession()
                .createQuery("SELECT count(*) FROM " + getType().getName()
                        + " C " + "WHERE C.name LIKE :name")
                .setParameter("name", "%" + name + "%")
                .list();
        return result != null && result.size() == 1 ?
                ((Long) result.get(0)).intValue() : 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Course> findCoursesByCredit(int min, int max) {
        return getSessionFactory().getCurrentSession()
                .createQuery("FROM " + getType().getName() + " C "
                        + "WHERE C.credit >= :min AND C.credit <= :max")
                .list();
    }

    @Override
    public int countCoursesByCredit(int min, int max) {
        List result = getSessionFactory().getCurrentSession()
                .createQuery("SELECT count(*) FROM " + getType().getName() + " C "
                        + "WHERE C.credit >= :min AND C.credit <= :max")
                .list();
        return result != null && result.size() == 1 ?
                ((Long) result.get(0)).intValue() : 0;
    }
}