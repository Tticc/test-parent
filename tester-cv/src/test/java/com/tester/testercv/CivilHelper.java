package com.tester.testercv;


import com.tester.testerpool.config.AsyncExecutorConfig;
import com.tester.testerpool.config.AsyncScheduleExecutorConfig;
import com.tester.testerpool.config.PoolBeanConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * CivilHelper
 */
@Slf4j
public class CivilHelper {

    public static ScheduledExecutorService scheduledExecutorService;

    public static ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public static long initialDelay = 4L;
    public static long period = 10L;
    public static TimeUnit timeUnit = TimeUnit.MINUTES;


    public static int calDis(int[] a,int[] b){
        double i = Math.sqrt(Math.pow(a[0] - b[0],2) + Math.pow(a[1] - b[1],2) + Math.pow(a[2] - b[2],2));
        return (int)Math.round(i);
    }


    public static void civilGrowth(Civil civil){
        // 将 growth 行为加入定时线程池，定时执行
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            civilGrowthSub(civil);
        },initialDelay,period,timeUnit);
    }
    public static void civilGrowthSub(Civil civil){
        if(civil.getHp() < 0){
            return;
        }
        civil.setAge(civil.getAge()+(int)period)
                .setAttack(civil.getAge()/100)
                .setDefence(civil.getAge()/100)
                .setHp(civil.getAge()/100)
                .setRadius(civil.getAge()/100);
    }

    public static void checker(List<Civil> alive,List<Civil> death){
        Assert.notNull(alive,"alive should not be NULL");
        Assert.notNull(death,"death should not be NULL");
        // 每 10 分钟执行一次，清理死亡实体
        threadPoolTaskScheduler.schedule(() -> {
            if (CollectionUtils.isEmpty(alive)) {
                return;
            }
            log.info("entity:{}",alive.get(0));
            for (Civil civil : alive) {
                if (civil.getHp() < 0) {
                    alive.remove(civil);
                    death.add(civil);
                }
            }
        }, new CronTrigger("0 0/10 * * * ?"));
        threadPoolTaskScheduler.schedule(()-> log.info("keep alive"), new CronTrigger("0/10 * * * * ?"));
//        threadPoolTaskScheduler.schedule(()-> System.out.println("schedule print2"), new CronTrigger("0/20 * * * * ?"));
//        threadPoolTaskScheduler.schedule(()-> System.out.println("schedule print3"), new CronTrigger("0/30 * * * * ?"));
//        threadPoolTaskScheduler.schedule(()-> System.out.println("schedule print4"), new CronTrigger("0/40 * * * * ?"));

    }




    static {
        // jdk schedule
        scheduledExecutorService = new PoolBeanConfig().getScheduledExecutorService();
        // spring schedule
        threadPoolTaskScheduler = new AsyncScheduleExecutorConfig().getMyThreadPoolTaskScheduler();
    }
}
