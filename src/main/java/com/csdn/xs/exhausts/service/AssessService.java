package com.csdn.xs.exhausts.service;

import com.csdn.xs.exhausts.domain.AssessmentDomain;
import com.csdn.xs.exhausts.domain.OptimizationDomain;
import com.csdn.xs.exhausts.domain.ResultDomain;
import com.csdn.xs.exhausts.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 14:35 2019-12-09
 */
@Service
@Slf4j
public class AssessService {

    @Autowired
    private DataService dataService;

    public void startAssess(List<OptimizationDomain> list) {
        //List<OptimizationDomain> list = dataService.findOptimizeHoursAgo(hoursGap);
        List<AssessmentDomain> assessmentDomains = new ArrayList<>();

        String tempLicense = list.get(0).getLicensePlate();
        int remoteSenseCount = 0;
        double minCO = Double.MAX_VALUE;
        double minNO = Double.MAX_VALUE;
        double minHC = Double.MAX_VALUE;
        double minVEI = Double.MAX_VALUE;
        double totalCO = 0d;
        double totalNO = 0d;
        double totalHC = 0d;
        double totalVEI = 0d;
        boolean flag = list.get(0).getResult().equals("通过");
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getLicensePlate().equals(tempLicense)) {
                AssessmentDomain assessmentDomain = new AssessmentDomain();
                assessmentDomain.setLicenseType(list.get(i-1).getLicenseType());
                assessmentDomain.setRecognizeCode(list.get(i-1).getRecognizeCode());
                ResultDomain result = dataService.findResultByCode(list.get(i-1).getRecognizeCode()).get(0);
                if (DateUtils.isOutOfDate(result.getMeasureDate())) {
                    flag = false;
                    assessmentDomain.setNextMeasure(-1);
                } else {
                    assessmentDomain.setNextMeasure(DateUtils.getNextMeasureDays(result.getMeasureDate()));
                }
                assessmentDomain.setNumberOfRemoteSense(remoteSenseCount);
                assessmentDomain.setLicensePlate(tempLicense);
                assessmentDomain.setAssessTime(new java.sql.Timestamp(System.currentTimeMillis()));
                //assessmentDomain.setAssessTime(System.currentTimeMillis());
                if (!flag) {
                    assessmentDomain.setResult("不免检");
                    assessmentDomain.setVei(minVEI);
                    assessmentDomain.setCo(minCO);
                    assessmentDomain.setNo(minNO);
                    assessmentDomain.setHc(minHC);
                } else {
                    assessmentDomain.setResult("免检");
                    assessmentDomain.setVei(totalVEI / remoteSenseCount);
                    assessmentDomain.setCo(totalCO / remoteSenseCount);
                    assessmentDomain.setNo(totalNO / remoteSenseCount);
                    assessmentDomain.setHc(totalHC / remoteSenseCount);
                }
                assessmentDomain.setNumberOfMeasurement(dataService.findResultCountByCode(result.getRecognizeCode()));
                assessmentDomains.add(assessmentDomain);
                // 重置变量
                tempLicense = list.get(i).getLicensePlate();
                remoteSenseCount = 1;
                minVEI = list.get(i).getVei();
                minCO = list.get(i).getCo();
                minNO = list.get(i).getNo();
                minHC = list.get(i).getHc();
                totalVEI = minVEI;
                totalCO = minCO;
                totalHC = minHC;
                totalNO = minNO;
                flag = list.get(i).getResult().equals("通过");
            } else {
                remoteSenseCount++;
                if (list.get(i).getResult().equals("不通过")) {
                    flag = false;
                    minVEI = Math.min(minVEI, list.get(i).getVei());
                    minCO = Math.min(minCO, list.get(i).getCo());
                    minHC = Math.min(minHC, list.get(i).getHc());
                    minNO = Math.min(minNO, list.get(i).getNo());
                }
                totalVEI += list.get(i).getVei();
                totalCO += list.get(i).getCo();
                totalHC += list.get(i).getHc();
                totalNO += list.get(i).getNo();
            }
        }


        // 最后一个

        AssessmentDomain assessmentDomain = new AssessmentDomain();
        assessmentDomain.setLicenseType(list.get(list.size()-1).getLicenseType());
        assessmentDomain.setRecognizeCode(list.get(list.size()-1).getRecognizeCode());
        ResultDomain result = dataService.findResultByCode(list.get(list.size()-1).getRecognizeCode()).get(0);
        if (DateUtils.isOutOfDate(result.getMeasureDate())) {
            flag = false;
            assessmentDomain.setNextMeasure(-1);
        } else {
            assessmentDomain.setNextMeasure(DateUtils.getNextMeasureDays(result.getMeasureDate()));
        }
        assessmentDomain.setNumberOfRemoteSense(remoteSenseCount);
        assessmentDomain.setLicensePlate(tempLicense);
        assessmentDomain.setAssessTime(new java.sql.Timestamp(System.currentTimeMillis()));

        if (!flag) {
            assessmentDomain.setResult("不免检");
            assessmentDomain.setVei(minVEI);
            assessmentDomain.setCo(minCO);
            assessmentDomain.setNo(minNO);
            assessmentDomain.setHc(minHC);
        } else {
            assessmentDomain.setResult("免检");
            assessmentDomain.setVei(totalVEI / remoteSenseCount);
            assessmentDomain.setCo(totalCO / remoteSenseCount);
            assessmentDomain.setNo(totalNO / remoteSenseCount);
            assessmentDomain.setHc(totalHC / remoteSenseCount);
        }
        assessmentDomain.setNumberOfMeasurement(dataService.findResultCountByCode(result.getRecognizeCode()));
        assessmentDomains.add(assessmentDomain);

        log.info("size " + assessmentDomains.size());

        dataService.insertAllAssessment(assessmentDomains);
        log.info("评估完成");
    }


}
