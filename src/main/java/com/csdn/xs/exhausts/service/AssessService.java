package com.csdn.xs.exhausts.service;

import com.csdn.xs.exhausts.domain.AssessmentDomain;
import com.csdn.xs.exhausts.domain.OptimizationDomain;
import com.csdn.xs.exhausts.domain.ResultDomain;
import com.csdn.xs.exhausts.domain.XGBResultDomain;
import com.csdn.xs.exhausts.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 将多条优化数据进行汇总评估
 * @author YJJ
 * @Date: Created in 14:35 2019-12-09
 */
@Service
@Slf4j
public class AssessService {

    @Autowired
    private DataService dataService;

    public void startService(List<XGBResultDomain> list) {
        log.info("开始统计");
        HashMap<String, ArrayList<XGBResultDomain>> map = new HashMap<>();
        for (XGBResultDomain domain : list) {
            if (map.containsKey(domain.getLicense())) {
                map.get(domain.getLicense()).add(domain);
            } else {
                ArrayList<XGBResultDomain> temp = new ArrayList<>();
                temp.add(domain);
                map.put(domain.getLicense(), temp);
            }
        }

        ArrayList<AssessmentDomain> assessmentResult = new ArrayList<>();

        log.info("总共 " + map.size() + " 车");
        int count = 0;
        Long nameListVersion = dataService.getNewestNameListVersion();
        nameListVersion++;
        dataService.updateNameListVersion(nameListVersion);
        for (String key : map.keySet()) {
            log.info("No " + count++ + " license: " + key);
            AssessmentDomain domain = new AssessmentDomain();

            // 免检名单版本
            domain.setNameListVersion(nameListVersion);

            domain.setLicensePlate(key);
            domain.setAssessTime(new java.sql.Timestamp(System.currentTimeMillis()));
            ArrayList<XGBResultDomain> xgbResultDomains = map.get(key);
            Double totalVEI, totalHC, totalNO, totalCO;
            totalVEI = totalHC = totalNO = totalCO = 0d;
            for (XGBResultDomain xgbResultDomain : xgbResultDomains) {
                totalVEI += xgbResultDomain.getVei();
                totalCO += xgbResultDomain.getPredictCO();
                totalHC += xgbResultDomain.getPredictHC();
                totalNO += xgbResultDomain.getPredictNO();
            }

            // 平均值
            domain.setVei(totalVEI / xgbResultDomains.size());
            domain.setCo(totalCO / xgbResultDomains.size());
            domain.setNo(totalNO / xgbResultDomains.size());
            domain.setHc(totalHC / xgbResultDomains.size());

            // 评价结果
            if (domain.getVei() > 85) {
                domain.setResult("免检");
            } else {
                domain.setResult("不免检");
            }

            // 单一识别码
            List<ResultDomain> resultDomains = dataService.findResultByLicense(domain.getLicensePlate());
            if (resultDomains.size() == 0)
                continue;
            ResultDomain result = resultDomains.get(0);
            domain.setRecognizeCode(result.getRecognizeCode());

            // 遥测年检次数
            domain.setNumberOfMeasurement(dataService.findResultCountByCode(domain.getRecognizeCode()));
            domain.setNumberOfRemoteSense(xgbResultDomains.size());

            //车辆种类
            domain.setLicenseType(result.getLicenseType());

            // 下一次年检时间
            if (DateUtils.isOutOfDate(result.getMeasureDate())) {
                domain.setNextMeasure(-1);
            } else {
                domain.setNextMeasure(DateUtils.getNextMeasureDays(result.getMeasureDate()));
            }
            assessmentResult.add(domain);
        }

        dataService.insertAllAssessment(assessmentResult);
        log.info("统计结束");
    }

    public void startAssess(List<OptimizationDomain> list) {
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

        log.info("size " + assessmentDomains.size());

        dataService.insertAllAssessment(assessmentDomains);
        log.info("评估完成");
    }


}
