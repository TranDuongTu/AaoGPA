package com.tutran.aaogpa.data.local;

import com.tutran.aaogpa.data.models.Course;
import com.tutran.aaogpa.data.models.CourseResult;
import com.tutran.aaogpa.data.models.Student;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/test-context.xml"})
public class CourseResultDAOTest extends GenericDAOTest {

    @Test
    public void testInsertCR() {
        Student stu = new Student();
        stu.setId(1);
        stu.setName("Student");

        Course co = new Course();
        co.setId(1);
        co.setName("Course");

        CourseResult cr = new CourseResult();
        cr.setStudent(stu);
        cr.setCourse(co);
        cr.setResult(9.5);

        courseResultDAO.save(cr);

        List<CourseResult> retrievedCrs =
                courseResultDAO.findCourseResultsOfStudentId(stu.getId());
        for (CourseResult cResult : retrievedCrs) {
            if (stu.getId() == cResult.getStudent().getId()
                    && co.getId() == cResult.getCourse().getId()) {
                Assert.assertEquals(cr.getResult(), cResult.getResult(), 1e-9);
                return;
            }
        }
        Assert.assertTrue(false);
    }
}
