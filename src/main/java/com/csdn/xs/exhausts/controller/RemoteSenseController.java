package com.csdn.xs.exhausts.controller;

import com.csdn.xs.exhausts.Response.RemoteSenseSaveResponse;
import com.csdn.xs.exhausts.domain.RemoteSenseDomain;
import com.csdn.xs.exhausts.service.DataService;
import com.csdn.xs.exhausts.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
;

/**
 * @author YJJ
 * @Date: Created in 10:52 2019-12-12
 */
@Slf4j
@RestController
public class RemoteSenseController {

    @Autowired
    private DataService dataService;

    @PostMapping("/remote/upload")
    public RemoteSenseSaveResponse saveRemoteSense(
                                                   String state,
                                                   Date passTime,
                                                   String license,
                                                   String color,
                                                   Double co,
                                                   Double co2,
                                                   Double no,
                                                   Double hc,
                                                   Double ringelmanBlack,
                                                   Double kValue,
                                                   Double opacity,
                                                   Double averageOpacity,
                                                   Double vsp,
                                                   Double velocity,
                                                   Double acceleration,
                                                   Integer credence,
                                                   Double windVelocity,
                                                   Integer windDirection,
                                                   Double temperature,
                                                   Double humidity,
                                                   Integer airPressure,
                                                   Integer fixture,
                                                   String monitorPoint,
                                                   String diselCars,
                                                   MultipartFile imgData) {
        RemoteSenseSaveResponse response = new RemoteSenseSaveResponse();
/*        if (!num.equals(remoteData.size()) || !num.equals(imgData.size())) {
            response.setSuccess(false);
            response.setMsg("错误, 数据量不匹配");
            return response;
        }*/
        RemoteSenseDomain remoteData = new RemoteSenseDomain();
        remoteData.setState(state);
        remoteData.setPassTime(new java.sql.Timestamp(passTime.getTime()));
        remoteData.setLicensePlate(license);
        remoteData.setColor(color);
        remoteData.setCo(co);
        remoteData.setCo2(co2);
        remoteData.setNo(no);
        remoteData.setHc(hc);
        remoteData.setRingelmanBlack(ringelmanBlack);
        remoteData.setKValue(kValue);
        remoteData.setOpacity(opacity);
        remoteData.setAverageOpacity(averageOpacity);
        remoteData.setVsp(vsp);
        remoteData.setAcceleration(acceleration);
        remoteData.setVelocity(velocity);
        remoteData.setCredence(credence);
        remoteData.setWindVelocity(windVelocity);
        remoteData.setWindDirection(windDirection);
        remoteData.setTemperature(temperature);
        remoteData.setHumidity(humidity);
        remoteData.setAirPressure(airPressure);
        remoteData.setFixture(fixture);
        remoteData.setMonitorPoint(monitorPoint);
        remoteData.setDiselCars(diselCars);

        MultipartFile file = imgData;
        String contentType = file.getContentType();
        String rootFileName = file.getOriginalFilename();
        log.info("上传图片 name:{}, type:{}", rootFileName, contentType);
        String fileName= null;
        try {
            fileName = FileUtils.saveImg(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fileName == null) {
            response.setSuccess(false);
            response.setMsg("上传失败");
            return response;
        }

        remoteData.setImageUrl(fileName);
        dataService.saveRemoteSense(remoteData);
        response.setSuccess(true);
        response.setMsg("成功");
        return response;
    }
}
