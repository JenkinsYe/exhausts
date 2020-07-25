package com.csdn.xs.exhausts.controller;

import com.csdn.xs.exhausts.response.Result;
import com.csdn.xs.exhausts.service.DataService;
import com.csdn.xs.exhausts.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author YJJ
 * @Date: Created in 14:26 2020-06-29
 */
@RestController
public class MeasurementController {

    @Autowired
    private DataService dataService;

    @GetMapping("/api/measurement/distance")
    public Result findVehicleDistributionByDistance() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("<1w", dataService.findResultCountByDistanceInternal(0l, 10000l));
        map.put("1~10w", dataService.findResultCountByDistanceInternal(10001l, 100000l));
        map.put("10~20w", dataService.findResultCountByDistanceInternal(100001l,200000l));
        map.put("20~30w", dataService.findResultCountByDistanceInternal(200001l,300000l));
        map.put("30~40w", dataService.findResultCountByDistanceInternal(300001l,400000l));
        map.put("40~50w", dataService.findResultCountByDistanceInternal(400001l,500000l));
        map.put("50~100w", dataService.findResultCountByDistanceInternal(500001l,1000000l));
        map.put("100~200w", dataService.findResultCountByDistanceInternal(1000001l,2000000l));
        map.put(">200w", dataService.findResultCountByDistanceInternal(2000001l, Long.MAX_VALUE));
        return new Result().success(map);
    }

    @GetMapping("/api/measurement/year")
    public Result findVehicleDistributionByMeasureDate() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("6", dataService.findResultCountByYears(0, 6));
        map.put("6~7", dataService.findResultCountByYears(6, 7));
        map.put("7~8", dataService.findResultCountByYears(7,8));
        map.put("8~9", dataService.findResultCountByYears(8,9));
        map.put("9~10", dataService.findResultCountByYears(9,10));
        map.put("10~11", dataService.findResultCountByYears(10,11));
        map.put("11~12", dataService.findResultCountByYears(11,12));
        map.put("12~13", dataService.findResultCountByYears(12,13));
        map.put("13~14", dataService.findResultCountByYears(13,14));
        map.put("14~15", dataService.findResultCountByYears(14,15));
        map.put("15", dataService.findResultCountByYears(15, Integer.MAX_VALUE));

        return new Result().success(map);
    }
}
