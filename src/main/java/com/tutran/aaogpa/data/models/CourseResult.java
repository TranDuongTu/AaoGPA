package com.tutran.aaogpa.data.models;

public class CourseResult extends DomainObject {
    private Student student;
    private Course course;
    private double result;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return String.format(
                "[Student: %s; Course: %s; Result: %f]",
                student, course, result
        );
    }
}
