package com.tutran.aaogpa.services;

import com.tutran.aaogpa.data.web.ParsedResult;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/test-context.xml"})
public class WebDataRepositoryTest {

    @Autowired
    private WebDataRepository webDataRepository;

    @Test
    public void shouldParseMyId() {
        ParsedResult result = webDataRepository
                .getMarkOfStudentBlocking("51003857");
        Assert.assertNotNull(result);

        // I know my marks!
        Assert.assertTrue(result.getTakenCourses().containsKey("501127"));
        Assert.assertTrue(result.getTakenCourses()
                .get("501127").getName().compareTo("Kỹ thuật lập trình") == 0
        );
        List<Double> programmingMarks =
                result.getTakenCoursesResult().get("501127");
        Assert.assertTrue(programmingMarks.size() == 1);
        Assert.assertEquals(10.0, programmingMarks.get(0), 1e-9);

        List<Double> calculusMarks =
                result.getTakenCoursesResult().get("006001");
        Assert.assertTrue(calculusMarks.size() == 2);
        if (calculusMarks.get(0) > calculusMarks.get(1)) {
            Assert.assertEquals(9.4, calculusMarks.get(0), 1e-9);
        } else {
            Assert.assertEquals(7.1, calculusMarks.get(0), 1e-9);
        }
    }
}
