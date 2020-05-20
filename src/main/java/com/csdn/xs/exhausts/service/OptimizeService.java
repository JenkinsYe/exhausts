package com.csdn.xs.exhausts.service;

import com.csdn.xs.exhausts.domain.InternalDomain;
import com.csdn.xs.exhausts.domain.OptimizationDomain;
import com.csdn.xs.exhausts.domain.RemoteSenseDomain;
import com.csdn.xs.exhausts.domain.ResultDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * @author YJJ
 * @Date: Created in 14:48 2019-12-08
 */
@Service
@Slf4j
public class OptimizeService {

    @Autowired
    private DataService dataService;

    private List<InternalDomain> internals;

    @PostConstruct
    public void init() {
        internals = dataService.findAllInternal();
    }

    public void startOptimize(List<RemoteSenseDomain> domains) {
        log.info("开始优化");
        List<OptimizationDomain> optimizationDomains = new ArrayList<>();
        Long now = System.currentTimeMillis();
        int count = 0;
        for (RemoteSenseDomain remoteSenseDomain : domains) {
            // 查询年检数据
            List<ResultDomain> resultDomains = dataService.findResultByLicense(remoteSenseDomain.getLicensePlate());
            if (resultDomains.size() == 0)
                continue;
            ResultDomain domain = resultDomains.get(0);
            Long delta = now - domain.getRegisterDate().getTime();
            Long fiveYear = 24l * 3600l * 365l * 5l * 1000l;
            if (delta < fiveYear)
                continue;
            OptimizationDomain optimizationDomain = new OptimizationDomain();
            Double co, no, hc, v, a, vsp;
            co = remoteSenseDomain.getCo();
            no = remoteSenseDomain.getNo();
            hc = remoteSenseDomain.getHc();
            v = remoteSenseDomain.getVelocity();
            a = remoteSenseDomain.getAcceleration();
            vsp = remoteSenseDomain.getVsp();

            Double tco, tno, thc;
            tco = co + 2.994 * Math.pow(10, -5) * v - 1.377 * Math.pow(10, -4)*a + 3.589 * Math.pow(10, -5)*a*a + 0.00132*vsp;
            tno = no + 1.430 + 0.623*Math.pow(10, -2)*v  - 7.058 * Math.pow(10, -2)*a + 1 * Math.pow(10, -3)*a*a + 0.0832*vsp;
            thc = hc + 0.332+(3.601*Math.pow(10, -3)*v -5.179*Math.pow(10, -2)*a + 9.807*Math.pow(10, -3)*a*a) + 0.0332*vsp;
            // 设置优化结果,相关系数可修改
            optimizationDomain.setLicenseType(domain.getLicenseType());
            optimizationDomain.setRecognizeCode(domain.getRecognizeCode());
            optimizationDomain.setLicensePlate(remoteSenseDomain.getLicensePlate());
            optimizationDomain.setFixture(remoteSenseDomain.getFixture());
            optimizationDomain.setRemoteSenseTime(remoteSenseDomain.getPassTime());
            optimizationDomain.setCo(domain.getCo() + 0.01 * Math.tanh(tco));
            optimizationDomain.setNo(domain.getNo() + 0.005 * Math.tanh(tno) + new Random().nextDouble() * 0.001);
            optimizationDomain.setHc(domain.getHc() + 0.005 * Math.tanh(thc) + new Random().nextDouble() * 0.001);

            // 计算VEI

            if (remoteSenseDomain.getState().equals("超标") || domain.getResult() == 0) {
                optimizationDomain.setVei(10d);
                optimizationDomain.setResult("不通过");
            } else {
                if (case3(domain)) {
                    optimizationDomain.setVei(10d + new Random().nextDouble() * 30);
                    optimizationDomain.setResult("不通过");
                } else if (case1(domain)) {
                    optimizationDomain.setVei(case2CalculateVEI(domain, remoteSenseDomain));
                    if (optimizationDomain.getVei() >= 85)
                        optimizationDomain.setResult("通过");
                    else
                        optimizationDomain.setResult("不通过");
                } else {
                    optimizationDomain.setVei(40d + new Random().nextDouble() * 30);
                    optimizationDomain.setResult("不通过");
                }
            }

            optimizationDomains.add(optimizationDomain);
        }
        dataService.saveOptimizations(optimizationDomains);
        log.info("优化完成");
    }

    private Double case2CalculateVEI(ResultDomain domain, RemoteSenseDomain remoteSenseDomain) {
        boolean ac, no, co, hc;
        ac = no = co = hc = false;
        Double result = 0d;
        for (int i = 0 ; i < internals.size(); i++) {
            InternalDomain internal = internals.get(i);
            if (internal.getAc() > domain.getTotalDistance() && !ac) {
                ac = true;
                result += 0.25 * (1 - internals.get(i-1).getId()/0.5);
            }
            if (internal.getCo() > domain.getCo() && !co) {
                co = true;
                result += 0.25 * (1 - internals.get(i-1).getId()/0.4);
            }
            if (internal.getNo() > domain.getNo() && !no) {
                no = true;
                result += 0.25 * (1 - internals.get(i-1).getId()/0.4);
            }
            if (internal.getHc() > domain.getHc() && !hc) {
                hc = true;
                result += 0.25 * (1 - internals.get(i-1).getId()/0.4);
            }
            if (ac && no && co && hc)
                break;
        }

        Double we;
        Double cco = remoteSenseDomain.getCo();
        Double nno = remoteSenseDomain.getNo();
        Double hhc = remoteSenseDomain.getHc();
        if ((cco > 0.2 && cco < 0.3) || (nno > 50 && nno < 100) || (hhc > 15 && hhc < 35)) {
            we = 0.33 - (cco/0.6 + nno/700 + hhc/80)*(1/3);
        } else if ((cco >= 0.3) || (nno >= 100) || (hhc >= 35)) {
            we = 0.01;
        } else {
            we = 1 - (cco/0.6 + nno/700 + hhc/80)*(1/3);
        }
        return 70d + 30 * result + we;
    }

    private boolean case1(ResultDomain domain) {
        Double ac = domain.getTotalDistance();
        Double hc = domain.getHc();
        Double no = domain.getNo();
        Double co = domain.getCo();

        if (ac <= 98193 && hc <= 0.040 &&  no <= 0.040 && co <= 0.550)
            return true;
        return false;
    }

    private boolean case3(ResultDomain domain) {
        Double td = domain.getTotalDistance();
        Double hc = domain.getHc();
        Double no = domain.getNo();
        Double co = domain.getCo();

        if (td > 169028.4 && hc > 0.07 && no > 0.08 && co > 1.02)
            return true;
        return false;
    }

}
