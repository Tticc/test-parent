package com.tester.testerswing.capture;

import com.tester.base.dto.model.BaseDTO;
import com.tester.testercommon.constant.ConstantList;
import com.tester.testerswing.gaussian.GaussianHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 起始点和终止点，用来划定一个区域。在这个区域取正态分布点
 *
 * @Date 2024-12-14
 * @Author 温昌营
 **/
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class GaussianStrPointInfoDTO extends BaseDTO {

    public GaussianStrPointInfoDTO(String one){
        this.st = one;
        this.ed = one;
    }

    // 起始点。 x,y
    private String st;

    // 终止点。x,y
    private String ed;


    public int getX() {
        return GaussianHelper.getGaussianInt(Integer.valueOf(st.split(ConstantList.COMMA_SPLIT_STR)[0].trim()), Integer.valueOf(ed.split(ConstantList.COMMA_SPLIT_STR)[0].trim()));
    }

    public int getY() {
        return GaussianHelper.getGaussianInt(Integer.valueOf(st.split(ConstantList.COMMA_SPLIT_STR)[1].trim()), Integer.valueOf(ed.split(ConstantList.COMMA_SPLIT_STR)[1].trim()));
    }
}
