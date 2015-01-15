package com.tutran.aaogpa.data.local;

import com.tutran.aaogpa.data.models.Course;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CourseDAO extends GenericDAO<Course> {
    Course getCourseByID(String id);

    List<Course> findCoursesByIdPattern(String pattern);
    int countCoursesByIDPattern(String pattern);

    List<Course> findCoursesByName(String name);
    int countCoursesByName(String name);

    List<Course> findCoursesByCredit(int min, int max);
    int countCoursesByCredit(int min, int max);
}
