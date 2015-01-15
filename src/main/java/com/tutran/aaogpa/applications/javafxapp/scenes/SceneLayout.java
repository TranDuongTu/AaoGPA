package com.tutran.aaogpa.applications.javafxapp.scenes;

public enum SceneLayout {
    Overview("src/main/resources/ui/javafx/layouts/overview_scene.fxml"),
    IndividualMonitor("src/main/resources/ui/javafx/layouts/individual_monitor_scene.fxml"),
    CourseAnalyzer("src/main/resources/ui/javafx/layouts/course_analyzer_scene.fxml"),
    FacultyAnalyzer("src/main/resources/ui/javafx/layouts/faculty_analyzer_scene.fxml");

    private String desc;
    SceneLayout(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }
}
