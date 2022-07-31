package com.tester.testerswing.capture;

import com.tester.base.dto.model.BaseDTO;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PointInfo extends BaseDTO {
    private int x;
    private int y;
}
