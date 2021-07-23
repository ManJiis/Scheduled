package top.b0x0.scheduled.task;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定时任务的使用
 *
 * @author ManJiis
 **/
@Component
public class MyDynamicTask {

    public static String currentThreadName() {
        return Thread.currentThread().getName();
    }

//    public static void main(String[] args) {
//        String currentMethodName = currentMethodName();
//        System.out.println("currentMethodName = " + currentMethodName);
//        String currentClassName = currentClassName();
//        System.out.println("currentClassName = " + currentClassName);
//    }

    public static String currentClassName() {
        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        int lastIndexOf = className.lastIndexOf(".");
        return className.substring(lastIndexOf + 1);
    }

    public static String currentFullyClassName() {
        return Thread.currentThread().getStackTrace()[2].getClassName();
    }

    public static String currentMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    public void execute() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("[" + currentThreadName() + "] " + currentClassName() + "." + currentMethodName() + " 现在时间是： " + df.format(new Date()));
    }

    void sayHello() {
        System.out.println("[" + currentThreadName() + "] " + currentClassName() + "." + currentMethodName() + " say hello: ManJiis.");
    }

    void sayHello(String name) {
        System.out.println("[" + currentThreadName() + "] " + currentClassName() + "." + currentMethodName() + " say hello: " + name);
    }

    void printlnCurrentTime() {
        System.out.println("[" + currentThreadName() + "] " + currentClassName() + "." + currentMethodName() + " 当前时间是： " + System.currentTimeMillis());
    }

}
