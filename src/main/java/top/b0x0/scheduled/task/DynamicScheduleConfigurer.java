package top.b0x0.scheduled.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.StringUtils;
import top.b0x0.scheduled.mapper.ToolJobMapper;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 动态定时任务配置类
 *
 * @author ManJiis
 * @since 2021-07-22
 **/
@Configuration
public class DynamicScheduleConfigurer implements SchedulingConfigurer {

//    @Mapper
//    public interface CronMapper {
//        @Select("select cron from cron limit 1")
//        public String getCron();
//    }

    //注入mapper
    @Autowired
    @SuppressWarnings("all")
    ToolJobMapper toolJobMapper;

    @Autowired
    @Qualifier("myThreadPoolTaskScheduler")
    private TaskScheduler myThreadPoolTaskScheduler;

    /**
     * 执行定时任务.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        taskRegistrar.setTaskScheduler(myThreadPoolTaskScheduler);

        // addTriggerTask(Runnable task, Trigger trigger)

        taskRegistrar.addTriggerTask(

                // 1.添加任务内容(Runnable)
                this::task1,

                // 2.设置执行周期(Trigger)
                triggerContext -> {

                    // 2.1 从数据库获取执行周期
                    String cron = toolJobMapper.getCron();

                    // 2.2 合法性校验.
                    if (StringUtils.isEmpty(cron)) {
                        // TODO Omitted Code ..
                    }
                    // 2.3 返回执行周期(Date)
                    Date nextExecutionTime = new CronTrigger(cron).nextExecutionTime(triggerContext);
                    return nextExecutionTime;
                }
        );
    }

    public void task1() {
        System.out.println(MyTask.getThreadName() + "  DynamicScheduleConfigurer.configureTasks() 现在是: " + LocalDateTime.now().toLocalTime());
    }
}