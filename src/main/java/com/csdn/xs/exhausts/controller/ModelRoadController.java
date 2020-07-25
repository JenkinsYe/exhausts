package com.csdn.xs.exhausts.controller;

import com.csdn.xs.exhausts.domain.ModelRoadDomain;
import com.csdn.xs.exhausts.response.Result;
import com.csdn.xs.exhausts.service.ModelRoadDataService;
import com.csdn.xs.exhausts.utils.ConstantUtils;
import com.csdn.xs.exhausts.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 16:33 2020-06-10
 */
@Slf4j
@RestController
public class ModelRoadController {

    @Autowired
    ModelRoadDataService modelRoadDataService;

    /**
     * 实时污染物接口
     * @param vehicleType
     * @return
     */
    @GetMapping("/api/modelRoad/realtime/pollution")
    public Result getModelRoadByTime(@RequestParam(value = "vehicleType", required = false) String vehicleType) {
        log.info("实时污染物查询，车辆类型: " + vehicleType);
        HashMap<String, Object> map = new HashMap<>();
        List<ModelRoadDomain> domains = modelRoadDataService.findNewestModelRoad(vehicleType);

        map.put("size", domains.size());
        map.put("dataList", domains);
        return new Result().success(map);
    }

    /**
     * 单一车型污染物查询
     * pollutionType为空时，查询全道路全污染物浓度
     * 不为空时，查询具体污染物全道路排名
     * @param dateString
     * @param vehicleType
     * @param pollutionType
     * @return
     */
    @GetMapping("/api/modelRoad/pollution")
    public Result getModelRoadPollution(@RequestParam(value = "time", required = true) String dateString,
                                        @RequestParam(value = "vehicleType", required = true) String vehicleType,
                                        @RequestParam(value = "order", required = false) String pollutionType) {
        if (dateString == null || vehicleType == null) {
            return new Result().fail("非可选参数为空");
        }
        Date date;
        try {
            date = DateUtils.dateStringToDate(dateString, "yyyy-MM-dd HH");
        } catch (ParseException e) {
            e.printStackTrace();
            return new Result().fail("日期无法解析");
        }

        // 获取该车型所有道路所有尾气污染量
        if (pollutionType == null || "".equals(pollutionType)) {
            log.info("全道路尾气查询,时间{},查询车型{}", dateString, vehicleType);
            List<ModelRoadDomain> list = modelRoadDataService.findModelRoadByTimeAndVehicle(date, vehicleType);
            HashMap<String, Object> map = new HashMap<>();
            map.put("size", list.size());
            map.put("dataList", list);
            if (list.size() != 0)
                return new Result().success(map);
            else {
                Result result = new Result();
                result.setMsg("该时间暂无数据");
                return result;
            }
        }

        // 获取该污染物该车型所有道路排名
        if ("HC".equals(pollutionType) || "CO".equals(pollutionType) || "NOx".equals(pollutionType) || "SO2".equals(pollutionType)) {
            log.info("时间:{}, 车型:{} 全道路尾气排名, 按照{}排名", dateString, vehicleType, pollutionType);
            List<ModelRoadDomain> list = modelRoadDataService.findModelRoadOrderByPollutionType(date, vehicleType, pollutionType);
            if (list.size() == 0) {
                return new Result().fail("当前查询无数据");
            }
            List<Order> orders = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Order order = new Order();
                ModelRoadDomain domain = list.get(i);
                order.rank = i + 1;
                order.channelID = domain.getChannelID();
                switch (pollutionType) {
                    case "HC" : order.value = domain.getHc(); break;
                    case "CO" : order.value = domain.getCo(); break;
                    case "Nox" : order.value = domain.getNox(); break;
                    case "SO2" : order.value = domain.getSo2();
                }
                orders.add(order);
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("size", orders.size());
            map.put("time", date);
            map.put("order_by", pollutionType);
            map.put("dataList", orders);

            return new Result().success(map);
        } else {
            return new Result().fail("当前污染物不支持排名");
        }
    }

    class Order {
        public int rank;
        public String channelID;
        public double value;
    }
}
