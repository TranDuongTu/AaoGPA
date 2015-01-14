package com.tutran.aaogpa.data.web;

import com.tutran.aaogpa.data.models.Course;
import com.tutran.aaogpa.data.models.Student;

import java.util.List;
import java.util.Map;

public class ParsedResult {
    private Student student;
    private Map<String, Course> takenCourses;
    private Map<String, List<Double>> takenCoursesResult;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Map<String, Course> getTakenCourses() {
        return takenCourses;
    }

    public void setTakenCourses(Map<String, Course> takenCourses) {
        this.takenCourses = takenCourses;
    }

    public Map<String, List<Double>> getTakenCoursesResult() {
        return takenCoursesResult;
    }

    public void setTakenCoursesResult(Map<String, List<Double>> takenCoursesResult) {
        this.takenCoursesResult = takenCoursesResult;
    }
}
