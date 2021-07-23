package top.b0x0.scheduled.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定时任务的使用
 *
 * @author ManJiis
 **/
//@Component
public class MyTask {

    public static String getThreadName() {
        return Thread.currentThread().getName();
    }

    /**
     * 每5秒执行一次
     */
    @Scheduled(cron = "0/5 * *  * * ? ")
    public void execute() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(getThreadName() + "  MyTask.execute() 现在是： " + df.format(new Date()));
    }

    @Scheduled(cron = "${time.cron}")
    void testPlaceholder1() {
        System.out.println(getThreadName() + "  MyTask.testPlaceholder1() Execute at " + System.currentTimeMillis());
    }

    @Scheduled(cron = "*/${time.interval} * * * * *")
    void testPlaceholder2() {
        System.out.println(getThreadName() + "  MyTask.testPlaceholder2() Execute at " + System.currentTimeMillis());
    }

    @Scheduled(fixedDelayString = "${time.fixedDelay}")
    void testFixedDelayString() {
        System.out.println(getThreadName() + "  MyTask.testFixedDelayString() 现在是： " + System.currentTimeMillis());
    }
}
