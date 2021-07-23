package top.b0x0.scheduled.config.taskdynamic;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import top.b0x0.scheduled.common.ScheduledContains;
import top.b0x0.scheduled.common.SpringContextUtils;
import top.b0x0.scheduled.enity.ToolJob;
import top.b0x0.scheduled.mapper.ToolJobMapper;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

/**
 * 自定义ScheduledRunnable
 *
 * @author ManJiis
 * @since 2021-07-23
 */
@Data
@Component
public class ScheduledRunnable implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ScheduledRunnable.class);

    private static ScheduledRunnable scheduledRunnable;

    @Autowired
    ToolJobMapper toolJobMapper;

    /**
     * 实现Runnable接口后，直接注入ToolJobMapper空指针，使用此方式重新注入
     */
    @PostConstruct
    public void init() {
        scheduledRunnable = this;
        scheduledRunnable.toolJobMapper = this.toolJobMapper;
    }

//    @Resource
//    ToolJobMapper toolJobMapper;

    private String jobId;
    private String beanName;
    private String methodName;
    private String methodParams;
    private String cron;

    public ScheduledRunnable() {
    }

    public ScheduledRunnable(ToolJob toolJob) {
        this.jobId = toolJob.getJobId();
        this.beanName = toolJob.getJobBeanName();
        this.methodName = toolJob.getJobMethodName();
        this.methodParams = toolJob.getJobMethodParams();
        this.cron = toolJob.getCron();
    }

    public ScheduledRunnable(String beanName, String methodName, String methodParams, String cron) {
        this.beanName = beanName;
        this.methodName = methodName;
        this.methodParams = methodParams;
        this.cron = cron;
    }

    @Override
    public void run() {
        log.info("定时任务开始执行 - jobId：{}，cron：{}，bean：{}，方法：{}，参数：{}", jobId, cron, beanName, methodName, methodParams);
        long startTime = System.currentTimeMillis();

//        ToolJobMapper toolJobMapper = SpringContextUtils.getBean(ToolJobMapper.class);

        ToolJobMapper toolJobMapper = scheduledRunnable.toolJobMapper; // 此方法可用

        System.out.println("toolJobMapper = " + toolJobMapper);

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
            log.info("此定时任务已暂停 - jobId：{}", jobId);
            return;
        }

        try {
            Object target = SpringContextUtils.getBean(beanName);

            Method method = null;
            if (StringUtils.isNotEmpty(methodParams)) {
                method = target.getClass().getDeclaredMethod(methodName, String.class);
            } else {
                method = target.getClass().getDeclaredMethod(methodName);
            }

            ReflectionUtils.makeAccessible(method);
            if (StringUtils.isNotEmpty(methodParams)) {
                method.invoke(target, methodParams);
            } else {
                method.invoke(target);
            }
        } catch (Exception ex) {
            log.error(String.format("定时任务执行异常 - jobId：%s，bean：%s，方法：%s，参数：%s ", jobId, beanName, methodName, methodParams), ex);
        }

/*
        ToolJob updateJob = new ToolJob();
        updateJob.setJobId(toolJob.getJobId());
        updateJob.setJobExecStatus(ScheduledContains.JOB_EXEC_STATUS_STOP);
        updateJob.setLastExecTime(LocalDateTime.now());
        toolJobMapper.updateById(updateJob);
*/

        long times = System.currentTimeMillis() - startTime;
        log.info("定时任务执行结束 - jobId：{}，bean：{}，方法：{}，参数：{}，耗时：{} 毫秒", jobId, beanName, methodName, methodParams, times);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScheduledRunnable that = (ScheduledRunnable) o;

        if (beanName != null ? !beanName.equals(that.beanName) : that.beanName != null) return false;
        if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) return false;
        if (methodParams != null ? !methodParams.equals(that.methodParams) : that.methodParams != null) return false;
        return cron != null ? cron.equals(that.cron) : that.cron == null;
    }

    @Override
    public int hashCode() {
        int result = beanName != null ? beanName.hashCode() : 0;
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        result = 31 * result + (methodParams != null ? methodParams.hashCode() : 0);
        result = 31 * result + (cron != null ? cron.hashCode() : 0);
        return result;
    }

}