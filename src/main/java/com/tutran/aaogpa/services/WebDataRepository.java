package com.tutran.aaogpa.services;

import com.tutran.aaogpa.data.web.AaoWeb;
import com.tutran.aaogpa.data.web.AaoWebHtmlParser;
import com.tutran.aaogpa.data.web.ParsedResult;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Repository for services fetched from aao web
 */
public class WebDataRepository {

    private AaoWeb aaoWeb;
    private AaoWebHtmlParser aaoWebHtmlParser;

    @Autowired
    public WebDataRepository(AaoWeb aaoWeb, AaoWebHtmlParser aaoWebHtmlParser) {
        this.aaoWeb = aaoWeb;
        this.aaoWebHtmlParser = aaoWebHtmlParser;
    }

    public ParsedResult getMarkOfStudentBlocking(int id) {
        return aaoWebHtmlParser.parse(aaoWeb.getResultsBlocking(id));
    }

    public void getMarkOfStudentNonBlocking(final Listener listener) {
        aaoWeb.getResultsNonBlocking(new Listener() {
            @Override
            public void onFinishOneStudent(String html) {
                listener.onFinishOneStudent(html);
            }
        });
    }

    public static interface Listener {
        void onFinishOneStudent(String html);
    }
}
