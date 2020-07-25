package com.csdn.xs.exhausts.schedule;

import com.csdn.xs.exhausts.service.AutomationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 定时任务调度
 * @author YJJ
 * @Date: Created in 11:01 2019-12-09
 */
@Configuration
@EnableScheduling
@Slf4j
public class ScheduleConfig {

    @Autowired
    private AutomationService automationService;

    //@Scheduled(cron = "0 0/10 * * * ?")
    public void autoOptimizeProcess() {
        log.info("---------------------------");
        log.info("New Automation Optimize Work Start");

        automationService.startOptimizeWork();

        log.info("Automation Work Optimize End.");
        log.info("---------------------------");

    }

    //@Scheduled(cron = "0 0 0 0/2 * ?")
    public void autoAssessProcess() {
        log.info("---------------------------");
        log.info("New Automation Assess Work Start");

        automationService.startOptimizeWork();
        automationService.startAssessWork();

        log.info("Automation Work Assess End.");
        log.info("---------------------------");
    }


}
