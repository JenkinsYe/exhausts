package com.csdn.xs.exhausts.service;

import com.csdn.xs.exhausts.domain.RemoteSenseDomain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 11:28 2020-06-19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class StatisticServiceTest {

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private DataService dataService;

    @Test
    public void test() {
        List<RemoteSenseDomain> list = dataService.findRemoteSenseByFixture(19105);

        log.info(statisticService.getNumberOfInvalidRemoteSense(list) + "");
    }
}
