package top.b0x0.scheduled.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.b0x0.scheduled.common.R;
import top.b0x0.scheduled.enity.ToolJob;
import top.b0x0.scheduled.service.JobService;

/**
 * job
 *
 * @author ManJiis
 * @since 2021-07-23
 * @since jdk1.8
 */
@RestController
@RequestMapping("job")
@Api(tags = "JOB")
public class JobController {

    @Autowired
    JobService jobService;

    @PostMapping("add")
    @ApiOperation("新增")
    public R addJob(ToolJob job) {
        return R.ok(jobService.addJob(job));
    }

    @PostMapping("edit")
    @ApiOperation("编辑")
    public R editJob(@RequestBody ToolJob job) {
        return R.ok(jobService.editJob(job));
    }

    @DeleteMapping("status")
    @ApiOperation("启动/暂停")
    public R statusJob(ToolJob job) {
        return R.ok(jobService.statusJob(job));
    }

    @DeleteMapping("delete")
    @ApiOperation("删除")
    public R deleteJob(ToolJob job) {
        return R.ok(jobService.deleteJob(job));
    }

    @GetMapping("list")
    @ApiOperation("列表")
    public R listJob() {
        return R.ok(jobService.listJob());
    }

    @GetMapping("listForRunning")
    @ApiOperation("正常状态的定时任务")
    public R listJobForRunning() {
        return R.ok(jobService.listJobForRunning());
    }
}
