package com.tutran.aaogpa.data.models;

public class Student extends DomainObject {
    private String name;
    private String studentId;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format(
                "%s - %s",
                studentId, name
        );
    }

    @Override
    public int hashCode() {
        return studentId.hashCode();
    }

    public boolean equals(Object object) {
        // Basic checks.
        if (object == this) return true;
        if (object == null || getClass() != object.getClass()) return false;

        // Property checks.
        Student other = (Student) object;
        if (studentId == null ? other.studentId != null
                : !studentId.equals(other.studentId))
            return false;

        // All passed.
        return true;
    }
}
