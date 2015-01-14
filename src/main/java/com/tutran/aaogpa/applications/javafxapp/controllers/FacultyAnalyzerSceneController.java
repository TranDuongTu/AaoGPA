package com.tutran.aaogpa.applications.javafxapp.controllers;

import com.sun.istack.internal.NotNull;
import com.tutran.aaogpa.applications.javafxapp.scenes.SceneID;
import com.tutran.aaogpa.data.models.CourseResult;
import com.tutran.aaogpa.data.models.Student;
import com.tutran.aaogpa.services.Statistician;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.util.*;

public class FacultyAnalyzerSceneController extends Controller {

    private static final int NUMBER_OF_BIN = 20;

    private static final String ALL_YEAR = "All";
    private static final FacultyKeyName ALL_FACULTY =
            new FacultyKeyName("0", "All");

    @FXML
    private ListView<String> yearList;

    @FXML
    private ListView<FacultyKeyName> facultyList;

    @FXML
    private Button compareBtn, backBtn;

    @FXML
    private BarChart<String, Number> compareHistogram;

    @Override
    public void startScene() {
        // Supported input options for ListViews
        facultyList.getItems().clear();
        facultyList.getItems().add(ALL_FACULTY);
        for (String facultyKey : dataScope.getFaculties().keySet()) {
            String facultyName = dataScope.getFaculties().get(facultyKey);
            facultyList.getItems().add(
                    new FacultyKeyName(facultyKey, facultyName));
        }

        yearList.getItems().clear();
        yearList.getItems().add(ALL_YEAR);
        for (String year : dataScope.getSupportYears())
            yearList.getItems().add(year);

        facultyList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        yearList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    // ========================================================================
    // EVENT HANDLERS
    // ========================================================================

    @FXML
    protected void handleCompareBtnAction(ActionEvent e) {
        List<FacultyKeyName> selectedFaculties =
                facultyList.getSelectionModel().getSelectedItems();
        List<String> selectedYears =
                yearList.getSelectionModel().getSelectedItems();
        if (selectedFaculties == null || selectedFaculties.size() == 0
                || selectedYears == null || selectedYears.size() == 0) {
            Alert alertDialog = new Alert(Alert.AlertType.WARNING);
            alertDialog.setTitle("Invalid input");
            alertDialog.setContentText("Please select at least one faculty and year");
            alertDialog.setHeaderText("Invalid faculty or year");
            alertDialog.showAndWait();
            return;
        }

        updateCompareHistogram(
                getStudentsPerSeries(selectedFaculties, selectedYears));
    }

    private Map<String, Map<Student, Double>> getStudentsPerSeries(
            List<FacultyKeyName> selectedFaculties,
            List<String> selectedYears) {
        if (selectedFaculties.contains(ALL_FACULTY)) {
            selectedFaculties.clear();
            for (String faculty : dataScope.getFaculties().keySet())
                selectedFaculties.add(new FacultyKeyName(
                        faculty, dataScope.getFaculties().get(faculty)));
        }

        Map<String, List<Student>> studentPartitions =
                new HashMap<String, List<Student>>();
        if (selectedYears.contains(ALL_YEAR)) {
            for (FacultyKeyName faculty : selectedFaculties){
                studentPartitions.put(faculty.getName(),
                        localDataRepository.getStudentsByIDPattern(
                                faculty.getKey() + "%"));
            }
        } else {
            for (String year : selectedYears) {
                for (FacultyKeyName faculty : selectedFaculties) {
                    String categoryName = faculty.getName() + " - " + year;
                    studentPartitions.put(categoryName,
                            localDataRepository.getStudentsByIDPattern(
                                    faculty.getKey() + year.substring(2) + "%"));
                }
            }
        }

        Map<String, Map<Student, Double>> result =
                new HashMap<String, Map<Student, Double>>();
        for (String category : studentPartitions.keySet()) {
            List<CourseResult> allCRs = new ArrayList<CourseResult>();
            for (Student student : studentPartitions.get(category))
                allCRs.addAll(localDataRepository.getAllCourseResultsOfStudent(student.getStudentId()));
            result.put(category, Statistician.calculateStudentGPAs(allCRs));
        }

        return result;
    }

    private void updateCompareHistogram(
            Map<String, Map<Student, Double>> studentsPerSeries) {
        if (!localDataRepository.getLastUpdateStatus()) return;
        compareHistogram.getData().clear();

        // Config axis
        Axis<String> xAxis = compareHistogram.getXAxis();
        Axis<Number> yAxis = compareHistogram.getYAxis();
        xAxis.setLabel("Score intervals");
        yAxis.setLabel("Frequency");
        yAxis.setMinHeight(0);

        // categories for x-axis
        double interval = 10.0 / NUMBER_OF_BIN;
        List<String> intervals = new ArrayList<String>();
        for (int i = 0; i < NUMBER_OF_BIN; i++) {
            intervals.add("[" + (i*interval) + ", "
                    + ((i + 1) * interval) + ")");
        }

        // create series
        for (String seriesName : studentsPerSeries.keySet()) {
            List<List<Student>> histogram = Statistician.makeHistogram(
                    studentsPerSeries.get(seriesName), NUMBER_OF_BIN);
            XYChart.Series<String, Number> series =
                    new XYChart.Series<String, Number>();
            series.setName(seriesName);
            for (int i = 0; i < NUMBER_OF_BIN; i++) {
                String category = intervals.get(i);
                Number size = histogram.get(i).size();

                series.getData().add(
                        new XYChart.Data<String, Number>(category, size));
            }
            compareHistogram.getData().add(series);
        }
    }

    @FXML
    protected void handleBackBtnAction(ActionEvent e) {
        close(SceneID.OverView);
    }

    // ========================================================================
    // ADDITIONAL TYPES
    // ========================================================================

    private static class FacultyKeyName implements Comparable<FacultyKeyName> {
        private String key, name;

        public FacultyKeyName(String key, String name) {
            this.key = key;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public int compareTo(FacultyKeyName that) {
            return key.compareTo(that.key);
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }
    }
}
