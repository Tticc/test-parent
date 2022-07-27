package com.tester.testersearch.esUtil;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class Knowledge {
    Integer type;
    String belong;
    String keyWord;
    String desc;
    Integer priority;
    Integer deleted;
    Date createdTime;
    Date updatedTime;
    String createdBy;
    String updatedBy;
}