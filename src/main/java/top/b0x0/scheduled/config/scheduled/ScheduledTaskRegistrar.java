package top.b0x0.scheduled.config.scheduled;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;
import top.b0x0.scheduled.enity.ToolJob;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * ScheduledTaskRegistrar
 *
 * @author ManJiis
 * @since 2021-07-23
 */
@Component
public class ScheduledTaskRegistrar implements DisposableBean {

//    private final Map<Runnable, TaskScheduledFuture> scheduledTasks = new ConcurrentHashMap<>(16);

    private final List<ToolJob> normalStatusJobList = new ArrayList<>();

    private final List<CronTask> cronTaskList = new ArrayList<>();

    private final List<ScheduledFutureTask> scheduledFutureTaskList = new ArrayList<>();

    private final Map<ToolJob, Map<Runnable, ScheduledFutureTask>> scheduledTaskMap = new ConcurrentHashMap<>(16);

    @Autowired
    private org.springframework.scheduling.TaskScheduler taskScheduler;

    public Map<ToolJob, Map<Runnable, ScheduledFutureTask>> getScheduledTasks() {
        return this.scheduledTaskMap;
    }

    public List<ToolJob> getNormalStatusJobList() {
        return this.normalStatusJobList;
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
            Map<Runnable, ScheduledFutureTask> scheduledTasksTmp = new ConcurrentHashMap<>(16);
            scheduledTasksTmp.put(task, scheduleCronTask(cronTask));
            this.scheduledTaskMap.put(toolJob, scheduledTasksTmp);
            normalStatusJobList.add(toolJob);
        }
    }

    public void removeCronTask(ToolJob toolJob) {
        Map<Runnable, ScheduledFutureTask> scheduledFutureMap = this.scheduledTaskMap.remove(toolJob);
//        ScheduledRunnable scheduledRunnable = SpringContextUtils.getBean(ScheduledRunnable.class);
        ScheduledRunnable scheduledRunnable = new ScheduledRunnable(toolJob);
        scheduledRunnable.setToolJob(toolJob);
        ScheduledFutureTask futureTask = scheduledFutureMap.get(scheduledRunnable);
        if (futureTask != null) {
            futureTask.cancel();
        }
        normalStatusJobList.remove(toolJob);
    }

    @Override
    public void destroy() {
        Collection<Map<Runnable, ScheduledFutureTask>> values = scheduledTaskMap.values();
        for (Map<Runnable, ScheduledFutureTask> value : values) {
            Collection<ScheduledFutureTask> scheduledFutureTasks = value.values();
            for (ScheduledFutureTask futureTask : scheduledFutureTasks) {
                futureTask.cancel();
            }
        }
        this.scheduledTaskMap.clear();
    }

    /**
     * @param cronTask /
     * @return /
     */
    public ScheduledFutureTask scheduleCronTask(CronTask cronTask) {
        ScheduledFuture<?> future = this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
        return new ScheduledFutureTask(future);
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