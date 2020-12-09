package com.tester.testercv.config;

import lombok.Data;

import java.util.Date;

@Data
public class OutBean {
    private Date dateStart = new Date();
    private Date dateEnd = new Date();
    public void doPrint(){
        System.out.println(dateStart);
        System.out.println(dateEnd);
    }
}