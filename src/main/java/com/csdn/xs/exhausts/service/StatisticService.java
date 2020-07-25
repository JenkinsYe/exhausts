package com.csdn.xs.exhausts.service;

import com.csdn.xs.exhausts.domain.RemoteSenseDomain;
import com.csdn.xs.exhausts.domain.StatisticDomain;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 12:28 2020-06-16
 */
@Service
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
}
