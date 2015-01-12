package com.tutran.aaogpa.data.web;

import com.tutran.aaogpa.data.models.Course;
import com.tutran.aaogpa.data.models.Student;

import java.util.List;
import java.util.Map;

public class ParsedResult {
    private Student student;
    private Map<Integer, Course> takenCourses;
    private Map<Integer, List<Double>> takenCoursesResult;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Map<Integer, Course> getTakenCourses() {
        return takenCourses;
    }

    public void setTakenCourses(Map<Integer, Course> takenCourses) {
        this.takenCourses = takenCourses;
    }

    public Map<Integer, List<Double>> getTakenCoursesResult() {
        return takenCoursesResult;
    }

    public void setTakenCoursesResult(Map<Integer, List<Double>> takenCoursesResult) {
        this.takenCoursesResult = takenCoursesResult;
    }
}
