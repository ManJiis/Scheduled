package top.b0x0.scheduled.common;

/**
 * @author TANG
 * @since 2021-07-23
 * @since jdk1.8
 */
public class ScheduledContains {

    /**
     * 状态（1正常 0暂停）
     */
    public static final Integer JOB_STATUS_NORMAL = 1;
    public static final Integer JOB_STATUS_DISABLE = 0;
    /**
     * 任务执行状态（ 0已停止 1执行中 ）
     */
    public static final Integer JOB_EXEC_STATUS_RUNNING = 1;
    public static final Integer JOB_EXEC_STATUS_STOP = 0;
}
