package com.csdn.xs.exhausts.controller;

import com.csdn.xs.exhausts.domain.ModelRoadDomain;
import com.csdn.xs.exhausts.response.Result;
import com.csdn.xs.exhausts.service.ModelRoadDataService;
import com.csdn.xs.exhausts.utils.ConstantUtils;
import com.csdn.xs.exhausts.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.*;

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

    @GetMapping("/api/modelRoad/pollution/total/order")
    public Result getModelRoadPollutionOrderByTimeInternal(@RequestParam("start") String startString,
                                                           @RequestParam("end") String endString,
                                                           @RequestParam("order") String pollutionType) {
        Date start, end;
        Result result = new Result();
        try {
            start = DateUtils.dateStringToDate(startString, "yyyy-MM-dd HH");
            end = DateUtils.dateStringToDate(endString, "yyyy-MM-dd HH");

            List<ModelRoadDomain> domains = modelRoadDataService.findModelRoadByTimeInternal(start, end);
            HashMap<String, Double> map = new HashMap<>();
            for (ModelRoadDomain domain : domains) {
                if ("HC".equals(pollutionType)) {
                    if (map.containsKey(domain.getChannelID())) {
                        map.put(domain.getChannelID(), map.get(domain.getChannelID()) + domain.getHc());
                    } else {
                        map.put(domain.getChannelID(), domain.getHc());
                    }
                } else if ("CO".equals(pollutionType)) {
                    if (map.containsKey(domain.getChannelID())) {
                        map.put(domain.getChannelID(), map.get(domain.getChannelID()) + domain.getCo());
                    } else {
                        map.put(domain.getChannelID(), domain.getCo());
                    }
                } else if ("NOx".equals(pollutionType)) {
                    if (map.containsKey(domain.getChannelID())) {
                        map.put(domain.getChannelID(), map.get(domain.getChannelID()) + domain.getNox());
                    } else {
                        map.put(domain.getChannelID(), domain.getNox());
                    }
                } else if ("SO2".equals(pollutionType)) {
                    if (map.containsKey(domain.getChannelID())) {
                        map.put(domain.getChannelID(), map.get(domain.getChannelID()) + domain.getSo2());
                    } else {
                        map.put(domain.getChannelID(), domain.getSo2());
                    }
                }
            }

            List<Order> tempOrder = new ArrayList<>();
            for (String key: map.keySet()) {
                tempOrder.add(new Order(0, key, map.get(key)));
            }

            tempOrder.sort(new Comparator<Order>() {
                @Override
                public int compare(Order o1, Order o2) {
                    if (o1.value < o2.value)
                        return 1;
                    return -1;
                }
            });
            for (int i = 0; i < tempOrder.size(); i++) {
                tempOrder.get(i).rank = i+1;
            }


            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("size", tempOrder.size());
            resultMap.put("startTime", start);
            resultMap.put("endTime", end);
            resultMap.put("order_by", pollutionType);
            resultMap.put("dataList", tempOrder);

            return result.success(resultMap);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Result().fail("日期无法解析");
        }
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

    @GetMapping("/api/modelRoad/realtime/pollution/total")
    public Result getModelRoadTotalRealtimePollutionByVehicle(@RequestParam(value = "vehicleType", required = true) String vehicleType) {
        log.info("车型最新全域污染物查询， 车型{}", vehicleType);
        Result result = new Result();
        List<ModelRoadDomain> domains = modelRoadDataService.findNewestModelRoad(vehicleType);
        Double totalHC, totalCO, totalNOx, totalSO2, totalNO, totalNO2, totalPM25, totalPM10;
        totalHC = totalCO = totalNOx = totalSO2 = totalNO = totalNO2 = totalPM25 = totalPM10 = 0d;
        for (ModelRoadDomain domain : domains) {
            totalHC += domain.getHc();
            totalCO += domain.getCo();
            totalNOx += domain.getNox();
            totalSO2 += domain.getSo2();
            totalNO += domain.getNo();
            totalNO2 += domain.getNo2();
            totalPM25 += domain.getPm25();
            totalPM10 += domain.getPm10();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("hc", totalHC);
        map.put("co", totalCO);
        map.put("nox", totalNOx);
        map.put("so2", totalSO2);
        map.put("no", totalNO);
        map.put("no2", totalNO2);
        map.put("pm25", totalPM25);
        map.put("pm10", totalPM10);
        return result.success(map);
    }

    class Order {
        public int rank;
        public String channelID;
        public double value;

        public Order(int rank, String channelID, double value) {
            this.rank = rank;
            this.channelID = channelID;
            this.value = value;
        }

        public Order(){}
    }
}
