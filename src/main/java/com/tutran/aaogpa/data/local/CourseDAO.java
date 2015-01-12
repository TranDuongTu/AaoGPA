package com.tutran.aaogpa.data.local;

import com.tutran.aaogpa.data.models.Course;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CourseDAO extends GenericDAO<Course> {
    List<Course> findCoursesByName(String name);
    List<Course> findCoursesByIdPrefixPattern(String pattern);
    int findCoursesCountByIDPrefixPattern(String pattern);
}
