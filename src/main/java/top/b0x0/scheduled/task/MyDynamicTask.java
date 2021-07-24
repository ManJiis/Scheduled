package top.b0x0.scheduled.task;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static top.b0x0.scheduled.common.CurrentThreadUtils.*;

/**
 * 定时任务的使用
 *
 * @author ManJiis
 **/
@Component
public class MyDynamicTask {

    public static void main(String[] args) {
//        String currentMethodName = currentMethodName();
//        System.out.println("currentMethodName = " + currentMethodName);
//        String currentClassName = currentClassName();
//        System.out.println("currentClassName = " + currentClassName);

        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        int nextInt = threadLocalRandom.nextInt(9999);
        System.out.println("nextInt = " + nextInt);
        System.out.printf("%04d%n", new Random().nextInt(9999));
        System.out.println(" = " + System.currentTimeMillis() + String.format("%04d", ThreadLocalRandom.current().nextInt(9999)));
    }

    public void execute() throws InterruptedException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Thread.sleep(5000);
        System.out.println("[" + currentThreadName() + "] " + currentClassName() + "." + currentMethodName() + " 现在时间是： " + df.format(new Date()));
    }

    void sayHello() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("[" + currentThreadName() + "] " + currentClassName() + "." + currentMethodName() + " say hello: ManJiis.");
    }

    void sayHello(String name) {
        System.out.println("[" + currentThreadName() + "] " + currentClassName() + "." + currentMethodName() + " say hello: " + name);
    }

    void printlnCurrentTime() {
        System.out.println("[" + currentThreadName() + "] " + currentClassName() + "." + currentMethodName() + " 当前时间是： " + System.currentTimeMillis());
    }

}
