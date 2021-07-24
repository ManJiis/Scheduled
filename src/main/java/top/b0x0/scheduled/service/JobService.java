package top.b0x0.scheduled.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.b0x0.scheduled.common.ScheduledContains;
import top.b0x0.scheduled.enity.ToolJob;
import top.b0x0.scheduled.enity.ToolJobReq;
import top.b0x0.scheduled.mapper.ToolJobMapper;
import top.b0x0.scheduled.config.scheduled.ScheduledTaskRegistrar;
import top.b0x0.scheduled.config.scheduled.ScheduledRunnable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author ManJiis
 * @since 2021-07-23
 * @since jdk1.8
 */
@Component
public class JobService {
    private static final Logger log = LoggerFactory.getLogger(JobService.class);

    @Autowired
    private ToolJobMapper toolJobMapper;

    @Autowired
    private ScheduledTaskRegistrar scheduledTaskRegistrar;

    public boolean addJob(ToolJobReq job) {
        log.info("addJob(): {}", JSON.toJSONString(job));

        ToolJob toolJob = toolJobMapper.selectOne(new QueryWrapper<ToolJob>()
                .eq("job_class_name", job.getJobClassName())
                .eq("job_method_name", job.getJobMethodName()));

        if (toolJob != null) {
            return false;
        }
        ToolJob addToolJob = new ToolJob();
        BeanUtils.copyProperties(job, addToolJob);
        String jobId = System.currentTimeMillis() + String.format("%04d", ThreadLocalRandom.current().nextInt(9999));
        addToolJob.setJobId(jobId);
        int insert = toolJobMapper.insert(addToolJob);
        if (!(insert > 0)) {
            return false;
        }
        if (job.getJobStatus().equals(ScheduledContains.JOB_STATUS_NORMAL)) {
            ScheduledRunnable task = new ScheduledRunnable(addToolJob);
//            scheduledTaskRegistrar.addCronTask(task, job.getCron());
            scheduledTaskRegistrar.addCronTask(addToolJob, task, job.getCron());
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

    public List<ToolJob> listNormalStatusJob() {
        return scheduledTaskRegistrar.getNormalStatusJobList();
    }
}
