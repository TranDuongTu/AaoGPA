package com.tutran.aaogpa.data.local;

import com.tutran.aaogpa.data.models.Course;
import com.tutran.aaogpa.data.models.CourseResult;
import com.tutran.aaogpa.data.models.Student;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class GenericDAOTest {

    @Autowired
    protected List<Student> testStudents;

    @Autowired
    protected List<Course> testCourses;

    @Autowired
    protected List<CourseResult> testCrs;

    @Autowired
    protected StudentDAO studentDAO;

    @Autowired
    protected CourseDAO courseDAO;

    @Autowired
    protected CourseResultDAO courseResultDAO;

    @Before
    public void initTestData() {
        studentDAO.deleteAll();
        courseDAO.deleteAll();
        courseResultDAO.deleteAll();

        for (CourseResult cr : testCrs) {
            courseResultDAO.saveOrUpdate(cr);
        }
    }

    @After
    public void clearTestData() {
        /* guarantee that CASCADE policy is save-update-delete */
        courseResultDAO.deleteAll();
    }
}
