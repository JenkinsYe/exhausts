package com.csdn.xs.exhausts.service;

import com.csdn.xs.exhausts.domain.MeasurementDomain;
import com.csdn.xs.exhausts.domain.ModelInputDomain;
import com.csdn.xs.exhausts.domain.RemoteSenseDomain;
import com.csdn.xs.exhausts.domain.ResultDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 15:04 2019-12-08
 */
@Service
@Slf4j
public class ModelInputGenerateService {

    @Autowired
    private DataService dataService;


    public void startGenerateModelInput(List<RemoteSenseDomain> remoteSenseDomains) {

        log.info("开始生成模式输入数据");
        List<ModelInputDomain> modelInputDomains = new ArrayList<>();
        HashMap<String, ResultDomain> hashMap = new HashMap<>();
        for (RemoteSenseDomain remoteSense : remoteSenseDomains) {
            ResultDomain temp = hashMap.get(remoteSense.getLicensePlate());
            if (temp == null) {
                List<ResultDomain> tempDomains = dataService.findResultByLicense(remoteSense.getLicensePlate());
                if (tempDomains.size() == 0) {
                    continue;
                }
                temp = tempDomains.get(0);
                hashMap.put(remoteSense.getLicensePlate(), temp);
            }
            ModelInputDomain inputDomain = new ModelInputDomain();
            inputDomain.setPassTime(remoteSense.getPassTime());
            inputDomain.setLicensePlate(remoteSense.getLicensePlate());
            inputDomain.setVsp(remoteSense.getVsp());
            inputDomain.setVelocity(remoteSense.getVelocity());
            inputDomain.setAcceleration(remoteSense.getAcceleration());
            inputDomain.setFuelType(temp.getFuelType());
            inputDomain.setRegisterDate(temp.getRegisterDate());
            inputDomain.setTemperature(remoteSense.getTemperature());
            inputDomain.setHumidity(remoteSense.getHumidity());
            modelInputDomains.add(inputDomain);
        }
        try {
            writeToDisk(modelInputDomains);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("模式输入数据生成完成");
    }

    private void writeToDisk(List<ModelInputDomain> domains) throws Exception {
        log.info("write to csv file");
        File file = new File("data.csv");
        OutputStreamWriter ow = new OutputStreamWriter(new FileOutputStream(file), "utf8");

        for (ModelInputDomain domain : domains) {
            ow.write(domain.getPassTime() + ",");
            ow.write(domain.getLicensePlate()+",");
            ow.write(domain.getVsp()+",");
            ow.write(domain.getVelocity() + ",");
            ow.write(domain.getAcceleration()+",");
            ow.write(domain.getFuelType() + ",");
            ow.write(domain.getRegisterDate()+",");
            ow.write(domain.getTemperature()+",");
            ow.write(domain.getHumidity()+"");
            ow.write("\r\n");
        }
        ow.flush();
        ow.close();
        log.info("write work completed!");
    }
}
