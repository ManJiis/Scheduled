package top.b0x0.scheduled.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import top.b0x0.scheduled.common.ScheduledContains;
import top.b0x0.scheduled.config.taskdynamic.TaskScheduledFuture;
import top.b0x0.scheduled.enity.ToolJob;
import top.b0x0.scheduled.mapper.ToolJobMapper;
import top.b0x0.scheduled.config.taskdynamic.ScheduledTaskRegistrar;
import top.b0x0.scheduled.config.taskdynamic.ScheduledRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author TANG
 * @since 2021-07-23
 * @since jdk1.8
 */
@Component
public class JobService {

    @Autowired
    ToolJobMapper toolJobMapper;

    @Autowired
    ScheduledTaskRegistrar scheduledTaskRegistrar;

    public boolean addJob(ToolJob job) {
        ToolJob toolJob = toolJobMapper.selectOne(new QueryWrapper<ToolJob>()
                .eq("job_class_name", job.getJobClassName())
                .eq("job_method_name", job.getJobMethodName()));

        if (toolJob != null) {
            return false;
        }
        int insert = toolJobMapper.insert(toolJob);
        if (!(insert > 0)) {
            return false;
        }
        if (job.getJobStatus().equals(ScheduledContains.JOB_STATUS_NORMAL)) {
            ScheduledRunnable task = new ScheduledRunnable(job);
//            scheduledTaskRegistrar.addCronTask(task, job.getCron());
            scheduledTaskRegistrar.addCronTask(job, task, job.getCron());
        }
        return true;
    }

    public boolean editJob(ToolJob job) {
        ToolJob toolJob = toolJobMapper.selectOne(new QueryWrapper<ToolJob>()
                .eq("job_id", job.getJobId())
                .eq("job_bean_name", job.getJobBeanName())
                .eq("job_class_name", job.getJobClassName())
                .eq("job_method_name", job.getJobMethodName()));

        if (toolJob == null) {
            return false;
        }

        //先移除再添加
        if (job.getJobStatus().equals(ScheduledContains.JOB_STATUS_NORMAL)) {
//            ScheduledRunnable task = new ScheduledRunnable(job);
//            scheduledTaskRegistrar.removeCronTask(task);
            scheduledTaskRegistrar.removeCronTask(job);
        }

        if (job.getJobStatus().equals(ScheduledContains.JOB_STATUS_NORMAL)) {
            ScheduledRunnable task = new ScheduledRunnable(job);
//            scheduledTaskRegistrar.addCronTask(task, job.getCron());
            scheduledTaskRegistrar.addCronTask(job, task, job.getCron());
        }
        toolJobMapper.updateById(job);
        return true;
    }

    public boolean deleteJob(ToolJob job) {

        int delete = toolJobMapper.delete(new UpdateWrapper<ToolJob>()
                .eq("job_id", job.getJobId())
                .eq("job_bean_name", job.getJobBeanName())
                .eq("job_class_name", job.getJobClassName())
                .eq("job_method_name", job.getJobMethodName()));

        if (!(delete > 0)) {
            return false;
        }

        ScheduledRunnable task = new ScheduledRunnable(job);
//        scheduledTaskRegistrar.removeCronTask(task);
        scheduledTaskRegistrar.removeCronTask(job);

        return true;
    }

    public boolean statusJob(ToolJob job) {

        ToolJob toolJob = toolJobMapper.selectOne(new UpdateWrapper<ToolJob>()
                .eq("job_id", job.getJobId())
                .eq("job_bean_name", job.getJobBeanName())
                .eq("job_class_name", job.getJobClassName())
                .eq("job_method_name", job.getJobMethodName()));

        if (toolJob == null) {
            return false;
        }

        // 状态（1正常 0暂停）
        if (job.getJobStatus().equals(ScheduledContains.JOB_STATUS_DISABLE)) {
            ScheduledRunnable task = new ScheduledRunnable(job);
//            scheduledTaskRegistrar.removeCronTask(task);
            scheduledTaskRegistrar.removeCronTask(job);
        }
        if (job.getJobStatus().equals(ScheduledContains.JOB_STATUS_NORMAL)) {
            ScheduledRunnable task = new ScheduledRunnable(job);
//            scheduledTaskRegistrar.addCronTask(task, job.getCron());
            scheduledTaskRegistrar.addCronTask(job, task, job.getCron());
        }
        toolJobMapper.updateById(job);
        return true;
    }

    public List<ToolJob> listJob() {
        return toolJobMapper.selectList(new QueryWrapper<>());
    }

    public Object listJobForRunning() {
        Map<ToolJob, Map<Runnable, TaskScheduledFuture>> scheduledTasks = scheduledTaskRegistrar.getScheduledTasks();
        return scheduledTasks.keySet().toArray();
    }
}
