package com.csdn.xs.exhausts.service;

import com.csdn.xs.exhausts.domain.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 16:02 2019-12-08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DataServiceTest {

    @Autowired
    private DataService dataService;

    @Autowired
    private ModelRoadDataService modelRoadDataService;

    @Test
    public void test() {
        List<MeasurementDomain> domainList = dataService.findMeasurementByLicense("浙AY638L");
        for (MeasurementDomain domain : domainList) {
            log.info(domain.getLicensePlate());
        }
        log.info(domainList.size()+"");
    }

    @Test
    public void modelRoadDataServiceTests(){
        List<ModelRoadDomain> list = modelRoadDataService.findNewestModelRoad(null);
        for (ModelRoadDomain domain : list) {
            log.info(domain.toString());
        }
        log.info(list.size()+"");
    }

    @Test
    public void test2() {
        List<RemoteSenseDomain> domains = dataService.findRemoteSenseHoursAgo(3);
        for (RemoteSenseDomain domain : domains) {
            log.info(domain.getPassTime().toString());
        }
    }
/*
    @Test
    public void test3() {
        List<OptimizationDomain> list = new ArrayList<>();
        list.add(new OptimizationDomain());
        list.add(new OptimizationDomain());
        dataService.saveOptimizations(list);
    }*/

    @Test
    public void fiveYearTest() {
        ResultDomain resultDomain = dataService.findResultByLicense("浙AJ68R5").get(0);

        log.info(System.currentTimeMillis() + "");
        log.info(resultDomain.getRegisterDate().getTime() + "");
        Long delta = System.currentTimeMillis() - resultDomain.getRegisterDate().getTime();
        Long fiveYear = 24l * 3600l * 365l * 5l * 1000l;
        log.info(delta + "");
        log.info(24l * 3600l * 365l * 5l * 1000l + "");
        boolean flag = delta < fiveYear;
        log.info(flag + "");
    }

    @Test
    public void getRemoteIDTest() {
        Long id = dataService.findLastRemoteSenseId();
        log.info("The last remote sense id is : " + id);
    }

    @Test
    public void getOptimizeIDTest() {
        Long id = dataService.findLastOptimizeId();
        log.info("The last optimize id is " + id);
    }

    @Test
    public void averageTest() {
        List<XGBResultDomain> domains = dataService.findALLXGB();
        log.info("size " + domains.size());
        int count1, count2;
        Double no1, no2, hc1, hc2, co1, co2;
        no1 = no2 = hc1 = hc2 = co1 = co2 = 0d;
        count1 = count2 = 0;
        int step = 0;
        for (XGBResultDomain domain : domains) {
            log.info("step : " + step++);
            if (domain.getVei() >= 85) {
                no1 += domain.getCNO();
                co1 += domain.getCCO();
                hc1 += domain.getCHC();
                count1++;
            } else {
                no2 += domain.getCNO();
                co2 += domain.getCCO();
                hc2 += domain.getCHC();
                count2++;
            }
        }
        log.info("no1 :" + (no1 / count1));
        log.info("co1 :" + (co1 / count1));
        log.info("hc1 :" + (hc1 / count1));

        log.info("no2 :" + (no2 / count2));
        log.info("co2 :" + (co2 / count2));
        log.info("hc2 :" + (hc2 / count2));
    }
}
