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
            stu.setId(Integer.parseInt(buff[1].substring(5, 8)));
            stu.setName(buff[0]);
        } else {
            return null;
        }

        // Extract GPA information
        Map<Integer, Course> takenCourses = new HashMap<Integer, Course>();
        Map<Integer, List<Double>> takenCoursesResult
                = new HashMap<Integer, List<Double>>();
        Elements rows = doc.select(MARK_TABLEC_ROWS_SELECTOR);
        for (Element row : rows) {
            Elements tds = row.children();

            // footer
            if (tds.size() == 1) continue;

            // header
            if (tds.get(0).select("font > strong").size() > 0)
                continue;

            // content
            int coId = Integer.parseInt(tds.get(0).children().get(0).text());
            String coName = tds.get(1).children().get(0).text();
            double coMark;
            try {
                coMark = Double.parseDouble(tds.get(6).children().get(0).text());
            } catch (NumberFormatException e) {
                continue;
            }

            // tracking
            if (!takenCourses.containsKey(coId)) {
                Course course = new Course();
                course.setId(coId);
                course.setName(coName);

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
