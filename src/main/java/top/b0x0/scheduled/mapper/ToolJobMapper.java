package top.b0x0.scheduled.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import top.b0x0.scheduled.enity.ToolJob;

public interface ToolJobMapper extends BaseMapper<ToolJob> {
    @Select("select cron from tool_job limit 1")
    String getCron();
}