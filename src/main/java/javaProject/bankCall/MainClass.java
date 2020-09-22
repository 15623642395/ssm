package javaProject.bankCall;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 主启动类
 * 	1、客户叫号
 * 	2、窗口处理业务
 * @author 56525
 *
 */
public class MainClass {

	public static void main(String[] args) {

		// 采用线程池的定时任务为普通客户拿号
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
			public void run() {
				Integer serviceNumber = NumberMachine.getInstance().getCommonManager().generateNewNumber();
				System.out.println("第" + serviceNumber + "号普通客户正在等待服务！");
			}
		}, 0, Constants.COMMON_CUSTOMER_INTERVAL_TIME, TimeUnit.SECONDS);

		// 采用线程池的定时任务为快速客户拿号
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
			public void run() {
				Integer serviceNumber = NumberMachine.getInstance().getExpressManager().generateNewNumber();
				System.out.println("第" + serviceNumber + "号快速客户正在等待服务！");
			}
		}, 0, Constants.COMMON_CUSTOMER_INTERVAL_TIME * 2, TimeUnit.SECONDS);

		// 采用线程池的定时任务为VIP客户拿号
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
			public void run() {
				Integer serviceNumber = NumberMachine.getInstance().getVipManager().generateNewNumber();
				System.out.println("第" + serviceNumber + "号VIP客户正在等待服务！");
			}
		}, 0, Constants.COMMON_CUSTOMER_INTERVAL_TIME * 6, TimeUnit.SECONDS);

		// 产生4个普通窗口，为客户服务
		for (int i = 1; i < 5; i++) {
			ServiceWindow window = new ServiceWindow();
			window.setNumber(i);
			window.start();
		}

		// 产生1个快速窗口，为客户服务
		ServiceWindow expressWindow = new ServiceWindow();
		expressWindow.setType(CustomerType.EXPRESS);
		expressWindow.start();

		// 产生1个VIP窗口，为客户服务
		ServiceWindow vipWindow = new ServiceWindow();
		vipWindow.setType(CustomerType.VIP);
		vipWindow.start();
	}
}
