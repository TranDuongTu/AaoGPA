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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.*;


public class CourseAnalyzerSceneController extends Controller {

    private static final int NUMBER_OF_BEST = 15;

    // ========================================================================
    // UI CONTROLS
    // ========================================================================

    @FXML
    private Button searchBtn, analyzeBtn, backBtn;

    @FXML
    private TextField idTextField, nameTextField, creditTextField;

    @FXML
    private ChoiceBox<Course> coursesChoiceBox;

    @FXML
    private ChoiceBox<SearchCriteria> criteriaChoiceBox;

    @FXML
    private ListView<String> bestStudentsList;

    @FXML
    private BarChart<String, Number> coursesHistogramChart;

    // ========================================================================
    // STARTING PROCEDURES
    // ========================================================================

    @Override
    public void startScene() {
        initSearchCriteriaChoiceBox();
    }

    private void initSearchCriteriaChoiceBox() {
        criteriaChoiceBox.getItems().clear();
        for (SearchCriteria criteria : SearchCriteria.values())
            criteriaChoiceBox.getItems().add(criteria);
        criteriaChoiceBox.getSelectionModel().selectFirst();
    }

    // ========================================================================
    // EVENT HANDLERS
    // ========================================================================

    @FXML
    protected void handleSearchBtnAction(ActionEvent e) {
        SearchCriteria inputCriteria =
                criteriaChoiceBox.getSelectionModel().getSelectedItem();

        List<Course> courses;
        if (inputCriteria == SearchCriteria.ByID) {
            String inputId = idTextField.getText();
            courses = localDataRepository.getCoursesByIDPattern(
                    "%" + inputId + "%");
        } else if (inputCriteria == SearchCriteria.ByName) {
            String inputName = nameTextField.getText();
            courses = localDataRepository.getCoursesByName(inputName);
        } else {
            int credits = Integer.parseInt(creditTextField.getText());
            courses = localDataRepository.getCoursesByCredit(credits, credits);
        }

        coursesChoiceBox.getItems().clear();
        for (Course course : courses)
            coursesChoiceBox.getItems().add(course);
        coursesChoiceBox.getSelectionModel().selectFirst();
    }

    @FXML
    protected void handleAnalyzeBtnAction(ActionEvent e) {
        Course selectedCourse = coursesChoiceBox.getValue();
        Map<Student, Double> students = findStudentsFromCourse(selectedCourse);
        Map<Student, Double> bestStudents = topBestStudents(students, NUMBER_OF_BEST);

        updateBestStudentsList(bestStudents);

        updateChart(students);
    }

    private void updateBestStudentsList(Map<Student, Double> bestStudents) {
        List<Student> flatStus = new ArrayList<Student>(bestStudents.keySet());
        flatStus.sort(new StudentComparator(bestStudents));

        bestStudentsList.getItems().clear();
        for (Student stu : flatStus) {
            bestStudentsList.getItems().add(
                    bestStudents.get(stu) + " - " + stu.toString());
        }
    }

    private void updateChart(Map<Student, Double> students) {
        Map<String, Map<Student, Double>> gpaPerCategory =
                new HashMap<String, Map<Student, Double>>();
        gpaPerCategory.put("All student", students);

        coursesHistogramChart.getData().clear();
        ChartFactory.makeHistogram(coursesHistogramChart, gpaPerCategory);
    }

    private Map<Student, Double> findStudentsFromCourse(Course course) {
        List<CourseResult> courseResults = localDataRepository
                .getAllCourseResultsOfCourse(course.getCourseId());

        Map<Student, Double> result = new HashMap<Student, Double>();
        for (CourseResult cr : courseResults) {
            result.put(cr.getStudent(), cr.getResult());
        }

        return result;
    }

    private Map<Student, Double> topBestStudents(
            final Map<Student, Double> students, int number) {
        Map<Student, Double> result = new HashMap<Student, Double>();

        List<Student> topStudents = new ArrayList<Student>(students.keySet());
        topStudents.sort(new StudentComparator(students));

        for (int i = 0; i < number && i < students.size(); i++) {
            Student stu = topStudents.get(i);
            result.put(stu, students.get(stu));
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

    private enum SearchCriteria {
        ByID("By ID"),
        ByName("By Name"),
        ByCredit("By Credit");

        private String desc;
        SearchCriteria(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return this.desc;
        }

        @Override
        public String toString() {
            return getDesc();
        }
    }

    private static class StudentComparator implements Comparator<Student> {
        private Map<Student, Double> gpas;

        public StudentComparator(Map<Student, Double> gpas) {
            this.gpas = gpas;
        }

        @Override
        public int compare(Student o1, Student o2) {
            double x = gpas.get(o1), y = gpas.get(o2);
            if (x < y) return 1;
            else if (x > y) return -1;
            else return o1.getName().compareTo(o2.getName());
        }
    }
}
