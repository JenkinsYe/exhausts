package com.csdn.xs.exhausts.controller;

import com.csdn.xs.exhausts.domain.OptimizationDomain;
import com.csdn.xs.exhausts.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YJJ
 * @Date: Created in 12:40 2019-12-08
 */
@RestController
public class TestController {


    @Autowired
    private DataService dataService;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }



    @GetMapping("/saveTest")
    public String saveOptimization() {
        OptimizationDomain domain = new OptimizationDomain();
        domain.setLicensePlate("test");
        dataService.saveOptimization(domain);
        return "success";
    }


}
