package com.tutran.aaogpa.data.web.parsers.jsoup;

import com.tutran.aaogpa.data.web.AaoWebHtmlParser;
import com.tutran.aaogpa.data.web.ParsedResult;
import com.tutran.aaogpa.data.models.Course;
import com.tutran.aaogpa.data.models.Student;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class AaoWebHtmlParserJsoup implements AaoWebHtmlParser {
    private static final String NAME_SELECTOR =
            "div > div > div > div > div > div > div > div > form > div > font > div";
    private static final String MARK_TABLEC_ROWS_SELECTOR =
            "div > div > div > div > div > div > div > div > form > div > table > tbody > tr > td > table > tbody > tr";

    @Override
    public ParsedResult parse(String html) {
        Document doc = Jsoup.parse(html);

        // Extract student's identity
        Student stu = new Student();
        Elements identitySection = doc.select(NAME_SELECTOR);
        if (identitySection.size() > 0) {
            String[] buff = identitySection.get(0).text().split("-");
            stu.setStudentId(buff[1].substring(5, 13));
            stu.setName(buff[0].trim());
        } else {
            return null;
        }

        // Extract GPA information
        Map<String, Course> takenCourses = new HashMap<String, Course>();
        Map<String, List<Double>> takenCoursesResult
                = new HashMap<String, List<Double>>();
        Elements rows = doc.select(MARK_TABLEC_ROWS_SELECTOR);
        for (Element row : rows) {
            Elements tds = row.children();

            // footer
            if (tds.size() == 1) continue;

            // header
            if (tds.get(0).select("font > strong").size() > 0)
                continue;

            // content
            String coId = tds.get(0).children().get(0).text();
            String coName = tds.get(1).children().get(0).text();
            double coMark;
            int credit;
            try {
                coMark = Double.parseDouble(tds.get(6).children().get(0).text());
                credit = Integer.parseInt(tds.get(3).children().get(0).text());
            } catch (NumberFormatException e) {
                continue;
            }

            // tracking
            if (!takenCourses.containsKey(coId)) {
                Course course = new Course();
                course.setCourseId(coId);
                course.setName(coName);
                course.setCredit(credit);

                takenCourses.put(coId, course);
                takenCoursesResult.put(coId, new ArrayList<Double>(
                        Arrays.asList(coMark)
                ));
            } else {
                takenCoursesResult.get(coId).add(coMark);
            }
        }

        ParsedResult result = new ParsedResult();
        result.setStudent(stu);
        result.setTakenCourses(takenCourses);
        result.setTakenCoursesResult(takenCoursesResult);
        return result;
    }
}
