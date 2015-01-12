package com.tutran.aaogpa.data;

import java.util.List;
import java.util.Map;

public class DataScope {
    private Map<String, String> faculties;
    private List<String> supportYears;

    public Map<String, String> getFaculties() {
        return faculties;
    }

    public void setFaculties(Map<String, String> faculties) {
        this.faculties = faculties;
    }

    public List<String> getSupportYears() {
        return supportYears;
    }

    public void setSupportYears(List<String> supportYears) {
        this.supportYears = supportYears;
    }
}
