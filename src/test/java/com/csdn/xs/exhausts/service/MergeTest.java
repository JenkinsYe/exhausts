package com.csdn.xs.exhausts.service;

import com.csdn.xs.exhausts.domain.ProcessDomain;
import com.csdn.xs.exhausts.domain.ResultDomain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 10:00 2019-12-11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MergeTest {


    @Autowired
    private DataService dataService;

/*    @Test
    public void test() {
        log.info("Test start!");
        List<ResultDomain> list = dataService.findAllResult();

        for (int i = 0; i < list.size(); i++) {
            ResultDomain temp = list.get(i);
            log.info("No. " + i + " domain");
            ProcessDomain process = dataService.findProcessByID(temp.getMeasureID()).get(0);

            temp.setLicenseCard(process.getLicenseCard());
            temp.setRegisterType(process.getRegisterType());
            temp.setRegisterDate(process.getRegisterDate());
            temp.setFuelType(process.getFuelType());
            temp.setTotalDistance(process.getTotalDistance());
            temp.setWeight(process.getWeight());
            dataService.updateResult(temp);
        }

    }

    @Test
    public void test2() {
        ResultDomain domain = dataService.findResultById("1911140092");
        domain.setTotalDistance(5432.00d);
        dataService.updateResult(domain);
    }*/

    @Test
    public void test3() {
        ProcessDomain processDomain = dataService.findProcessByID("1911140092").get(0);
        log.info("Distance " + processDomain.getTotalDistance());
        log.info("hc: " + processDomain.getHc());
    }
}
