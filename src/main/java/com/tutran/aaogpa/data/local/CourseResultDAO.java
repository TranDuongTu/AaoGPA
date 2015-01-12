package com.tutran.aaogpa.data.local;

import com.tutran.aaogpa.data.models.CourseResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CourseResultDAO extends GenericDAO<CourseResult> {
    List<CourseResult> findCourseResultsOfStudentId(int id);
    List<CourseResult> findCourseResultsOfCourseId(int id);
    List<CourseResult> findCourseResultsOfStudentInRange(int stuId, double minMark, double maxMark);
    List<CourseResult> findCourseResultsOfCourseInRange(int coId, double minMark, double maxMark);
}
