package webProject.emp.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
/**
 * 定时任务开启后执行定时任务逻辑
 * @author 56525
 *
 */
@Component
public class QuartzJob implements Job {
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("任务成功运行");
		ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
		System.out.println("任务名称 = [" + scheduleJob.getJobName() + "]");
		// 根据name 与 group组成的唯一标识来判别该执行何种操作……
	}
}