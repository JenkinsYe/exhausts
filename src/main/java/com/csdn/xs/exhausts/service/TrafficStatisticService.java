package com.csdn.xs.exhausts.service;

import com.alibaba.fastjson.JSONObject;
import com.csdn.xs.exhausts.domain.TrafficDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 11:20 2020-06-17
 */
@Service
@Slf4j
public class TrafficStatisticService {

    @Autowired
    private TrafficDataService trafficDataService;

    public List<HashMap<String, Object>> getVehiclePercentage(String timeString) {
        List<TrafficDomain> list = trafficDataService.findTrafficByTimeString(timeString);
        List<HashMap<String, Object>> resultList = new ArrayList<>();
        for (TrafficDomain domain : list) {
            resultList.add(getVehiclePercentageForEachChannel(domain));
        }

        return resultList;
    }

    private HashMap<String, Object> getVehiclePercentageForEachChannel(TrafficDomain domain) {
        HashMap<String, Object> resultMap = new HashMap<>();
        int total = domain.getFlow();
        if (total == 0)
            return resultMap;
        JSONObject json = JSONObject.parseObject(domain.getFlowString());
        for (String key : json.keySet()) {
            if ("averageSpeed".equals(key))
                continue;
            JSONObject json2 = json.getJSONObject(key);
            int temp = 0;
            for (String key2 : json2.keySet()) {
                temp += (int)json2.get(key2);
            }

            resultMap.put(key, temp * 1.0 / total);
        }
        resultMap.put("channelID", domain.getChannelID());
        return resultMap;
    }
}
