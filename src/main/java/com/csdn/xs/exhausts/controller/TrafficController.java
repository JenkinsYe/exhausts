package com.csdn.xs.exhausts.controller;

import com.csdn.xs.exhausts.domain.TrafficDomain;
import com.csdn.xs.exhausts.response.Result;
import com.csdn.xs.exhausts.service.TrafficDataService;
import com.csdn.xs.exhausts.service.TrafficStatisticService;
import com.csdn.xs.exhausts.utils.ConstantUtils;
import com.csdn.xs.exhausts.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.*;

/**
 * @author YJJ
 * @Date: Created in 10:44 2020-06-17
 */
@RestController
@Slf4j
public class TrafficController {

    @Autowired
    private TrafficDataService trafficDataService;

    @Autowired
    private TrafficStatisticService trafficStatisticService;

    /**
     * 获取车型对照表
     * @return
     */
    @GetMapping("/api/vehicleList")
    public Result getVehicleTypeList() {
        return new Result().success(ConstantUtils.vehicleMap);
    }

    /**
     * 交通流接口
     * @param startString
     * @param endString
     * @param channelID
     * @param vehicleType
     * @return
     */
    @GetMapping("/api/traffic/flow")
    public Result getTrafficFlowByTimeAndChannelID(@RequestParam("start") String startString, @RequestParam("end") String endString,
                                                   @RequestParam("channelID") String channelID, @RequestParam("vehicleType") String vehicleType) {

        if (startString == null || endString == null || channelID == null || vehicleType == null)
            return new Result().fail("参数不可为空");
        log.info("实时车流量, 开始时间{}, 结束时间{}, 通道编号{}, 车辆类型{}", startString, endString, channelID, vehicleType);
        Result result = new Result();
        Date start;
        Date end;
        try {
            start = DateUtils.dateStringToDate(startString, "yyyy-MM-dd HH:mm:ss");
            end = DateUtils.dateStringToDate(endString, "yyyy-MM-dd HH:mm:ss");
            HashMap<String, Object> map = new HashMap<>();
            map.put("flow", trafficDataService.getFlowByTimeAndChannelAndType(start, end, channelID, vehicleType));
            return new Result().success(map);
        } catch (ParseException e) {
            e.printStackTrace();
            result.fail();
            result.setMsg("日期无法解析");
            return result;
        }
    }

    /**
     * 车型百分比接口
     * @param dateString
     * @return
     */
    @GetMapping("/api/traffic/typePercentage")
    public Result getEachVehicleTypePercentageForAllChannel(@RequestParam("time") String dateString) {
        if (dateString == null) {
            return new Result().fail("参数不可为空");
        }
        log.info("全道路尾气污染物百分比, 时间{}", dateString);
        Result result = new Result();
        try {
            Date date = DateUtils.dateStringToDate(dateString, "yyyy-MM-dd HH");
            HashMap<String, Object> map = new HashMap<>();
            map.put("dataList", trafficStatisticService.getVehiclePercentage(dateString));
            return new Result().success(map);
        } catch (ParseException e) {
            e.printStackTrace();
            result.fail();
            result.setMsg("日期无法解析");
            return result;
        }
    }

    /**
     * 道理车流量排名接口
     * @param dateString
     * @return
     */
    @GetMapping("/api/traffic/flowOrder")
    public Result getFlowOrderByTime(@RequestParam("time") String dateString) {
        log.info("获取车流量排名，时间{}", dateString);
        if (dateString == null) {
            return new Result().fail("参数不可为空");
        }
        Result result = new Result();
        try {
            Date date = DateUtils.dateStringToDate(dateString, "yyyy-MM-dd HH");
            HashMap<String, Object> map = new HashMap<>();
            List<TrafficDomain> list = trafficDataService.findTrafficByTimeString(dateString);
            list.sort((o1, o2) -> o2.getFlow() - o1.getFlow());
            List<FlowOrder> orders = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                FlowOrder order = new FlowOrder();
                order.rank = i + 1;
                order.channelID = list.get(i).getChannelID();
                order.flow = list.get(i).getFlow();
                orders.add(order);
            }
            map.put("dataList", orders);
            map.put("time", date);
            return new Result().success(map);
        } catch (ParseException e) {
            e.printStackTrace();
            result.fail();
            result.setMsg("日期无法解析");
            return result;
        }
    }

    class FlowOrder {
        public String channelID;
        public int rank;
        public int flow;
    }
}
