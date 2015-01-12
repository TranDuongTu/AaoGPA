package com.tutran.aaogpa.data.local;

import com.tutran.aaogpa.data.models.Course;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-context.xml"})
public class CourseDAOTest extends GenericDAOTest {

    @Test
    public void testGetCourses() {
        List<Course> courses = courseDAO.getAll();
        Assert.assertEquals(testCourses.size(), courses.size());

        Set<String> preNames = new TreeSet<String>();
        Set<Integer> preIds = new TreeSet<Integer>();
        for (Course course : testCourses) {
            preNames.add(course.getName());
            preIds.add(course.getId());
        }

        for (Course course : courses) {
            Assert.assertTrue(preNames.contains(course.getName()));
            Assert.assertTrue(preIds.contains(course.getId()));
            preNames.remove(course.getName());
            preIds.remove(course.getId());
        }
    }

    @Test
    public void testInsertNewCourse() {
        Course newCourse = new Course();
        newCourse.setId(1);
        newCourse.setName("New Course");
        courseDAO.save(newCourse);

        List<Course> retrievedCourses =
                courseDAO.findCoursesByName("New Course");
        Assert.assertTrue(retrievedCourses.size() >= 1);
        for (Course course : retrievedCourses) {
            if (course.getId() == newCourse.getId()) {
                Assert.assertTrue(
                        newCourse.getName().compareTo(course.getName()) == 0
                );
                return;
            }
        }
        Assert.assertTrue(false);
    }

    @Test
    @ExpectedException(DataIntegrityViolationException.class)
    public void testInsertCourseWithDuplicateId() {
        Course co1 = new Course();
        co1.setId(1);
        co1.setName("A");

        Course co2 = new Course();
        co2.setId(1);
        co2.setName("B");

        courseDAO.save(co1);
        courseDAO.save(co2);
    }

    @Test
    public void testGetCourseByName() {
        Course co1 = new Course();
        co1.setId(1);
        co1.setName("AAAABBBB");

        Course co2 = new Course();
        co2.setId(2);
        co2.setName("ABABABABAB");

        courseDAO.save(co1);
        courseDAO.save(co2);

        List<Course> retrievedCourse = courseDAO.findCoursesByName("AB");
        Assert.assertTrue("Retrive: " + retrievedCourse.size() + " courses",
                retrievedCourse.size() >= 2);

        int count = 0;
        for (Course course : retrievedCourse) {
            if ((course.getId() == co1.getId()
                    && course.getName().compareTo(co1.getName()) == 0)
                    || (course.getId() == co2.getId()
                    && course.getName().compareTo(co2.getName()) == 0))
                count++;
        }
        Assert.assertEquals(2, count);
    }
}
