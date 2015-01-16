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
    private Button continueLoadDataBtn, refreshDataBtn,
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
    protected void handleRefreshDataBtnAction(ActionEvent e) {
        localDataRepository.clearAllData();

        disableScene();
        List<String> facultyStrings = new ArrayList<String>(
                supportData.getSupportFaculties().keySet());
        List<String> yearStrings = supportData.getSupportYears();
        int workload = estimateWorkload(facultyStrings, yearStrings);

        Task<Void> refreshTask = createBackgroundDataCrawlingTask(
                facultyStrings, yearStrings, 0, workload);

        Thread backgroundThread = new Thread(refreshTask);
        backgroundThread.setDaemon(true);
        backgroundThread.start();

        updateDataStatus();
    }

    @FXML
    protected void handleContinueLoadDataBtnAction(ActionEvent actionEvent) {
        disableScene();
        List<String> facultyStrings = new ArrayList<String>(
                supportData.getSupportFaculties().keySet());
        List<String> yearStrings = supportData.getSupportYears();
        int workload = estimateWorkload(facultyStrings, yearStrings);

        Task<Void> updateTask = createBackgroundDataCrawlingTask(
                facultyStrings, yearStrings,
                calculateNumberOfParsedStudent(), workload);
        progressBar.progressProperty().bind(updateTask.progressProperty());

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
        refreshDataBtn.setDisable(true);
        continueLoadDataBtn.setDisable(true);

        progressBar.setVisible(true);
        if (progressBar.progressProperty().isBound())
            progressBar.progressProperty().unbind();
        progressLabel.setText("");
    }

    private void enableScene() {
        individualBtn.setDisable(false);
        courseBtn.setDisable(false);
        facultyBtn.setDisable(false);
        refreshDataBtn.setDisable(false);
        continueLoadDataBtn.setDisable(false);

        progressBar.setVisible(false);
        if (progressBar.progressProperty().isBound())
            progressBar.progressProperty().unbind();
        progressLabel.setText("");
    }

    // ========================================================================
    // PRIVATE HELPERS
    // ========================================================================

    private int calculateNumberOfParsedStudent() {
        int result = 0;
        String pattern = null, prePattern = null;
        for (String faculty : supportData.getSupportFaculties().keySet()) {
            for (String year : supportData.getSupportYears()) {
                pattern = faculty + year.substring(2);
                if (localDataRepository.countStudentsByIDPattern(
                        pattern + "%") > 0) {
                    result += MAX_STUDENT_NUMBER;
                    prePattern = pattern;
                } else {
                    pattern = null;
                    result -= MAX_STUDENT_NUMBER;
                    break;
                }
            }
            if (pattern == null) break;
        }

        if (prePattern == null) return 0;

        for (int i = MAX_STUDENT_NUMBER - 1; i >= 0; i--) {
            String stuId = prePattern + fiveDigitsInteger(i);
            if (localDataRepository.getStudentById(stuId) != null) {
                result += i + 1;
                break;
            }
        }

        return result;
    }

    private Task<Void> createBackgroundDataCrawlingTask(
            final List<String> faculties,
            final List<String> years,
            final int nPassedStudents,
            final int workload) {
        return new CrawlingTask(faculties, years, nPassedStudents, workload);
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

    // ========================================================================
    // ADDITIONAL TYPES
    // ========================================================================

    private class CrawlingTask extends Task<Void> {
        private List<String> faculties, years;
        int nPassedStudents, workload;

        public CrawlingTask(List<String> faculties, List<String> years, int nPassedStudents, int workload) {
            this.faculties = faculties;
            this.years = years;
            this.nPassedStudents = nPassedStudents;
            this.workload = workload;
        }

        @Override
        protected Void call() {
            UpdateStatus thisUpdate = trackThisUpdate();

            try {
                crawlAndUpdateData();
            } catch (Exception e) {
                rasieAlert(e);
                return null;
            } finally {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        enableScene();
                    }
                });
            }

            finishSuccessThisUpdate(thisUpdate);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    updateDataStatus();
                }
            });
            return null;
        }

        private void crawlAndUpdateData() {
            Map<String, Integer> insertedCourses =
                    localDataRepository.getCourseIDs();
            Map<String, Integer> insertedStudents =
                    localDataRepository.getStudentIDs();

            int parsedStudents = 0;
            for (String faculty : faculties) {
                for (String year : years) {
                    parsedStudents = crawlStudentsOfFacultyAndYear(
                            faculty, year,
                            insertedCourses, insertedStudents,
                            parsedStudents);
                }
            }
        }

        private void rasieAlert(final Exception e) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Crawling and Updating process");
                    alert.setHeaderText("Something wrong");
                    alert.setContentText("Something occured when updating data: "
                            + e.getMessage());
                    alert.showAndWait();
                }
            });
        }

        private UpdateStatus trackThisUpdate() {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmSS");
            UpdateStatus newStatus = new UpdateStatus();
            newStatus.setStatus(false);
            newStatus.setUpdateDate(Long.parseLong(format.format(new Date())));
            localDataRepository.insertOrUpdateStatus(newStatus);
            return newStatus;
        }

        private void finishSuccessThisUpdate(UpdateStatus thisUpdate) {
            thisUpdate.setStatus(true);
            localDataRepository.insertOrUpdateStatus(thisUpdate);
        }

        private int crawlStudentsOfFacultyAndYear(String faculty, String year,
                Map<String, Integer> insertedCourses,
                Map<String, Integer> insertedStudents,
                int parsedStudents) {

            for (int i = 0; i <= 9999; i++) {
                if (parsedStudents < nPassedStudents) {
                    parsedStudents++;
                    continue;
                }

                String nextStudent = faculty + year.substring(2)
                        + fiveDigitsInteger(i);
                ParsedResult result = webDataRepository
                        .getMarkOfStudentBlocking(nextStudent);

                if (result != null)
                    for (CourseResult cr : aggregateParsedData(result)) {
                        String stuId = cr.getStudent().getStudentId();
                        String coId = cr.getCourse().getCourseId();

                        if (insertedCourses.containsKey(coId)) {
                            int id = insertedCourses.get(coId);
                            cr.getCourse().setId(id);
                        }
                        if (insertedStudents.containsKey(stuId)) {
                            int id = insertedStudents.get(stuId);
                            cr.getStudent().setId(id);
                        }
                        localDataRepository.insertCourseResult(cr);
                        insertedCourses.put(cr.getCourse().getCourseId(),
                                cr.getCourse().getId());
                    }

                parsedStudents++;
                updateProgress(parsedStudents, workload);

                final int currentParsedStudents = parsedStudents;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        progressLabel.setText(currentParsedStudents
                                + "/" + workload + " students parsed");
                    }
                });
            }

            return parsedStudents;
        }
    }
}
