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
 * @Date: Created in 16:49 2019-12-08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ModelInputServiceTest {

    @Autowired
    private ModelInputGenerateService modelInputGenerateService;

    @Autowired
    private DataService dataService;
/*
    @Test
    public void test() {
        log.info("generate work begin..");
        List<RemoteSenseDomain> remoteSenseDomains = dataService.findRemoteSenseHoursAgo(3);
        modelInputGenerateService.startGenerateModelInput(remoteSenseDomains);
        log.info("generate work end..");
    }*/
}
