package com.tutran.aaogpa.data.local;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/test-context.xml"})
public class StudentDAOTest extends GenericDAOTest {

    @Test
    public void testInsertNewStudent() {

    }
}