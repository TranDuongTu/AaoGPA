package com.tutran.aaogpa.data.web;

import com.tutran.aaogpa.data.models.Course;
import com.tutran.aaogpa.data.models.Student;

import java.util.List;
import java.util.Map;

public class ParsedResult {
    private Student student;
    private Map<Course, List<Double>> takenCourses;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Map<Course, List<Double>> getTakenCourses() {
        return takenCourses;
    }

    public void setTakenCourses(Map<Course, List<Double>> takenCourses) {
        this.takenCourses = takenCourses;
    }
}
