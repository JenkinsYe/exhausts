package com.csdn.xs.exhausts.controller;

import com.csdn.xs.exhausts.domain.StatisticDomain;
import com.csdn.xs.exhausts.response.Result;
import com.csdn.xs.exhausts.service.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author YJJ
 * @Date: Created in 09:51 2020-06-16
 */
@RestController
@Slf4j
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    /**
     * 根据免检名单期号获取免检/非免检统计值
     * @param version
     * @return
     */
    @GetMapping("/api/nameList/statistic")
    public Result findNameListAverageData(@RequestParam(required = false) Long version) {
        log.info("获取第{}期免检/非免检/遥测平均值", version);
        StatisticDomain domain = statisticService.findStatisticByVersion(version);
        HashMap<String, Object> map = new HashMap<>();
        if (domain != null) {
            map.put("wc_co", domain.getWcCO());
            map.put("wc_no", domain.getWcNO());
            map.put("wc_hc", domain.getWcHC());
            map.put("c_co", domain.getCCO());
            map.put("c_no", domain.getCNO());
            map.put("c_hc", domain.getCHC());
            map.put("wc_rae", domain.getWcRate());
            map.put("start_time", domain.getRsStartTime());
            map.put("end_time", domain.getRsEndTime());

            return new Result().success(map);
        } else {
            return new Result().fail("查询为空");
        }
    }


    /**
     * 节省时间、费用接口
     * @return
     */
    @GetMapping("/api/costsaving")
    public Result getTotalCostSaving() {
        log.info("获取节省时间、费用累计值");
        HashMap<String, Object> map = new HashMap<>();
        map.put("money_cost_saving", statisticService.getMoneySaving());
        map.put("time_cost_saving", statisticService.getTimeSaving());

        return new Result().success(map);
    }

    @GetMapping("/api/road/pollution")
    public Result getCurrentRoadPollution() {
        return new Result();
    }
}
