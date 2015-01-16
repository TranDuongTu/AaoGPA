package com.tutran.aaogpa.applications.javafxapp.controllers;

import com.tutran.aaogpa.applications.javafxapp.charts.ChartFactory;
import com.tutran.aaogpa.applications.javafxapp.scenes.SceneID;
import com.tutran.aaogpa.data.models.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacultyAnalyzerSceneController extends Controller {

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
        for (String facultyKey : supportData.getSupportFaculties().keySet()) {
            String facultyName = supportData.getSupportFaculties().get(facultyKey);
            facultyList.getItems().add(
                    new FacultyKeyName(facultyKey, facultyName));
        }

        yearList.getItems().clear();
        yearList.getItems().add(ALL_YEAR);
        for (String year : supportData.getSupportYears())
            yearList.getItems().add(year);

        facultyList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        yearList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    // ========================================================================
    // EVENT HANDLERS
    // ========================================================================

    @FXML
    protected void handleCompareBtnAction(ActionEvent e) {
        List<FacultyKeyName> selectedFaculties = getSelectedFaculties();
        List<String> selectedYears = getSelectedYears();
        if (selectedFaculties == null || selectedFaculties.size() == 0
                || selectedYears == null || selectedYears.size() == 0) {
            Alert alertDialog = new Alert(Alert.AlertType.WARNING);
            alertDialog.setTitle("Invalid input");
            alertDialog.setContentText("Please select at least one faculty and year");
            alertDialog.setHeaderText("Invalid faculty or year");
            alertDialog.showAndWait();
            return;
        }

        compareHistogram.getData().clear();
        ChartFactory.makeHistogram(compareHistogram,
                getStudentGPAsPerCategory(selectedFaculties, selectedYears));
    }

    private List<FacultyKeyName> getSelectedFaculties() {
        List<FacultyKeyName> result = new ArrayList<FacultyKeyName>();
        for (FacultyKeyName facultyKeyName :
                facultyList.getSelectionModel().getSelectedItems()) {
            result.add(facultyKeyName);
        }
        return result;
    }

    private List<String> getSelectedYears() {
        List<String> result = new ArrayList<String>();
        for (String year : yearList.getSelectionModel().getSelectedItems()) {
            result.add(year);
        }
        return result;
    }

    private Map<String, Map<Student, Double>> getStudentGPAsPerCategory(
            List<FacultyKeyName> selectedFaculties, List<String> selectedYears) {

        // Get final list of faculties to be analyzed
        if (selectedFaculties.contains(ALL_FACULTY)) {
            selectedFaculties.clear();
            for (String faculty : supportData.getSupportFaculties().keySet())
                selectedFaculties.add(new FacultyKeyName(
                        faculty, supportData.getSupportFaculties().get(faculty)));
        }

        // Obtain GPAs for each category
        Map<String, Map<Student, Double>> result =
                new HashMap<String, Map<Student, Double>>();
        if (selectedYears.contains(ALL_YEAR)) {
            for (FacultyKeyName faculty : selectedFaculties){
                result.put(faculty.getName(),
                        localDataRepository.getStudentsGpaByIDPattern(
                                faculty.getKey() + "%"));
            }
        } else {
            for (String year : selectedYears) {
                for (FacultyKeyName faculty : selectedFaculties) {
                    String categoryName = faculty.getName() + " - " + year;
                    result.put(categoryName,
                            localDataRepository.getStudentsGpaByIDPattern(
                                    faculty.getKey() + year.substring(2) + "%"));
                }
            }
        }

        return result;
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
