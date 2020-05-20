package com.csdn.xs.exhausts.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author YJJ
 * @Date: Created in 10:54 2019-12-09
 */
@Service
@Slf4j
public class ModelService {

    public void startCalculator() {
/*        String command = "/home/jianglinhui/s_car/scripts/run_all.bash " + "data.csv";
        try {
            Process ps = Runtime.getRuntime().exec(command);
            ps.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        log.info("模式计算完成");

    }
}
