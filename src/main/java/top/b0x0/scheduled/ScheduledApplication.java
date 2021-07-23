package top.b0x0.scheduled;

import org.mapstruct.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * http://localhost:9099/doc.html
 *
 * @author ManJiis
 * @since 2021-07-22
 */
@EnableScheduling //开启定时任务
@SpringBootApplication
@MapperScan("top.b0x0.scheduled.mapper")
public class ScheduledApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduledApplication.class, args);
    }

}
