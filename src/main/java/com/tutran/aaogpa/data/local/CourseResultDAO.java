package com.tutran.aaogpa.data.local;

import com.tutran.aaogpa.data.models.CourseResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CourseResultDAO extends GenericDAO<CourseResult> {
    List<CourseResult> findCourseResultsOfStudentId(String id);
    List<CourseResult> findCourseResultsOfCourseId(String id);
    List<CourseResult> findBestCourseResultsOfStudentId(String stuId, int maxResults);
    List<CourseResult> findBestCourseResultsOfCourseId(String coId, int maxResults);
}
