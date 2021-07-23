package top.b0x0.scheduled.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 定时任务线程池（可并行处理定时任务）
 *
 * @author ManJiis
 * @since 2021-07-23
 */
@Configuration
public class ScheduledConfig implements SchedulingConfigurer {

    @Autowired
    @Qualifier("myThreadPoolTaskScheduler")
    private TaskScheduler myThreadPoolTaskScheduler;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setTaskScheduler(myThreadPoolTaskScheduler);
    }


    @Bean(name = "myThreadPoolTaskScheduler")
    public TaskScheduler myThreadPoolTaskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.setThreadNamePrefix("Dynamic-Scheduled-");
        taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //调度器shutdown被调用时等待当前被调度的任务完成
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        //等待时长
        taskScheduler.setAwaitTerminationSeconds(60);
        return taskScheduler;
    }
}