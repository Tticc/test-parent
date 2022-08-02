package com.tester.testerswing.capture;

import com.tester.base.dto.model.BaseDTO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 坐标。起点 (0,0) 在左上角
 *
 * @Date 9:54 2022/8/2
 * @Author 温昌营
 **/
@Data
@Accessors(chain = true)
public class PointInfoDTO extends BaseDTO {

    // x轴，横轴
    private int x;

    // y轴，纵轴
    private int y;
}
