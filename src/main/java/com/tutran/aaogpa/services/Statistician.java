package com.tutran.aaogpa.services;

import com.tutran.aaogpa.data.models.Course;
import com.tutran.aaogpa.data.models.CourseResult;
import com.tutran.aaogpa.data.models.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Statistician {

    public static Map<Student, Double> calculateStudentGPAs(
            List<CourseResult> courseResults) {
        Map<Student, Double> result = new HashMap<Student, Double>();

        /* courseResults doesnt contain duplicate taken courses */
        Map<Student, Integer> creditPerStudent = new HashMap<Student, Integer>();
        for (CourseResult cr : courseResults) {
            Student stu = cr.getStudent();
            Course course = cr.getCourse();

            if (!result.containsKey(stu)) {
                result.put(stu, cr.getResult() * course.getCredit());
                creditPerStudent.put(stu, course.getCredit());
            } else {
                result.put(stu, result.get(stu) + cr.getResult()*course.getCredit());
                creditPerStudent.put(stu,
                        creditPerStudent.get(stu) + course.getCredit());
            }
        }

        for (Student stu : result.keySet()) {
            result.put(stu, result.get(stu) / creditPerStudent.get(stu));
        }

        return result;
    }

    public static List<List<Student>> makeHistogram(
            Map<Student, Double> students, int n) {
        List<List<Student>> result = new ArrayList<List<Student>>();
        for (int i = 0; i < n; i++) result.add(new ArrayList<Student>());

        double interval = 10.0 / n;
        for (Map.Entry<Student, Double> entry : students.entrySet()) {
            double gpa = entry.getValue();
            for (int i = 0; i < n; i++) {
                if (gpa < (i + 1)*interval) {
                    result.get(i).add(entry.getKey());
                    break;
                }
            }
        }

        return result;
    }
}
