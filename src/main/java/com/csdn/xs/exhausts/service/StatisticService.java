package com.csdn.xs.exhausts.service;

import com.csdn.xs.exhausts.domain.RemoteSenseDomain;
import com.csdn.xs.exhausts.domain.StatisticDomain;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 12:28 2020-06-16
 */
@Service
@Slf4j
public class StatisticService {

    @Autowired
    private DataService dataService;

    public StatisticDomain findStatisticByVersion(Long version) {
        if (version == null) {
            return dataService.findNewestStatistic();
        } else {
            return dataService.findStatisticByVersion(version);
        }
    }

    public Integer getMoneySaving() {
        int count = dataService.findWithoutCheckingVehicleCount();
        return 65 * count;
    }

    public Integer getTimeSaving() {
        int count = dataService.findWithoutCheckingVehicleCount();
        return count;
    }


    public Integer getNumberOfInvalidRemoteSense(List<RemoteSenseDomain> domainList) {
        int count = 0;
        for (RemoteSenseDomain domain : domainList) {
            if (domain.getCredence() < 80 || domain.getVsp() < 0 || domain.getVsp() > 20
                    || domain.getTemperature() < -20 || domain.getTemperature() > 45 || domain.getDiselCars().equals("True"))
                count++;
        }
        return count;
    }

    public HashMap<String, Object> getAveragePollutionByFixtureAndTimeInternal(Integer fixture, Date start, Date end) {
        List<RemoteSenseDomain> domains = dataService.findRemoteSenseByFixtureAndTimeInternal(start, end, fixture);
        HashMap<String, Object> map = new HashMap<>();
        double totalNO, totalCO, totalCO2, totalHC;
        totalCO = totalCO2 = totalHC = totalNO = 0;
        int count = 0;
        for (RemoteSenseDomain domain: domains) {
            if (domain.getNo() == null || domain.getCo() == null || domain.getCo2() == null || domain.getHc() == null) {
                continue;
            }
            count++;
            totalNO += domain.getNo();
            totalCO2 += domain.getCo2();
            totalCO += domain.getCo();
            totalHC += domain.getHc();
        }
        if (count == 0) {
            map.put("co2", totalCO2);
            map.put("co", totalCO);
            map.put("no", totalNO);
            map.put("hc", totalHC);
            map.put("size", 0);
            return map;
        }
        map.put("co2", totalCO2/count);
        map.put("co", totalCO/count);
        map.put("no", totalNO/count);
        map.put("hc", totalHC/count);
        map.put("size", count);
        return map;
    }
}
