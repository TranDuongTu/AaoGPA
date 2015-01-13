package com.tutran.aaogpa.applications.javafxapp.controllers;

import com.tutran.aaogpa.applications.javafxapp.scenes.SceneID;
import com.tutran.aaogpa.data.models.Course;
import com.tutran.aaogpa.data.models.CourseResult;
import com.tutran.aaogpa.data.models.Student;
import com.tutran.aaogpa.data.models.UpdateStatus;
import com.tutran.aaogpa.data.web.ParsedResult;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.*;

public class OverviewSceneController extends Controller {

    private static final int STUDENT_NUMBER_LENGTH = 5;
    private static final int MAX_STUDENT_NUMBER = 5000;

    // ========================================================================
    // UI CONTROLS
    // ========================================================================

    @FXML
    private Button loadDataBtn, clearDataBtn;

    @FXML
    private Button individualBtn, courseBtn, facultyBtn;

    @FXML
    private Label totalStudentLabel, totalCourseLabel,
            totalYearLabel, totalFacultyLabel,
            dateUpdateLabel, dataStatusLabel,
            progressLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private BarChart scoreHistogram;

    @FXML
    private ListView facultyList, yearList;

    @Override
    public void startScene() {
        enableScene();
        updateDataStatus();
    }

    // ========================================================================
    // EVENT HANDLERS
    // ========================================================================

    @FXML
    protected void handleClearDataBtnAction(ActionEvent e) {
        localDataRepository.clearAllData();
        updateDataStatus();
    }

    @FXML
    protected void handleLoadDataBtnAction(ActionEvent actionEvent) {
        disableScene();
        final List<String> facultyStrings =
                new ArrayList<String>(dataScope.getFaculties().keySet());
        final List<String> years = dataScope.getSupportYears();
        final int workload = estimateWorkload(facultyStrings, years);

        Thread backgroundThread = new Thread(createBackgroundDataCrawlingTask(
                facultyStrings, years, workload
        ));
        backgroundThread.setDaemon(true);
        backgroundThread.start();
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

    private void updateDataStatus() {
        // Total students and courses
        totalStudentLabel.setText("Total students: "
                + localDataRepository.getTotalStudents());
        totalCourseLabel.setText("Total courses: "
                + localDataRepository.getTotalCourses());

        // total faculties and years (query by id prefix pattern)
        int totalFaculties = 0, totalYears = 0;
        for (String faculty : dataScope.getFaculties().keySet()) {
            if (localDataRepository.getTotalStudentsByIDPrefixPattern(faculty) != 0)
                totalFaculties += 1;
        }
        for (String year : dataScope.getSupportYears())
            if (localDataRepository.getTotalStudentsByIDPrefixPattern(
                    "_" + year.substring(2)) > 0) {
                totalYears += 1;
            }
        totalFacultyLabel.setText("Total faculties: " + totalFaculties);
        totalYearLabel.setText("Total years: " + totalYears);

        // Data validity
        Date lastUpdatDate = localDataRepository.getLastDateUpdate();
        if (lastUpdatDate == null)
            dateUpdateLabel.setText("Data has never been updated");
        else
            dateUpdateLabel.setText("Last update date - "
                    + lastUpdatDate.toString());
        if (localDataRepository.getLastUpdateStatus()) {
            dataStatusLabel.setText("Data Status - ready to use");
            dataStatusLabel.setTextFill(Color.BLUE);
        } else {
            dataStatusLabel.setText("Data Status - incomplete");
            dataStatusLabel.setTextFill(Color.RED);
        }
    }

    private void disableScene() {
        individualBtn.setDisable(true);
        courseBtn.setDisable(true);
        facultyBtn.setDisable(true);
        clearDataBtn.setDisable(true);
        loadDataBtn.setDisable(true);

        progressBar.setVisible(true);
        if (progressBar.progressProperty().isBound())
            progressBar.progressProperty().unbind();
        progressLabel.setText("");
    }

    private void enableScene() {
        individualBtn.setDisable(false);
        courseBtn.setDisable(false);
        facultyBtn.setDisable(false);
        clearDataBtn.setDisable(false);
        loadDataBtn.setDisable(false);

        progressBar.setVisible(false);
        if (progressBar.progressProperty().isBound())
            progressBar.progressProperty().unbind();
        progressLabel.setText("");
    }

    private List<CourseResult> aggregateParsedData(ParsedResult parsedResult) {
        List<CourseResult> result = new ArrayList<CourseResult>();

        Student stu = parsedResult.getStudent();
        for (String coId : parsedResult.getTakenCourses().keySet()) {
            Course course = parsedResult.getTakenCourses().get(coId);
            Double maxScore = Collections.max(parsedResult.getTakenCoursesResult().get(coId));

            CourseResult cr = new CourseResult();
            cr.setStudent(stu);
            cr.setCourse(course);
            cr.setResult(maxScore);
            result.add(cr);
        }

        return result;
    }

    private String fiveDigitsInteger(int x) {
        StringBuilder result = new StringBuilder(String.valueOf(x));
        result.reverse();
        int length = result.length();
        for (int i = 0; i < STUDENT_NUMBER_LENGTH - length; i++)
            result.append("0");
        return result.reverse().toString();
    }

    private int estimateWorkload(List<String> faculties, List<String> years) {
        // Estimate workload
        int totalStudents = 0;
        for (String x : faculties)
            for (String y : years)
                totalStudents += MAX_STUDENT_NUMBER;
        return totalStudents;
    }

    private Task<Void> createBackgroundDataCrawlingTask(
            final List<String> faculties,
            final List<String> years,
            final int workload) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                // track this update
                UpdateStatus newStatus = new UpdateStatus();
                newStatus.setStatus(false);
                newStatus.setUpdateDate(1);
                localDataRepository.insertOrUpdateStatus(newStatus);

                // Update process
                try {
                    crawlAndUpdateData();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Crawling and Updating process");
                    alert.setHeaderText("Something wrong");
                    alert.setContentText(
                            "Something occured when updating data: "
                                    + e.getMessage());
                    alert.showAndWait();
                    return null;
                } finally {
                    // After updating, enable scene
                    enableScene();
                }

                // Update process is successful
                newStatus.setStatus(true);
                localDataRepository.insertOrUpdateStatus(newStatus);
                updateDataStatus();
                return null;
            }

            private void crawlAndUpdateData() {
                // track inserted courses (no need for student!)
                Map<String, Integer> insertedCourses = new HashMap<String, Integer>();

                // Update process
                int parsedStudents = 0;
                for (String faculty : faculties) {
                    for (String year : years) {
                        for (int i = 0; i <= 9999; i++) {
                            String nextStudent = faculty + year
                                    + fiveDigitsInteger(i);
                            ParsedResult result = webDataRepository
                                    .getMarkOfStudentBlocking(nextStudent);

                            if (result != null)
                                for (CourseResult cr : aggregateParsedData(result)) {
                                    if (insertedCourses.containsKey(cr.getCourse().getCourseId())) {
                                        int id = insertedCourses.get(cr.getCourse().getCourseId());
                                        cr.getCourse().setId(id);
                                    }
                                    localDataRepository.insertCourseResult(cr);
                                    insertedCourses.put(cr.getCourse().getCourseId(),
                                            cr.getCourse().getId());
                                }

                            parsedStudents++;
                            updateProgress(parsedStudents, workload);
                            progressLabel.setText(String.format("%d/%d students",
                                    parsedStudents, workload));
                        }
                    }
                }
            }
        };
        progressBar.progressProperty().bind(task.progressProperty());

        return task;
    }
}
