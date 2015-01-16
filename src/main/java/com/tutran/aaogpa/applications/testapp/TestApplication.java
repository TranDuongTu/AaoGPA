package com.tutran.aaogpa.applications.testapp;

import com.tutran.aaogpa.applications.Application;
import com.tutran.aaogpa.data.models.Course;

import java.util.HashMap;
import java.util.Map;

public class TestApplication implements Application {
    public static void main(String[] args) {
        Course co1 = new Course();
        co1.setCourseId("AAA");

        Course co2 = new Course();
        co2.setCourseId("AAA");

        Map<Course, Double> map = new HashMap<Course, Double>();
        map.put(co1, 9d);
        map.put(co2, 8d);

        System.out.println(map.size());
        System.out.println(co1.hashCode());
        System.out.println(co2.hashCode());
    }
}
