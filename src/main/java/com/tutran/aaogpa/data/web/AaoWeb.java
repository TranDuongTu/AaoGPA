package com.tutran.aaogpa.data.web;

public interface AaoWeb {
    public final String URL =
            "http://www.aao.hcmut.edu.vn/image/data/Tra_cuu/xem_bd";

    String getResultsBlocking(String stuId);
}
