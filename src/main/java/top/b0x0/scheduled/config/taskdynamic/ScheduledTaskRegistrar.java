package top.b0x0.scheduled.config.taskdynamic;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;
import top.b0x0.scheduled.enity.ToolJob;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ScheduledTaskRegistrar
 *
 * @author ManJiis
 * @since 2021-07-23
 */
@Component
public class ScheduledTaskRegistrar implements DisposableBean {

//    private final Map<Runnable, TaskScheduledFuture> scheduledTasks = new ConcurrentHashMap<>(16);

    private final Map<ToolJob, Map<Runnable, TaskScheduledFuture>> scheduledTaskMap = new ConcurrentHashMap<>(16);

    @Autowired
    private TaskScheduler taskScheduler;

    public TaskScheduler getScheduler() {
        return this.taskScheduler;
    }

    public Map<ToolJob, Map<Runnable, TaskScheduledFuture>> getScheduledTasks() {
        return this.scheduledTaskMap;
    }

    public void addCronTask(ToolJob toolJob, Runnable task, String cronExpression) {
        addCronTaskCore(toolJob, new CronTask(task, cronExpression));
    }

    private void addCronTaskCore(ToolJob toolJob, CronTask cronTask) {
        if (cronTask != null && toolJob != null) {
            Runnable task = cronTask.getRunnable();
            if (this.scheduledTaskMap.containsKey(toolJob)) {
                this.removeCronTask(toolJob);
            }
            Map<Runnable, TaskScheduledFuture> scheduledTasksTmp = new ConcurrentHashMap<>(16);
            scheduledTasksTmp.put(task, scheduleCronTask(cronTask));
            this.scheduledTaskMap.put(toolJob, scheduledTasksTmp);
        }
    }

    public void removeCronTask(ToolJob toolJob) {
        Map<Runnable, TaskScheduledFuture> scheduledFutureMap = this.scheduledTaskMap.remove(toolJob);

        ScheduledRunnable scheduledRunnable = new ScheduledRunnable(toolJob);
        TaskScheduledFuture taskScheduledFuture = scheduledFutureMap.get(scheduledRunnable);
        if (taskScheduledFuture != null) {
            taskScheduledFuture.cancel();
        }
    }

    @Override
    public void destroy() {
        Collection<Map<Runnable, TaskScheduledFuture>> values = scheduledTaskMap.values();
        for (Map<Runnable, TaskScheduledFuture> value : values) {
            Collection<TaskScheduledFuture> taskScheduledFutures = value.values();
            for (TaskScheduledFuture taskScheduledFuture : taskScheduledFutures) {
                taskScheduledFuture.cancel();
            }
        }
        this.scheduledTaskMap.clear();
    }

    /**
     * 将定时任务保存在内存中
     *
     * @param cronTask /
     * @return /
     */
    public TaskScheduledFuture scheduleCronTask(CronTask cronTask) {
        TaskScheduledFuture taskScheduledFuture = new TaskScheduledFuture();
        taskScheduledFuture.future = this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
        return taskScheduledFuture;
    }


    //    public void addCronTask(Runnable task, String cronExpression) {
//        addCronTaskCore(new CronTask(task, cronExpression));
//    }
//
//    private void addCronTaskCore(CronTask cronTask) {
//        if (cronTask != null) {
//            Runnable task = cronTask.getRunnable();
//            if (this.scheduledTasks.containsKey(task)) {
//                removeCronTask(task);
//            }
//            this.scheduledTasks.put(task, scheduleCronTask(cronTask));
//        }
//    }
//
//    public void removeCronTask(Runnable task) {
//        TaskScheduledFuture taskScheduledFuture = this.scheduledTasks.remove(task);
//        if (taskScheduledFuture != null) {
//            taskScheduledFuture.cancel();
//        }
//    }

//    @Override
//    public void destroy() {
//        for (TaskScheduledFuture task : this.scheduledTasks.values()) {
//            task.cancel();
//        }
//        this.scheduledTasks.clear();
//    }
}