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
    public List<CourseResult> findCourseResultsOfCourseId(int coId) {
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
    public List<CourseResult> findCourseResultsOfStudentId(int stuId) {
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
    public List<CourseResult> findCourseResultsOfStudentInRange(
            int stuId, double minMark, double maxMark) {
        Query query = getSessionFactory().getCurrentSession()
                .createQuery(
                        "FROM " + getType().getName()
                                + " CR WHERE CR.student.studentId = :stuId "
                                + "AND CR.result >= :min "
                                + "AND CR.result <= :max"
                );
        query.setParameter("stuId", stuId);
        query.setParameter("min", minMark);
        query.setParameter("max", maxMark);
        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CourseResult> findCourseResultsOfCourseInRange(
            int coId, double minMark, double maxMark) {
        Query query = getSessionFactory().getCurrentSession()
                .createQuery(
                        "FROM " + getType().getName()
                                + " CR WHERE CR.course.courseId = :coId "
                                + "AND CR.result >= :min "
                                + "AND CR.result <= :max"
                );
        query.setParameter("coId", coId);
        query.setParameter("min", minMark);
        query.setParameter("max", maxMark);
        return query.list();
    }
}