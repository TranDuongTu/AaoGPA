package com.tutran.aaogpa.applications.javafxapp.controllers;

import com.tutran.aaogpa.applications.javafxapp.scenes.SceneID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class OverviewSceneController extends Controller {
    @FXML
    private Button dataLoadBtn;

    @FXML
    private Button individualBtn;

    @FXML
    private Button courseBtn;

    @FXML
    private Button facultyBtn;

    @FXML
    private ChoiceBox<KeyValuePair> facultyChoice;

    @FXML
    private ChoiceBox<String> yearChoice;

    @FXML
    private Label totalStudentLabel, totalCourseLabel,
            totalYearLabel, totalFacultyLabel,
            dateUpdateLabel, dataStatusLabel;

    @FXML
    private ProgressBar progressBar;

    @Override
    public void startScene() {
        // Faculty choice box
        for (String facultyID : dataScope.getFaculties().keySet()) {
            String facultyName = dataScope.getFaculties().get(facultyID);
            facultyChoice.getItems().add(
                    new KeyValuePair(facultyID, facultyName));
        }
        facultyChoice.getSelectionModel().selectFirst();

        // Years choice box
        for (String year : dataScope.getSupportYears())
            yearChoice.getItems().add(year);
        yearChoice.getSelectionModel().selectFirst();

        // Basic statistic information
        totalStudentLabel.setText("Total students: " + localDataRepository.getTotalStudents());
        totalCourseLabel.setText("Total courses: " + localDataRepository.getTotalCourses());

        int totalFaculties = 0, totalYears = 0;
        for (String faculty : dataScope.getFaculties().keySet()) {
            if (faculty.compareTo("0") != 0
                && localDataRepository.getTotalStudentsByIDPrefixPattern(faculty) != 0)
                totalFaculties += 1;
        }
        for (String year : dataScope.getSupportYears())
            if (year.length() == 4 && localDataRepository
                    .getTotalStudentsByIDPrefixPattern("_" + year.substring(2)) > 0)
                totalYears += 1;
        totalFacultyLabel.setText("Total faculties: " + totalFaculties);
        totalYearLabel.setText("Total years: " + totalYears);

        // Data status
        Date lastUpdatDate = localDataRepository.getLastDateUpdate();
        if (lastUpdatDate == null)
            dateUpdateLabel.setText("Never update data");
        else
            dateUpdateLabel.setText("Last update date - "
                    + lastUpdatDate.toString());
        if (localDataRepository.getLastUpdateStatus()) {
            dataStatusLabel.setText("Valid Data Status");
            dataStatusLabel.setTextFill(Color.BLUE);
        } else {
            dataStatusLabel.setText("Invalid Data Status");
            dataStatusLabel.setTextFill(Color.RED);
        }

    }

    // ========================================================================
    // EVENT HANDLERS
    // ========================================================================

    @FXML
    protected void handleDataLoadBtnAction(ActionEvent actionEvent) {
        disableScene();
        final CountDownLatch latch = new CountDownLatch(1);
        final List<String> facultyStrings = new ArrayList<String>();
        final List<String> years = new ArrayList<String>();

        // Process input criteria
        String selectdFaculty = facultyChoice
                .getSelectionModel().getSelectedItem().getKey();
        String selectedYear = yearChoice
                .getSelectionModel().getSelectedItem();
        if (selectdFaculty.compareTo("0") == 0)
            for (String fac : dataScope.getFaculties().keySet())
                facultyStrings.add(fac);
        else
            facultyStrings.add(selectdFaculty);
        if (selectedYear.length() == 4)
            years.add(selectedYear.substring(2));
        else
            for (String year : dataScope.getSupportYears())
                years.add(year.substring(2));

        // Estimate workload
        int totalStudents = 0;
        for (String x : facultyStrings)
            for (String y : years)
                totalStudents += 10000;

        // Start crawling the web
        new Thread(new Runnable() {
            @Override
            public void run() {
                int parsedStudents = 0;
                for (String faculty : facultyStrings) {
                    for (String year : years) {
                        for (int i = 0; i <= 9999; i++) {

                        }
                    }
                }

                latch.countDown();
            }
        });

        try {
            latch.wait();
        } catch (InterruptedException e) {
            // TODO: Show message box
            enableScene();
        }
    }

    @FXML
    protected void handleIndividualBtnAction(ActionEvent actionEvent) {
        close(SceneID.IndividualMonitor);
    }

    @FXML
    protected void handleCourseBtnAction(ActionEvent e) {
        close(SceneID.CourseAnalyzer);
    }

    @FXML
    protected void handleFacultyBtnAction(ActionEvent e) {
        close(SceneID.FacultyAnalyzer);
    }

    // ========================================================================
    // ADDITION TYPES
    // ========================================================================

    private class KeyValuePair extends Pair<String, String> {
        public KeyValuePair(String key, String value) {
            super(key, value);
        }

        @Override
        public String toString() {
            return getValue();
        }
    }

    // ========================================================================
    // PRIVATE HELPERS
    // ========================================================================

    private void disableScene() {

    }

    private void enableScene() {

    }
}
