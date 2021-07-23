package top.b0x0.scheduled.config.scheduled;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import top.b0x0.scheduled.common.SpringContextUtils;
import top.b0x0.scheduled.enity.ToolJob;
import top.b0x0.scheduled.mapper.ToolJobMapper;

import java.util.List;

/**
 * 项目启动时加载数据库正常状态下的定时任务
 *
 * @author ManJiis
 * @since 2021-07-23
 */
@Component
public class JobRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(JobRunner.class);

    @Autowired
    private ToolJobMapper toolJobMapper;

    @Autowired
    private ScheduledTaskRegistrar scheduledTaskRegistrar;

    @Override
    public void run(String... args) {
        // 初始加载数据库里状态为正常的定时任务
        List<ToolJob> jobList = toolJobMapper.selectList(new QueryWrapper<ToolJob>().eq("job_status", 1));

        if (jobList != null && jobList.size() > 0) {
            for (ToolJob job : jobList) {
                ScheduledRunnable task = new ScheduledRunnable(job);
//                ScheduledRunnable task = SpringContextUtils.getBean(ScheduledRunnable.class);
//                scheduledTaskRegistrar.addCronTask(task, job.getCron());
                task.setToolJob(job);
                scheduledTaskRegistrar.addCronTask(job, task, job.getCron());
            }
        }
        log.info("定时任务已加载完毕...");
    }
}