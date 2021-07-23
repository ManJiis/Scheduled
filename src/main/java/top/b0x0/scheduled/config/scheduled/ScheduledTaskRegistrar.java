package top.b0x0.scheduled.config.scheduled;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;
import top.b0x0.scheduled.common.SpringContextUtils;
import top.b0x0.scheduled.enity.ToolJob;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    private final List<ToolJob> normalStatusJobList = new ArrayList<ToolJob>();

    private final Map<ToolJob, Map<Runnable, TaskScheduledFuture>> scheduledTaskMap = new ConcurrentHashMap<>(16);

    @Autowired
    private TaskScheduler taskScheduler;

    public Map<ToolJob, Map<Runnable, TaskScheduledFuture>> getScheduledTasks() {
        return this.scheduledTaskMap;
    }

    public List<ToolJob> getNormalStatusJobList() {
        return this.normalStatusJobList;
    }

    public void addCronTask(ToolJob toolJob, Runnable task, String cronExpression) {
        addCronTaskCore(toolJob, new CronTask(task, cronExpression));
    }

    private void addCronTaskCore(ToolJob toolJob, CronTask cronTask) {
        normalStatusJobList.add(toolJob);
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
        normalStatusJobList.remove(toolJob);
        ScheduledRunnable scheduledRunnable = new ScheduledRunnable(toolJob);
//        ScheduledRunnable scheduledRunnable = SpringContextUtils.getBean(ScheduledRunnable.class);
        scheduledRunnable.setToolJob(toolJob);
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