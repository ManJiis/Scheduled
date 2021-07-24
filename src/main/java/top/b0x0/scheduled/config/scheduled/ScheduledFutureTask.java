package top.b0x0.scheduled.config.scheduled;

import java.util.concurrent.ScheduledFuture;

/**
 * @author ManJiis
 * @since 2021-07-23
 */
public final class ScheduledFutureTask {

    private final ScheduledFuture<?> taskFuture;

    public ScheduledFutureTask(ScheduledFuture<?> taskFuture) {
        this.taskFuture = taskFuture;
    }

    /**
     * 取消定时任务
     */
    public void cancel() {
        ScheduledFuture<?> future = this.taskFuture;
        if (future != null) {
            future.cancel(true);
        }
    }

}