package com.tester.testercommon.util.cache;

import lombok.Data;

/**
 * 缓存对象实体.
 * @Date 16:16 2021/3/9
 * @Author 温昌营
 **/
@Data
public class EntityCache {

    /**
     * 保存的数据
     */
    private Object datas;

    /**
     * 设置缓存的时间
     */
    private long cacheTime;


    public EntityCache(Object datas) {
        this.datas = datas;
        this.cacheTime = System.currentTimeMillis() / 1000;
    }

}
