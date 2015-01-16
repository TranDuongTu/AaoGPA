package com.tutran.aaogpa.applications.javafxapp.controllers;

import com.tutran.aaogpa.applications.javafxapp.charts.ChartFactory;
import com.tutran.aaogpa.applications.javafxapp.scenes.SceneID;
import com.tutran.aaogpa.data.models.Course;
import com.tutran.aaogpa.data.models.CourseResult;
import com.tutran.aaogpa.data.models.Student;
import com.tutran.aaogpa.data.models.UpdateStatus;
import com.tutran.aaogpa.data.web.ParsedResult;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.text.SimpleDateFormat;
import java.util.*;

public class OverviewSceneController extends Controller {

    private static final int STUDENT_NUMBER_LENGTH = 5;
    private static final int MAX_STUDENT_NUMBER = 10000;

    // ========================================================================
    // UI CONTROLS
    // ========================================================================

    @FXML
    private Button loadDataBtn, clearDataBtn,
            individualBtn, courseBtn, facultyBtn;

    @FXML
    private Label totalStudentLabel, totalCourseLabel,
            totalYearLabel, totalFacultyLabel,
            dateUpdateLabel, dataStatusLabel,
            progressLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private BarChart<String, Number> scoreHistogram;

    @FXML
    private ListView<String> facultyList, yearList;

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
                new ArrayList<String>(supportData.getSupportFaculties().keySet());
        final List<String> years = supportData.getSupportYears();
        final int workload = estimateWorkload(facultyStrings, years);

        Task<Void> updateTask = createBackgroundDataCrawlingTask(
                facultyStrings, years, workload
        );

        Thread backgroundThread = new Thread(updateTask);
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
    // MANAGE SCENE STATUS
    // ========================================================================

    private void updateDataStatus() {
        setTotalStudentAndCourseLabels();

        setTotalFacultyAndYearLabels();

        setListViewsForSupportedFacultyAndYears();

        setDataStatusLabel();

        updateChart();
    }

    private void setTotalStudentAndCourseLabels() {
        totalStudentLabel.setText("Total students: "
                + localDataRepository.countStudents());
        totalCourseLabel.setText("Total courses: "
                + localDataRepository.countCourses());
    }

    private void setTotalFacultyAndYearLabels() {
        int totalFaculties = 0, totalYears = 0;
        for (String faculty : supportData.getSupportFaculties().keySet()) {
            if (localDataRepository.countStudentsByIDPattern(faculty + "%") != 0)
                totalFaculties += 1;
        }
        for (String year : supportData.getSupportYears()) {
            if (localDataRepository.countStudentsByIDPattern("_" + year.substring(2) + "%") > 0)
                totalYears += 1;
        }
        totalFacultyLabel.setText("Total faculties: " + totalFaculties);
        totalYearLabel.setText("Total years: " + totalYears);
    }

    private void setListViewsForSupportedFacultyAndYears() {
        facultyList.getItems().clear();
        yearList.getItems().clear();
        for (String facultyName : supportData.getSupportFaculties().values())
            facultyList.getItems().add(0, facultyName);
        for (String year : supportData.getSupportYears())
            yearList.getItems().add(0, year);
    }

    private void setDataStatusLabel() {
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

    // ========================================================================
    // PRIVATE HELPERS
    // ========================================================================

    private Task<Void> createBackgroundDataCrawlingTask(
            final List<String> faculties,
            final List<String> years,
            final int workload) {

        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() {
                // track this update
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmSS");
                UpdateStatus newStatus = new UpdateStatus();
                newStatus.setStatus(false);
                newStatus.setUpdateDate(Long.parseLong(format.format(new Date())));
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
                    // After updating, try enabling scene
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            enableScene();
                        }
                    });
                }

                // Update process is successful
                newStatus.setStatus(true);
                localDataRepository.insertOrUpdateStatus(newStatus);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        updateDataStatus();
                    }
                });
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
                            String nextStudent = faculty + year.substring(2) + fiveDigitsInteger(i);
                            ParsedResult result = webDataRepository.getMarkOfStudentBlocking(nextStudent);

                            if (result != null)
                                for (CourseResult cr : aggregateParsedData(result)) {
                                    if (insertedCourses.containsKey(cr.getCourse().getCourseId())) {
                                        int id = insertedCourses.get(cr.getCourse().getCourseId());
                                        cr.getCourse().setId(id);
                                    }
                                    localDataRepository.insertCourseResult(cr);
                                    insertedCourses.put(cr.getCourse().getCourseId(), cr.getCourse().getId());
                                }

                            parsedStudents++;
                            updateProgress(parsedStudents, workload);

                            final int currentParsedStudents = parsedStudents;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    progressLabel.setText(currentParsedStudents + "/"
                                            + workload + " students parsed");
                                }
                            });
                        }
                    }
                }
            }
        };
        progressBar.progressProperty().bind(task.progressProperty());
        return task;
    }

    private List<CourseResult> aggregateParsedData(ParsedResult parsedResult) {
        List<CourseResult> result = new ArrayList<CourseResult>();

        Student stu = parsedResult.getStudent();
        for (Course course : parsedResult.getTakenCourses().keySet()) {
            Double maxScore = Collections.max(
                    parsedResult.getTakenCourses().get(course));

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
        int totalStudents = 0;
        for (String x : faculties)
            for (String y : years)
                totalStudents += MAX_STUDENT_NUMBER;
        return totalStudents;
    }

    private void updateChart() {
        scoreHistogram.getData().clear();

        Map<Student, Double> studentGPAs =
                localDataRepository.getStudentsGpaByIDPattern("%%");
        Map<String, Map<Student, Double>> gpaPerCategory =
                new HashMap<String, Map<Student, Double>>();
        gpaPerCategory.put("All Student", studentGPAs);

        ChartFactory.makeHistogram(scoreHistogram, gpaPerCategory);
    }
}
