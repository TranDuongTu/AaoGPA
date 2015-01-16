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
                "%s - %s - %s",
                courseId, name, credit
        );
    }

    @Override
    public int hashCode() {
        return courseId.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        // Basic checks.
        if (object == this) return true;
        if (object == null || getClass() != object.getClass()) return false;

        // Property checks.
        Course other = (Course) object;
        if (courseId == null ? other.courseId != null
                : !courseId.equals(other.courseId))
            return false;

        // All passed.
        return true;
    }
}
