package com.tutran.aaogpa.data.local;

import com.tutran.aaogpa.data.models.Course;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CourseDAO extends GenericDAO<Course> {
    Course findCourseById(String id);

    List<Course> findCoursesByIdPattern(String pattern);
    int findCoursesCountByIDPattern(String pattern);

    List<Course> findCoursesByName(String name);

    List<Course> findCoursesByCredit(int min, int max);
}
