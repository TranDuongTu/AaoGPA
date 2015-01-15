package com.tutran.aaogpa.data;

import java.util.List;
import java.util.Map;

public class SupportData {
    private Map<String, String> supportFaculties;
    private List<String> supportYears;

    public Map<String, String> getSupportFaculties() {
        return supportFaculties;
    }

    public void setSupportFaculties(Map<String, String> supportFaculties) {
        this.supportFaculties = supportFaculties;
    }

    public List<String> getSupportYears() {
        return supportYears;
    }

    public void setSupportYears(List<String> supportYears) {
        this.supportYears = supportYears;
    }
}
