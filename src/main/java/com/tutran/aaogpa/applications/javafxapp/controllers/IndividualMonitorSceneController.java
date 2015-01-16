package com.tutran.aaogpa.applications.javafxapp.controllers;

import com.tutran.aaogpa.applications.javafxapp.charts.ChartFactory;
import com.tutran.aaogpa.applications.javafxapp.scenes.SceneID;
import com.tutran.aaogpa.data.models.Course;
import com.tutran.aaogpa.data.models.CourseResult;
import com.tutran.aaogpa.data.models.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.*;

public class IndividualMonitorSceneController extends Controller {
    @FXML
    private Button searchBtn, backBtn;

    @FXML
    private TextField stuIdTextField;

    @FXML
    private ListView<String> coursesResultList;

    @FXML
    private BarChart<String, Number> performanceChart;

    @Override
    public void startScene() {

    }

    // ========================================================================
    // EVENT HANDLERS
    // ========================================================================

    @FXML
    protected void handleBackBtnAction(ActionEvent e) {
        close(SceneID.OverView);
    }

    @FXML
    protected void handleSearchBtnAction(ActionEvent e) {
        String stuId = stuIdTextField.getText();
        Student student = localDataRepository.getStudentById(stuId);
        if (student == null) return;

        Map<Course, Double> courses = getStudentResult(student);
        updateCoursesResultList(courses);
        updatePerformanceChart(courses);
    }

    private void updateCoursesResultList(Map<Course, Double> courses) {
        coursesResultList.getItems().clear();

        /* Sorting course results */
        List<Map.Entry<Course, Double>> entries =
                new ArrayList<Map.Entry<Course, Double>>();
        for (Map.Entry<Course, Double> entry : courses.entrySet()) {
            entries.add(entry);
        }
        entries.sort(new Comparator<Map.Entry<Course, Double>>() {
            @Override
            public int compare(Map.Entry<Course, Double> o1,
                               Map.Entry<Course, Double> o2) {
                return -o1.getValue().compareTo(o2.getValue());
            }
        });

        for (Map.Entry<Course, Double> entry : entries) {
            coursesResultList.getItems().add(entry.getValue()
                    + " - " + entry.getKey().toString());
        }
    }

    private void updatePerformanceChart(Map<Course, Double> courses) {
        performanceChart.getData().clear();

        Map<String, Double> data = new HashMap<String, Double>();
        for (Course course : courses.keySet())
            data.put(course.getName(), courses.get(course));
        ChartFactory.makeSimpleBarChart(performanceChart, data);
    }

    private Map<Course, Double> getStudentResult(Student student) {
        Map<Course, Double> result = new HashMap<Course, Double>();

        List<CourseResult> courseResults = localDataRepository
                .getAllCourseResultsOfStudent(student.getStudentId());
        for (CourseResult cr : courseResults) {
            result.put(cr.getCourse(), cr.getResult());
        }

        return result;
    }
}
