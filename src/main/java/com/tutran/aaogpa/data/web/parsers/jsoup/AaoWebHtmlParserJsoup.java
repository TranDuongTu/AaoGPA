package com.tutran.aaogpa.data.web.parsers.jsoup;

import com.tutran.aaogpa.data.models.Course;
import com.tutran.aaogpa.data.models.Student;
import com.tutran.aaogpa.data.web.AaoWebHtmlParser;
import com.tutran.aaogpa.data.web.ParsedResult;
import com.tutran.aaogpa.utils.Tuple;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class AaoWebHtmlParserJsoup implements AaoWebHtmlParser {
    private static final String NAME_SELECTOR =
            "div > div > div > div > div > div > div > div > "
                    + "form > div > font > div";
    private static final String MARK_TABLEC_ROWS_SELECTOR =
            "div > div > div > div > div > div > div > div > "
                    + "form > div > table > tbody > tr > td > "
                    + "table > tbody > tr";

    private static final Set<String> STRINGS_MEAN_ZERO = new HashSet<String>(
            Arrays.asList("CT", "MT", "VT", "KD", "DT", "IH")
    );

    private static final Set<String> STRINGS_MEAN_NOT_COUNT = new HashSet<String>(
            Arrays.asList("HT", "CH", "RT")
    );

    private static final Set<Double> NUMBERS_MEAN_ZERO = new HashSet<Double>(
            Arrays.asList(11d, 12d, 13d, 20d, 21d, 22d)
    );

    private static final Set<Double> NUMBERS_MEAN_NOT_COUNT = new HashSet<Double>(
            Arrays.asList(14d, 15d, 17d)
    );

    @Override
    public ParsedResult parse(String html) {
        Document doc = Jsoup.parse(html);

        Student stu = extractStudentIdentity(doc);
        Map<Course, List<Double>> takenCourses = extractCourses(doc);

        ParsedResult result = new ParsedResult();
        result.setStudent(stu);
        result.setTakenCourses(takenCourses);
        return result;
    }

    private Student extractStudentIdentity(Document doc) {
        Student stu = new Student();
        Elements identitySection = doc.select(NAME_SELECTOR);
        if (identitySection.size() > 0) {
            String[] buff = identitySection.get(0).text().split("-");
            stu.setStudentId(buff[1].substring(5, 13));
            stu.setName(buff[0].trim());
            return stu;
        } else {
            return null;
        }
    }

    private Map<Course, List<Double>> extractCourses(Document doc) {
        Map<Course, List<Double>> result = new HashMap<Course, List<Double>>();

        Elements resultRows = doc.select(MARK_TABLEC_ROWS_SELECTOR);
        for (Element row : resultRows) {
            Elements rowElements = row.children();

            // header
            if (rowElements.get(0).select("font > strong").size() > 0)
                continue;

            // footer
            if (rowElements.size() == 1) continue;

            // content
            String coId = rowElements.get(0).children().get(0).text();
            String coName = rowElements.get(1).children().get(0).text();
            String markString = rowElements.get(6).children().get(0).text();
            String creditString = rowElements.get(3).children().get(0).text();

            Tuple<Course, Double> rowResult = makeCourseFromParsedInfo(
                    coId, coName, markString, creditString);
            if (rowResult == null) continue;

            if (!result.containsKey(rowResult.getFirst())) {
                result.put(rowResult.getFirst(),
                        Arrays.asList(rowResult.getSecond()));
            } else {
                result.get(rowResult.getFirst())
                        .add(rowResult.getSecond());
            }
        }

        return result;
    }

    private Tuple<Course, Double> makeCourseFromParsedInfo(
            String coId, String coName,
            String markString, String creditString) {
        double mark;

        if (STRINGS_MEAN_ZERO.contains(markString)) {
            mark = 0;
        } else if (STRINGS_MEAN_NOT_COUNT.contains(markString)) {
            return null;
        } else {
            try {
                mark = Double.parseDouble(markString);
                if (NUMBERS_MEAN_NOT_COUNT.contains(mark))
                    throw new NumberFormatException();
                if (NUMBERS_MEAN_ZERO.contains(mark))
                    mark = 0;
            } catch (NumberFormatException e) {
                return null;
            }
        }

        Course course = new Course();
        course.setCourseId(coId);
        course.setName(coName);
        course.setCredit(Integer.parseInt(creditString));
        return new Tuple<Course, Double>(course, mark);
    }
}
