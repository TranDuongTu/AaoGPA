package com.tutran.aaogpa.applications.javafxapp.scenes;

public enum SceneLayout {
    Overview("src/main/resources/javafx/overview_scene.fxml"),
    IndividualMonitor("src/main/resources/javafx/individual_monitor_scene.fxml"),
    CourseAnalyzer("src/main/resources/javafx/course_analyzer_scene.fxml"),
    FacultyAnalyzer("src/main/resources/javafx/faculty_analyzer_scene.fxml");

    private String desc;
    SceneLayout(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }
}
