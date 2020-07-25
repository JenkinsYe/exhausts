package com.csdn.xs.exhausts.service;

import com.csdn.xs.exhausts.domain.*;
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

    @Autowired
    private AssessService assessService;

    private static Long XGB_ID = 0l;

    private List<InternalDomain> internals;

    @PostConstruct
    public void init() {

        internals = dataService.findAllInternal();
        XGB_ID = dataService.findLastXGBID();
    }



    public void startService() {
        log.info("开始计算优化结果VEI值");
        List<XGBResultDomain> domains = dataService.findXGBRESAfterID(XGB_ID);
        log.info("当前最后XGB_ID: " + XGB_ID);
        XGB_ID = domains.get(domains.size()-1).getId();
        dataService.updateXGBID(XGB_ID);
        log.info("最后XGB_ID更新为: " + XGB_ID);



        for (int i = 0; i < domains.size(); i++) {
            log.info("no. " + i);
            Case veiCase = getCaseID(domains.get(i));
            XGBResultDomain domain = domains.get(i);
            Double we;
            Double cco = domain.getCCO();
            Double nno = domain.getCNO();
            Double hhc = domain.getCHC();
            double v = (cco / 0.6 + nno / 700 + hhc / 80) * (1 / 3);
            if ((cco > 0.2 && cco < 0.3) || (nno > 50 && nno < 100) || (hhc > 15 && hhc < 35)) {
                we = 0.33 - v;
            } else if ((cco >= 0.3) || (nno >= 100) || (hhc >= 35)) {
                we = 0.01;
            } else {
                we = 1 - v;
            }
            if (veiCase.caseID == 1) {
                domain.setVei(0d);
            } else if (veiCase.caseID == 2) {
                domain.setVei(10d);
            } else if (veiCase.caseID == 3) {
                domain.setVei(
                        we + 10 + 27 * (0.25 * (1 - veiCase.coID) + 0.25 * (1 - veiCase.hcID) + 0.25 * (1 - veiCase.noID) + 0.15 * (1 - veiCase.acID) + 0.1 * (1 - veiCase.wcID))
                );
            } else if (veiCase.caseID == 4) {
                domain.setVei(
                        we + 70 + 27 * (0.25 * (1 - veiCase.coID) + 0.25 * (1 - veiCase.hcID) + 0.25 * (1 - veiCase.noID) + 0.15 * (1 - veiCase.acID) + 0.1 * (1 - veiCase.wcID))
                );
            } else {
                domain.setVei(
                        we + 40 + 27 * (0.25 * (1 - veiCase.coID) + 0.25 * (1 - veiCase.hcID) + 0.25 * (1 - veiCase.noID) + 0.15 * (1 - veiCase.acID) + 0.1 * (1 - veiCase.wcID))
                );
            }
            dataService.updateXGBRes(domain);
        }
        log.info("VEI计算结束");
        assessService.startService(domains);
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


    private boolean judgePredictResult(XGBResultDomain domain) {
        if (domain.getPredictCO() > 6 || domain.getPredictHC() > 2.5 || domain.getPredictNO() > 2.5)
            return false;
        return true;
    }

    private Case getCaseID(XGBResultDomain domain) {
        if (! domain.getTestState() && ! judgePredictResult(domain)) {
            return new Case(1);
        }
        if (!domain.getTestState() || !judgePredictResult(domain)) {
            return new Case(2);
        }
        boolean ac, no, co, hc, wc;
        ac = no = co = hc = wc = false;
        Double acID, noID, coID, hcID, wcID;
        acID =  noID = coID = hcID = wcID = 1d;
        for (int i = 0 ; i < internals.size(); i++) {
            InternalDomain internal = internals.get(i);
            if (internal.getAc() > domain.getDistance() && !ac) {
                ac = true;
                acID = internals.get(i-1).getId();
            }
            if (internal.getCo() > domain.getVelCO() && !co) {
                co = true;
                coID = internals.get(i-1).getId();
            }
            if (internal.getNo() > domain.getVelNO() && !no) {
                no = true;
                noID = internals.get(i-1).getId();
            }
            if (internal.getHc() > domain.getVelHC() && !hc) {
                hc = true;
                hcID = internals.get(i-1).getId();
            }
            if (internal.getWe() > domain.getWeight() && !wc) {
                wc = true;
                wcID = internals.get(i-1).getId();
            }
            if (ac && no && co && hc && wc)
                break;
        }
        if (acID > 0.5 && noID > 0.5 && coID > 0.5 && hcID > 0.5 && wcID > 0.5) {
            return new Case(3, acID, noID, coID, hcID, wcID);
        }
        if (acID < 0.5 && noID < 0.5 && coID < 0.5 && hcID < 0.5 && wcID < 0.5) {
            return new Case(4, acID, noID, coID, hcID, wcID);
        }
        return new Case(5, acID, noID, coID, hcID, wcID);
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
/*
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
        } */

        return 70d + 30 * result;
    }
/*
    public Double newCalculateVEI(XGBResultDomain domain) {
        boolean ac, no, co, hc;
        ac = no = co = hc = false;
        Double result = 0d;
        for (int i = 0 ; i < internals.size(); i++) {
            InternalDomain internal = internals.get(i);
            if (internal.getAc() > domain.getDistance() && !ac) {
                ac = true;
                result += 0.25 * (1 - internals.get(i-1).getId()/0.5);
            }
            if (internal.getCo() > domain.getVelCO() && !co) {
                co = true;
                result += 0.25 * (1 - internals.get(i-1).getId()/0.4);
            }
            if (internal.getNo() > domain.getVelNO() && !no) {
                no = true;
                result += 0.25 * (1 - internals.get(i-1).getId()/0.4);
            }
            if (internal.getHc() > domain.getVelHC() && !hc) {
                hc = true;
                result += 0.25 * (1 - internals.get(i-1).getId()/0.4);
            }
            if (ac && no && co && hc)
                break;
        }

        Double we;
        Double cco = domain.getCCO();
        Double nno = domain.getCNO();
        Double hhc = domain.getCHC();
        if ((cco > 0.2 && cco < 0.3) || (nno > 50 && nno < 100) || (hhc > 15 && hhc < 35)) {
            we = 0.33 - (cco/0.6 + nno/700 + hhc/80)*(1/3);
        } else if ((cco >= 0.3) || (nno >= 100) || (hhc >= 35)) {
            we = 0.01;
        } else {
            we = 1 - (cco/0.6 + nno/700 + hhc/80)*(1/3);
        }

        return 70d + 30 * result + we;
    }
*/

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

    private class Case {
        public int caseID;

        public Double acID, noID, coID, hcID, wcID;

        Case(int caseID) {
            this.caseID = caseID;
        }

        Case(int caseID, Double acID, Double noID, Double coID, Double hcID, Double wcID) {
            this.caseID = caseID;
            this.acID = acID;
            this.coID = coID;
            this.noID = noID;
            this.hcID = hcID;
            this.wcID = wcID;
        }
    }
}
