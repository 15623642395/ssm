package webProject.emp.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import webProject.emp.quartz.QuartzJob;
import webProject.emp.quartz.ScheduleJob;
import webProject.responseData.ResponseData;

/**
 * 1、实现定时任务时间根据前台用户设置的时间开启
 * 2、能暂停、重启、删除定时任务
 * 
 * @author 56525
 *
 */
@Controller
@RequestMapping("/quartz")
public class SpringQuartzContorller {

	Logger logger = LogManager.getLogger(SpringQuartzContorller.class.getName());
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	/**
	 * 跳转用户登陆页面 发送请求
	 * 
	 * http://localhost:8080/ssm/quartz/quartzLogin.do
	 * 
	 * @return
	 */
	@RequestMapping("/quartzLogin.do")
	public String welcome() {
		logger.info("进入定时任务设置页面");
		return "quartz";// （直接return:"abc"）的是转发到页面
	}

	/**
	 * 任务创建与更新(未存在的就创建，已存在的则更新)
	 * @param request
	 * @param response
	 * @param scheduleJob
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/quartzStart.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData updateQuartz(@RequestBody ScheduleJob job) {
		logger.info("定时任务开始");
		ResponseData responseData = null;
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			logger.info(job);
			if (null != job) {
				// 获取触发器标识
				TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
				// 获取触发器trigger
				CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
				if (null == trigger) {// 不存在任务
					// 创建任务
					JobDetail jobDetail = JobBuilder.newJob(QuartzJob.class)
							.withIdentity(job.getJobName(), job.getJobGroup()).build();
					jobDetail.getJobDataMap().put("scheduleJob", job);
					// 表达式调度构建器
					CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
					// 按新的cronExpression表达式构建一个新的trigger
					trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup())
							.withSchedule(scheduleBuilder).build();
					scheduler.scheduleJob(jobDetail, trigger);
					// 把任务插入数据库,再次进入时返给前台
					// quartzBS.add(job);

				} else {// 存在任务
					// Trigger已存在，那么更新相应的定时设置
					// 表达式调度构建器
					CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
					// 按新的cronExpression表达式重新构建trigger
					trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder)
							.build();
					// 按新的trigger重新设置job执行
					scheduler.rescheduleJob(triggerKey, trigger);
					// 更新数据库中的任务
					// quartzBS.update(job);
				}
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		responseData = new ResponseData();
		return responseData;
	}

	/**
	 * 暂停任务
	 * @param request
	 * @param response
	 * @param job
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/quartzStop.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData pauseQuartz(@RequestBody ScheduleJob scheduleJob) {
		ResponseData responseData = null;
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		try {
			scheduler.pauseJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		responseData = new ResponseData();
		return responseData;
	}

	/**
	 * 恢复任务
	 * @param request
	 * @param response
	 * @param scheduleJob
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/quartzRestart.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData resumeQuartz(@RequestBody ScheduleJob scheduleJob, ModelMap model) {
		ResponseData responseData = null;
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		try {
			scheduler.resumeJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		responseData = new ResponseData();
		return responseData;
	}

	/**
	 * 删除任务
	 * @param request
	 * @param response
	 * @param scheduleJob
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/quartzDelete.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData deleteQuartz(@RequestBody ScheduleJob scheduleJob) {
		ResponseData responseData = null;
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		try {
			scheduler.deleteJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		responseData = new ResponseData();
		return responseData;
	}
}
