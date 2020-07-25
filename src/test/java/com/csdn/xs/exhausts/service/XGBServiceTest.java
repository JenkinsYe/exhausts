package com.csdn.xs.exhausts.service;

import com.csdn.xs.exhausts.domain.XGBResultDomain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 13:31 2020-06-11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class XGBServiceTest {

    @Autowired
    private DataService dataService;

    @Autowired
    private OptimizeService optimizeService;

    @Test
    public void test() {
        optimizeService.startService();
    }
}
