package top.b0x0.scheduled.config.scheduled;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import top.b0x0.scheduled.common.ScheduledContains;
import top.b0x0.scheduled.common.SpringContextUtils;
import top.b0x0.scheduled.enity.ToolJob;
import top.b0x0.scheduled.mapper.ToolJobMapper;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义ScheduledRunnable
 *
 * @author ManJiis
 * @since 2021-07-23
 */
@Data
@NoArgsConstructor
@Component
//@Scope(value = "prototype")
public class ScheduledRunnable implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ScheduledRunnable.class);

    @Autowired
    ToolJobMapper toolJobMapper;

    private static ScheduledRunnable scheduledRunnable;

    /**
     * 实现Runnable接口后，直接注入ToolJobMapper空指针，使用此方式重新注入
     * (此句暂时划掉)方式二： 使用多例 创建的时候使用ApplicationContext创建
     */
    @PostConstruct
    public void init() {
        scheduledRunnable = this;
        scheduledRunnable.toolJobMapper = this.toolJobMapper;
    }

//    @Resource
//    ToolJobMapper toolJobMapper;

    private ToolJob toolJob;

    public ScheduledRunnable(ToolJob toolJob) {
        this.toolJob = toolJob;
    }


    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        log.info("定时任务开始执行 - jobId：{} ,cron: {}", toolJob.getJobId(), toolJob.getCron());
        String jobId = toolJob.getJobId();
        String jobBeanName = toolJob.getJobBeanName();
        String jobMethodName = toolJob.getJobMethodName();
        String jobClassName = toolJob.getJobClassName();
        String jobMethodParams = toolJob.getJobMethodParams();

        ToolJobMapper toolJobMapper = scheduledRunnable.toolJobMapper; // 此方法可用

//        System.out.println("toolJobMapper = " + toolJobMapper);

        ToolJob toolJob = toolJobMapper.selectById(jobId);

        if (toolJob == null) {
            log.info("未找到此定时任务 - jobId：{}", jobId);
            return;
        }
        if (toolJob.getJobExecStatus().equals(ScheduledContains.JOB_EXEC_STATUS_RUNNING)) {
            log.info("此定时任务已被其他机器执行 - jobId：{}", jobId);
            return;
        }
        if (toolJob.getJobStatus().equals(ScheduledContains.JOB_STATUS_DISABLE)) {
            log.info("此定时任务已被暂停 - jobId：{}", jobId);
            return;
        }

        try {
            Object target = SpringContextUtils.getBean(jobBeanName);

            Method method = null;
            if (StringUtils.isNotEmpty(jobMethodParams)) {
                // TODO 如何判断复杂参数的方法
                method = target.getClass().getDeclaredMethod(jobMethodName, String.class);
            } else {
                method = target.getClass().getDeclaredMethod(jobMethodName);
            }

            ReflectionUtils.makeAccessible(method);
            if (StringUtils.isNotEmpty(jobMethodParams)) {
                method.invoke(target, jobMethodParams);
            } else {
                method.invoke(target);
            }
        } catch (Exception ex) {
            log.error("定时任务执行异常 - {} ", JSON.toJSONString(toolJob), ex);
        }

        ToolJob updateJob = new ToolJob();
        updateJob.setJobId(toolJob.getJobId());
        updateJob.setJobExecStatus(ScheduledContains.JOB_EXEC_STATUS_STOP);
        updateJob.setLastExecTime(LocalDateTime.now());
        toolJobMapper.updateById(updateJob);

        long times = System.currentTimeMillis() - startTime;
        log.info("定时任务执行结束 - jobId：{}，cron: {} , 耗时：{} 毫秒", toolJob.getJobId(), toolJob.getCron(), times);
    }


}