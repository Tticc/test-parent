package com.tester.testerswing.voice.dto;

import com.tester.base.dto.model.BaseDTO;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageTaskDTO extends BaseDTO {

    private Integer type;

}
