package com.tutran.aaogpa.data.local.hibernate;

import com.tutran.aaogpa.data.local.CourseDAO;
import com.tutran.aaogpa.data.models.Course;
import org.hibernate.Query;

import java.util.List;

public class CourseDAOHibernate extends GenericDAOHibernate<Course>
        implements CourseDAO {

    public CourseDAOHibernate() {
        super(Course.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Course> findCoursesByName(String name) {
        Query query = getSessionFactory()
                .getCurrentSession()
                .createQuery("FROM " + getType().getName()
                        + " C WHERE C.name LIKE :name");
        query.setParameter("name", "%" + name + "%");
        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Course> findCoursesByIdPrefixPattern(String pattern) {
        return getSessionFactory().getCurrentSession()
                .createQuery("FROM Course C WHERE C.courseId LIKE :pattern")
                .setParameter("pattern", pattern + "%")
                .list();
    }

    @Override
    public int findCoursesCountByIDPrefixPattern(String pattern) {
        List list = getSessionFactory().getCurrentSession()
                .createQuery("SELECT count(*) FROM Course C WHERE C.courseId LIKE :pattern")
                .setParameter("pattern", pattern + "%")
                .list();
        if (list != null && list.size() == 1)
            return ((Long) list.get(0)).intValue();
        return 0;
    }
}