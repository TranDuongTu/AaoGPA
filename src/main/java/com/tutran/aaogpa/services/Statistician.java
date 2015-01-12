package com.tutran.aaogpa.services;

import com.tutran.aaogpa.data.models.Course;
import com.tutran.aaogpa.data.models.CourseResult;
import com.tutran.aaogpa.data.models.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Statistician {

    public static Map<Student, Double> calculateStudentGPAs(
            List<CourseResult> courseResults) {
        Map<Student, Double> result = new HashMap<Student, Double>();

        Map<Student, Integer> coursesPerStudent = new HashMap<Student, Integer>();
        for (CourseResult cr : courseResults) {
            Student stu = cr.getStudent();
            Course course = cr.getCourse();

            if (!result.containsKey(stu)) {
                result.put(stu, cr.getResult());
                coursesPerStudent.put(stu, 0);
            } else {
                result.put(stu, result.get(stu) + cr.getResult());
                coursesPerStudent.put(stu,
                        coursesPerStudent.get(stu) + course.getCredit());
            }
        }

        for (Student stu : result.keySet()) {
            result.put(stu, result.get(stu) / coursesPerStudent.get(stu));
        }

        return result;
    }
}
