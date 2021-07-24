package top.b0x0.scheduled.common;

/**
 * @author ManJiis
 * @since 2021-07-23
 * @since JDK1.8
 */
public class CurrentThreadUtils {

    public static String currentThreadName() {
        return Thread.currentThread().getName();
    }

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
}
