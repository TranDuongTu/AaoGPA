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

    // ========================================================================
    // GETTERs AND SETTERs
    // ========================================================================

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
    // DATA ACCESS METHODS
    // ========================================================================

    public void clearAllData() {
        courseResultDAO.deleteAll();
        courseDAO.deleteAll();
        studentDAO.deleteAll();
        updateStatusDAO.deleteAll();
    }

    /**
     * For Students data
     */

    public void insertStudent(Student student) {
        studentDAO.save(student);
    }

    public int getStudentsCount() {
        return studentDAO.size();
    }

    public int getStudentsCountByIDPattern(String pattern) {
        if (pattern == null) pattern = "";
        return studentDAO.findStudentsCountByIDPattern(pattern);
    }

    public List<Student> getStudentsByIDPattern(String pattern) {
        if (pattern == null) pattern = "";
        return studentDAO.findStudentsByIDPattern(pattern);
    }

    public Student getStudentById(String stuId) {
        if (stuId == null) stuId = "";
        return studentDAO.findStudentByID(stuId);
    }

    public List<Student> getStudentsByName(String name) {
        if (name == null) name = "";
        return studentDAO.findStudentsByName(name);
    }

    /**
     * For Course data
     */

    public void insertCourse(Course course) {
        courseDAO.save(course);
    }

    public int getCoursesCount() {
        return courseDAO.size();
    }

    public int getCoursesCountByIDPattern(String pattern) {
        return courseDAO.findCoursesCountByIDPattern(pattern);
    }


    public List<Course> getCoursesByName(String name) {
        if (name == null) name = "";
        return courseDAO.findCoursesByName(name);
    }

    public List<Course> getCoursesByIDPattern(String pattern) {
        if (pattern == null) pattern = "";
        return courseDAO.findCoursesByIdPattern(pattern);
    }

    public List<Course> getCoursesByCredit(int min, int max) {
        return courseDAO.findCoursesByCredit(min, max);
    }

    /**
     * For CourseResult data
     */

    public void insertCourseResult(CourseResult courseResult) {
        if (courseResult.getResult() > 10) courseResult.setResult(0);
        courseResultDAO.save(courseResult);
    }

    public List<CourseResult> getAllCourseResults() {
        return courseResultDAO.getAll();
    }

    public List<CourseResult> getBestCourseResultsOfCourse(String coId, int maxResults) {
        if (coId == null) coId = "";
        return courseResultDAO.findBestCourseResultsOfCourseId(coId, maxResults);
    }

    public List<CourseResult> getAllCourseResultsOfCourse(String coId) {
        if (coId == null) coId = "";
        return courseResultDAO.findCourseResultsOfCourseId(coId);
    }

    public List<CourseResult> getAllCourseResultsOfStudent(String stuId) {
        if (stuId == null) stuId = "";
        return courseResultDAO.findCourseResultsOfStudentId(stuId);
    }

    /**
     * Data Status
     */

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
