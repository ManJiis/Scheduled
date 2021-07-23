package top.b0x0.scheduled.config.taskdynamic;

import java.util.concurrent.ScheduledFuture;

/**
 * @author ManJiis
 * @since 2021-07-23
 */
public final class TaskScheduledFuture {

    volatile ScheduledFuture<?> future;

    /**
     * 取消定时任务
     */
    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}