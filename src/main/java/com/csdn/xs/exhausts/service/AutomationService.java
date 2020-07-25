package com.csdn.xs.exhausts.service;

import com.csdn.xs.exhausts.domain.OptimizationDomain;
import com.csdn.xs.exhausts.domain.RemoteSenseDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 17:09 2019-12-08
 */
@Service
@Slf4j
public class AutomationService {

    @Autowired
    private ModelInputGenerateService modelInputGenerateService;

    @Autowired
    private OptimizeService optimizeService;

    @Autowired
    private DataService dataService;

    @Autowired
    private AssessService assessService;

    @Autowired
    private ModelService modelService;

    private static Long remoteID = 0l;

    private static Long optimizeID = 0l;

    public static int cr1 = 0;
    public static int cr2 = 0;
    public static int cr3 = 0;
    public static int cr4 = 0;
    public static int cr5 = 0;
    public static int cr6 = 0;
    public static int cr7 = 0;
    public static int cr8 = 0;
    public static int cr9 = 0;


    @PostConstruct
    private void init() {
        remoteID = dataService.findLastRemoteSenseId();
        optimizeID = dataService.findLastOptimizeId();
        log.info("自动化服务初始化完成");
        log.info("remoteID: " + remoteID + " optimizeID: " + optimizeID);
    }

    /**
     * 自动化流程
     */
    public void startOptimizeWork() {
        log.info("已经优化最后遥测记录id: " + remoteID);
        // 查询当前时间间隔内的遥测数据
        List<RemoteSenseDomain> tempRemoteSenseDomains = dataService.findRemoteSenseAfterID(remoteID);

        if (tempRemoteSenseDomains.size() == 0) {
            return;
        }
        // 更新当前最新的遥测id
        remoteID = tempRemoteSenseDomains.get(tempRemoteSenseDomains.size() - 1).getId();
        dataService.updateRemoteSenseId(remoteID);
        log.info("最后遥测id记录更新为: " + remoteID);

        log.info("遥测数据记录 : " + tempRemoteSenseDomains.size());

        // 过滤不满足条件的车
        List<RemoteSenseDomain> remoteSenseDomains = filterData(tempRemoteSenseDomains);

        log.info("条件1过滤: " + cr1);
        log.info("条件2过滤: " + cr2);
        log.info("条件3过滤: " + cr3);
        log.info("条件4过滤: " + cr4);
        log.info("条件5过滤: " + cr5);
        log.info("条件6过滤: " + cr6);
        log.info("条件7过滤: " + cr7);
        log.info("条件8过滤: " + cr8);
        log.info("条件9过滤: " + cr9);



        if (remoteSenseDomains.size() == 0) return;
        log.info("筛选后遥测数据记录 : " +remoteSenseDomains.size());

        // 生成model输入数据
        modelInputGenerateService.startGenerateModelInput(remoteSenseDomains);

        // 等待model运行
        modelService.startCalculator();

        // 优化;
        optimizeService.startOptimize(remoteSenseDomains);


    }

    public void startAssessWork() {
        // 归纳评估
        log.info("开始评估");
        log.info("已经评估最后记录id: " + optimizeID);

        List<OptimizationDomain> list = dataService.findOptimizeAfterID(optimizeID);
        log.info("优化查询结果: " + list.size() + " 条遥测记录");
        if (list.size() == 0) {
            log.info("评估结束");
            return;
        }

        optimizeID = list.get(list.size() - 1).getId();
        dataService.updateOptimizeId(optimizeID);
        log.info("最后优化记录id更新为: " + optimizeID);

        assessService.startAssess(list);

    }


    public List<RemoteSenseDomain> filterData(List<RemoteSenseDomain> domains) {
        log.info("开始过滤数据");
        List<RemoteSenseDomain> result1 = new ArrayList<>();
        List<RemoteSenseDomain> result2 = new ArrayList<>();

        HashSet<String> set = new HashSet<>();

        int count = 1;
        for (RemoteSenseDomain domain : domains) {
            log.info("开始过滤 第{}条记录", count++);
            if (isValid(domain))
                result1.add(domain);
            else {
                set.add(domain.getLicensePlate());
            }
        }

        for (RemoteSenseDomain domain : result1) {
            if (!set.contains(domain.getLicensePlate())) {
                result2.add(domain);
            }
        }

        log.info("数据过滤结束");
        return result2;

    }

    /**
     * 判断遥测数据是否有价值
     * @param domain
     * @return
     */
    public boolean isValid(RemoteSenseDomain domain) {
        if (!domain.getColor().equals("蓝"))
            return false;
        if (domain.getVsp() <= 0 || domain.getVsp() > 20){
            cr1++;
            return false;
        }
        if (domain.getWindVelocity() > 5){
            cr2++;
            return false;
        }
        if (domain.getTemperature() < -20 || domain.getTemperature() > 45){
            cr3++;
            return false;
        }
        if (domain.getHumidity() > 95) {
            cr4++;
            return false;
        }
        if (domain.getCredence() < 85) {
            cr6++;
            return false;
        }
        if (domain.getCo() > 0.6 || domain.getCo() < 0.00001) {
            cr7++;
            return false;
        }
        if (domain.getNo() > 700 || domain.getNo() < 0.00001) {
            cr8++;
            return false;
        }
        if (domain.getHc() > 80 || domain.getHc() < 0.00001) {
            cr9++;
            return false;
        }
        if (dataService.findResultByLicense(domain.getLicensePlate()).size() == 0)
            return false;
        return true;
    }
}
