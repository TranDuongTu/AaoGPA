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
                "[ID: %s; Name: %s]",
                id, name
        );
    }

    @Override
    public int hashCode() {
        return studentId.hashCode();
    }
}
