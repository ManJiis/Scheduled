package top.b0x0.scheduled.task;

import org.springframework.stereotype.Component;
import top.b0x0.scheduled.common.ScheduledContains;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.b0x0.scheduled.common.CurrentThreadUtils.*;

/**
 * 定时任务的使用
 *
 * @author ManJiis
 **/
@Component
public class MyDynamicTask {

    public static void main(String[] args) {
        String currentMethodName = currentMethodName();
        System.out.println("currentMethodName = " + currentMethodName);
        String currentClassName = currentClassName();
        System.out.println("currentClassName = " + currentClassName);

        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        int nextInt = threadLocalRandom.nextInt(9999);
        System.out.println("nextInt = " + nextInt);
        System.out.printf("%04d%n", new Random().nextInt(9999));
        System.out.println("随机 = " + System.currentTimeMillis() + String.format("%04d", ThreadLocalRandom.current().nextInt(9999)));


        Matcher matcher = ScheduledContains.JOB_CRON_PATTERN.matcher("0 */30 * * * ?");
        boolean matches = matcher.matches();
        System.out.println("matches = " + matches);

        String regEx = "(((^([0-9]|[0-5][0-9])(\\,|\\-|\\/){1}([0-9]|[0-5][0-9]) )|^([0-9]|[0-5][0-9]) |^(\\* ))((([0-9]|[0-5][0-9])(\\,|\\-|\\/){1}([0-9]|[0-5][0-9]) )|([0-9]|[0-5][0-9]) |(\\* ))((([0-9]|[01][0-9]|2[0-3])(\\,|\\-|\\/){1}([0-9]|[01][0-9]|2[0-3]) )|([0-9]|[01][0-9]|2[0-3]) |(\\* ))((([0-9]|[0-2][0-9]|3[01])(\\,|\\-|\\/){1}([0-9]|[0-2][0-9]|3[01]) )|(([0-9]|[0-2][0-9]|3[01]) )|(\\? )|(\\* )|(([1-9]|[0-2][0-9]|3[01])L )|([1-7]W )|(LW )|([1-7]\\#[1-4] ))((([1-9]|0[1-9]|1[0-2])(\\,|\\-|\\/){1}([1-9]|0[1-9]|1[0-2]) )|([1-9]|0[1-9]|1[0-2]) |(\\* ))(([1-7](\\,|\\-|\\/){1}[1-7])|([1-7])|(\\?)|(\\*)|(([1-7]L)|([1-7]\\#[1-4]))))|(((^([0-9]|[0-5][0-9])(\\,|\\-|\\/){1}([0-9]|[0-5][0-9]) )|^([0-9]|[0-5][0-9]) |^(\\* ))((([0-9]|[0-5][0-9])(\\,|\\-|\\/){1}([0-9]|[0-5][0-9]) )|([0-9]|[0-5][0-9]) |(\\* ))((([0-9]|[01][0-9]|2[0-3])(\\,|\\-|\\/){1}([0-9]|[01][0-9]|2[0-3]) )|([0-9]|[01][0-9]|2[0-3]) |(\\* ))((([0-9]|[0-2][0-9]|3[01])(\\,|\\-|\\/){1}([0-9]|[0-2][0-9]|3[01]) )|(([0-9]|[0-2][0-9]|3[01]) )|(\\? )|(\\* )|(([1-9]|[0-2][0-9]|3[01])L )|([1-7]W )|(LW )|([1-7]\\#[1-4] ))((([1-9]|0[1-9]|1[0-2])(\\,|\\-|\\/){1}([1-9]|0[1-9]|1[0-2]) )|([1-9]|0[1-9]|1[0-2]) |(\\* ))(([1-7](\\,|\\-|\\/){1}[1-7] )|([1-7] )|(\\? )|(\\* )|(([1-7]L )|([1-7]\\#[1-4]) ))((19[789][0-9]|20[0-9][0-9])\\-(19[789][0-9]|20[0-9][0-9])))";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher1 = pattern.matcher("0 */30 * * * ?");
        System.out.println("matcher1 = " + matcher1.matches());
        boolean b = checkSpringCron("0 */30 * * * ?");
    }

    /**
     * (划掉 有问题)
     *
     * @param cron /
     * @return /
     */
    @Deprecated
    public static boolean checkSpringCron(String cron) {
        // cron表达式格式验证
        String str = "0 * * * * ?";

        String regMiao = "([0-9]{1,2}|[0-9]{1,2}\\-[0-9]{1,2}|\\*|[0-9]{1,2}\\/[0-9]{1,2}|[0-9]{1,2}\\,[0-9]{1,2})";
        String regFen = "\\s([0-9]{1,2}|[0-9]{1,2}\\-[0-9]{1,2}|\\*|[0-9]{1,2}\\/[0-9]{1,2}|[0-9]{1,2}\\,[0-9]{1,2})";
        String regShi = "\\s([0-9]{1,2}|[0-9]{1,2}\\-[0-9]{1,2}|\\*|[0-9]{1,2}\\/[0-9]{1,2}|[0-9]{1,2}\\,[0-9]{1,2})";
        String regRi = "\\s([0-9]{1,2}|[0-9]{1,2}\\-[0-9]{1,2}|\\*|[0-9]{1,2}\\/[0-9]{1,2}|[0-9]{1,2}\\,[0-9]{1,2}|\\?|L|W|C)";
        String regYue = "\\s([0-9]{1,2}|[0-9]{1,2}\\-[0-9]{1,2}|\\*|[0-9]{1,2}\\/[0-9]{1,2}|[0-9]{1,2}\\,[0-9]{1,2}|JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)";
        String regZhou = "\\s([1-7]{1}|[1-7]{1}\\-[1-7]{1}|[1-7]{1}\\#[1-7]{1}|\\*|[1-7]{1}\\/[1-7]{1}|[1-7]{1}\\,[1-7]{1}|MON|TUES|WED|THUR|FRI|SAT|SUN|\\?|L|C)";
        String regNian = "(\\s([0-9]{4}|[0-9]{4}\\-[0-9]{4}|\\*|[0-9]{4}\\/[0-9]{4}|[0-9]{4}\\,[0-9]{4})){0,1}";
        String regEx = regMiao + regFen + regShi + regRi + regYue + regZhou + regNian;
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(cron);
        // 字符串是否与正则表达式相匹配
        boolean rs = matcher.matches();
        System.out.println("checkSpringCron ==> " + rs);
        return rs;
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
