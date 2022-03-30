package com.tester.testerasync.constants;

public class MqConstant {

    //**************************** 内部mq区域 ***************************/
    public static final String INNER_MQ_TOPIC_PREFIX = "my_topic_";

    /**
     * 消息发送topic
     **/
    public static final String MESSAGE_SENT_TOPIC = INNER_MQ_TOPIC_PREFIX + "message_sent_topic";


    // groups
    public static final String TEST_ONE_GROUP1 = "test_one_group1";


    //**************************** 外部交互mq区域 ***************************/
    /**
     * 优惠券自动过期结果消费消息topic
     */
    public static final String MQ_TOPIC_COUPON_OUTDATE_PROCESS = "test_one";

}
