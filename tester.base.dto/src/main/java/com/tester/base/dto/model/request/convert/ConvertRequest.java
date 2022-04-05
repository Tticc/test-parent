package com.tester.base.dto.model.request.convert;

import com.tester.base.dto.model.BaseDTO;
import lombok.Data;

@Data
public class ConvertRequest extends BaseDTO {
    private TestConvertRequest testConvertRequest;
}
