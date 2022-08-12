package com.tester.testerswing.capture;

import com.tester.base.dto.model.BaseDTO;
import com.tester.testerswing.gaussian.GaussianHelper;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 起始点和终止点，用来划定一个区域。在这个区域取正态分布点
 *
 * @Date 9:54 2022/8/2
 * @Author 温昌营
 **/
@Data
@Accessors(chain = true)
public class GaussianPointInfoDTO extends BaseDTO {

    // 起始点
    private PointInfoDTO st;

    // 终止点
    private PointInfoDTO ed;


    public int getX() {
        return GaussianHelper.getGaussianInt(st.getX(), ed.getX());
    }

    public int getY() {
        return GaussianHelper.getGaussianInt(st.getY(), ed.getY());
    }
}
