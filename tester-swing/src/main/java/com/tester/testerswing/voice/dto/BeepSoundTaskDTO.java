package com.tester.testerswing.voice.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BeepSoundTaskDTO extends MessageTaskDTO {

    // 提示音文字
    private String text = "ok";

    // 提示音量。0-100。默认100，即保持电脑音量
    private int volume = 100;

    // 提示音速。默认0
    private int speed = 0;

}
