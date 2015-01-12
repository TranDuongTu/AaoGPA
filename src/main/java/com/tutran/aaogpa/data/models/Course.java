package com.tutran.aaogpa.data.models;

public class Course extends DomainObject {
    private String courseId;
    private String name;
    private int credit;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    @Override
    public String toString() {
        return String.format(
                "[ID: %s; name: %s]",
                id, name
        );
    }

    @Override
    public int hashCode() {
        return courseId.hashCode();
    }
}
