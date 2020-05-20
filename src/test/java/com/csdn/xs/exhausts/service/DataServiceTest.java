package com.csdn.xs.exhausts.service;

import com.csdn.xs.exhausts.domain.MeasurementDomain;
import com.csdn.xs.exhausts.domain.OptimizationDomain;
import com.csdn.xs.exhausts.domain.RemoteSenseDomain;
import com.csdn.xs.exhausts.domain.ResultDomain;
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

    @Test
    public void test() {
        List<MeasurementDomain> domainList = dataService.findMeasurementByLicense("浙AY638L");
        for (MeasurementDomain domain : domainList) {
            log.info(domain.getLicensePlate());
        }
        log.info(domainList.size()+"");
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
}
