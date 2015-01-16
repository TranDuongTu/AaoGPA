package com.tutran.aaogpa.services;

import com.tutran.aaogpa.data.models.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Statistician {

    public static List<List<Student>> makeHistogramBins(
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
