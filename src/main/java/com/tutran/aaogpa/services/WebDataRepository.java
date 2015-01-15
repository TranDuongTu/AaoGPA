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

    public ParsedResult getMarkOfStudentBlocking(String stuId) {
        return aaoWebHtmlParser.parse(aaoWeb.getResultsBlocking(stuId));
    }
}
