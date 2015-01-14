package com.tutran.aaogpa.data.local.hibernate;

import com.tutran.aaogpa.data.local.CourseResultDAO;
import com.tutran.aaogpa.data.models.CourseResult;
import org.hibernate.Query;

import java.util.List;

public class CourseResultDAOHibernate
        extends GenericDAOHibernate<CourseResult> implements CourseResultDAO {

    public CourseResultDAOHibernate() {
        super(CourseResult.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CourseResult> findCourseResultsOfCourseId(String coId) {
        Query query = getSessionFactory().getCurrentSession()
                .createQuery(
                        "FROM " + getType().getName()
                                + " CR WHERE CR.course.courseId = :coId"
                );
        query.setParameter("coId", coId);
        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CourseResult> findCourseResultsOfStudentId(String stuId) {
        Query query = getSessionFactory().getCurrentSession()
                .createQuery(
                        "FROM " + getType().getName()
                                + " CR WHERE CR.student.studentId = :stuId"
                );
        query.setParameter("stuId", stuId);
        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CourseResult> findBestCourseResultsOfStudentId(String stuId, int maxResults) {
        return getSessionFactory().getCurrentSession()
                .createQuery(
                        "FROM " + getType().getName()
                                + " CR WHERE CR.student.studentId = :stuId "
                                + "ORDER BY CR.result DESC")
                .setParameter("stuId", stuId)
                .setMaxResults(maxResults)
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CourseResult> findBestCourseResultsOfCourseId(String coId, int maxResults) {
        return getSessionFactory().getCurrentSession()
                .createQuery(
                        "FROM " + getType().getName()
                                + " CR WHERE CR.course.courseId = :coId "
                                + "ORDER BY CR.result DESC")
                .setParameter("coId", coId)
                .setMaxResults(maxResults)
                .list();
    }
}