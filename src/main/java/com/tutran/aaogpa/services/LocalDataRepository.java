package com.tutran.aaogpa.services;

import com.tutran.aaogpa.data.DataScope;
import com.tutran.aaogpa.data.local.CourseDAO;
import com.tutran.aaogpa.data.local.CourseResultDAO;
import com.tutran.aaogpa.data.local.StudentDAO;
import com.tutran.aaogpa.data.local.UpdateStatusDAO;
import com.tutran.aaogpa.data.models.Course;
import com.tutran.aaogpa.data.models.CourseResult;
import com.tutran.aaogpa.data.models.Student;
import com.tutran.aaogpa.data.models.UpdateStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Repository for accessing services from local database
 */
public class LocalDataRepository {
    private DataScope dataScope;
    private StudentDAO studentDAO;
    private CourseDAO courseDAO;
    private CourseResultDAO courseResultDAO;
    private UpdateStatusDAO updateStatusDAO;

    public StudentDAO getStudentDAO() {
        return studentDAO;
    }

    @Autowired
    public void setStudentDAO(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    public CourseDAO getCourseDAO() {
        return courseDAO;
    }

    @Autowired
    public void setCourseDAO(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    public CourseResultDAO getCourseResultDAO() {
        return courseResultDAO;
    }

    @Autowired
    public void setCourseResultDAO(CourseResultDAO courseResultDAO) {
        this.courseResultDAO = courseResultDAO;
    }

    public UpdateStatusDAO getUpdateStatusDAO() {
        return updateStatusDAO;
    }

    @Autowired
    public void setUpdateStatusDAO(UpdateStatusDAO updateStatusDAO) {
        this.updateStatusDAO = updateStatusDAO;
    }

    public DataScope getDataScope() {
        return dataScope;
    }

    @Autowired
    public void setDataScope(DataScope dataScope) {
        this.dataScope = dataScope;
    }

    // ========================================================================
    // EXPOSED API
    // ========================================================================

    public void clearAllData() {
        courseResultDAO.deleteAll();
        courseDAO.deleteAll();
        studentDAO.deleteAll();
    }

    public int getTotalStudents() {
        return studentDAO.size();
    }

    public int getTotalStudentsByIDPrefixPattern(String pattern) {
        return studentDAO.findStudentsCountByIDPrefixPattern(pattern);
    }

    public List<Student> getStudentsFromFacultyAndYear(
            String facultyString, String year) {
        if (facultyString == null) facultyString = "";
        if (year == null) year = "";

        String pattern = facultyString + year.substring(2);
        return studentDAO.findStudentsByIDPrefixPattern(pattern);
    }

    public Student getStudentById(String stuId) {
        if (stuId == null) stuId = "";
        return studentDAO.findStudentByExactID(stuId);
    }

    public List<Student> getStudentsByName(String name) {
        if (name == null) name = "";
        return studentDAO.findStudentsByName(name);
    }

    public int getTotalCourses() {
        return courseDAO.size();
    }

    public int getTotalCoursesByIDPrefixPattern(String pattern) {
        return courseDAO.findCoursesCountByIDPrefixPattern(pattern);
    }

    public List<Course> getCoursesByName(String name) {
        if (name == null) name = "";
        return courseDAO.findCoursesByName(name);
    }

    public List<Course> getCoursesFromFaculty(String facultyString) {
        if (facultyString == null) facultyString = "";
        return courseDAO.findCoursesByIdPrefixPattern(facultyString);
    }

    public List<CourseResult> getAllCourseResults() {
        return courseResultDAO.getAll();
    }

    public void insertCourse(Course course) {
        courseDAO.save(course);
    }

    public void insertStudent(Student student) {
        studentDAO.save(student);
    }

    public void insertCourseResult(CourseResult courseResult) {
        courseResultDAO.save(courseResult);
    }

    public Date getLastDateUpdate() {
        SimpleDateFormat formatString = new SimpleDateFormat("yyyyMMddHHmmSS");
        UpdateStatus lastUpdate = updateStatusDAO.getLastUpdate();
        if (lastUpdate == null) return null;
        try {
            return formatString.parse(
                    String.valueOf(lastUpdate.getUpdateDate()));
        } catch (ParseException e) {
            return null;
        }
    }

    public boolean getLastUpdateStatus() {
        UpdateStatus lastUpdate = updateStatusDAO.getLastUpdate();
        return lastUpdate != null && lastUpdate.getStatus();
    }

    public void insertOrUpdateStatus(UpdateStatus updateStatus) {
        updateStatusDAO.save(updateStatus);
    }
}
