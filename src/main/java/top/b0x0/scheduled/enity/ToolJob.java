package top.b0x0.scheduled.enity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ManJiis
 * @since 2021-07-23
 * @since JDK1.8
 */
@Data
@NoArgsConstructor
@TableName("tool_job")
public class ToolJob implements Serializable {
    private static final long serialVersionUID = 205703633626530121L;

    /**
     * 任务ID
     */
    @TableId(value = "job_id", type = IdType.ID_WORKER_STR)
    private String jobId;

    /**
     * cron表达式
     */
    private String cron;

    /**
     * job名称
     */
    private String jobName;
    private String jobBeanName;
    private String jobClassName;
    private String jobMethodName;
    private String jobMethodParams;

    /**
     * 状态（1正常 0暂停）
     */
    private Integer jobStatus;
    /**
     * 任务执行状态（ 0已停止 1执行中 ）
     */
    private Integer jobExecStatus;
    /**
     * 最近一次任务执行时间
     */
    private LocalDateTime lastExecTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    private String createBy;
    private String updateBy;


}