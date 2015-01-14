package com.tutran.aaogpa.data.web;

import com.tutran.aaogpa.services.WebDataRepository;

public interface AaoWeb {
    public final String URL =
            "http://www.aao.hcmut.edu.vn/image/data/Tra_cuu/xem_bd";

    String getResultsBlocking(String id);
    void getResultsNonBlocking(WebDataRepository.Listener listener);
}
