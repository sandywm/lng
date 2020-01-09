package com.lng.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lng.tools.ContantsProperties;
import com.lng.tools.CurrentTime;
import com.lng.tools.FileOpration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
  * 定时器执行
 * @author Administrator
 *
 */
@Component////把普通pojo实例化到spring容器中
@Async//每一个任务都是在不同的线程中
public class ScheduledService {
	
	private static Logger logger = LoggerFactory.getLogger(ScheduledService.class);
	
	@Autowired
	private ContantsProperties cp;
	
	/**
	 * @description 定时器一。每天凌晨2:30分自动删除临时文件夹图片
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2020年1月9日 上午8:57:22
	 */
	@Scheduled(cron = "0 30 2 * * *")//秒 分 时 日 月 年
	 public void scheduled(){
		logger.info("自动删除临时文件任务开始："+CurrentTime.getCurrentTime());
		FileOpration.deleteAllFile(cp.getWeburl()+"temp");
		logger.info("自动删除临时文件任务结束："+CurrentTime.getCurrentTime());
	}
	
	/**
	 * @description 定时器二
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2020年1月9日 上午8:57:12
	 */
//	@Scheduled(cron = "2 31 11 * * *")
//	 public void scheduled1(){
//		for(int i = 20000000 ; i < 30000000 ; i++) {
//			System.out.println("定时任务二开始!!!!!!!"+i);
//		}
//	}
}
