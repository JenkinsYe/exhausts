package com.csdn.xs.exhausts.controller;

import com.csdn.xs.exhausts.response.DynamicDetailRemoteSenseResponse;
import com.csdn.xs.exhausts.response.RemoteSenseSaveResponse;
import com.csdn.xs.exhausts.response.Result;
import com.csdn.xs.exhausts.domain.RemoteSenseDomain;
import com.csdn.xs.exhausts.service.DataService;
import com.csdn.xs.exhausts.service.StatisticService;
import com.csdn.xs.exhausts.utils.ConstantUtils;
import com.csdn.xs.exhausts.utils.DateUtils;
import com.csdn.xs.exhausts.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * @author YJJ
 * @Date: Created in 10:52 2019-12-12
 */
@Slf4j
@RestController
public class RemoteSenseController {

    @Autowired
    private DataService dataService;

    @Autowired
    private StatisticService statisticService;

    /**
     * 遥测数据上传接口
     * @param state
     * @param passTime
     * @param license
     * @param color
     * @param co
     * @param co2
     * @param no
     * @param hc
     * @param ringelmanBlack
     * @param kValue
     * @param opacity
     * @param averageOpacity
     * @param vsp
     * @param velocity
     * @param acceleration
     * @param credence
     * @param windVelocity
     * @param windDirection
     * @param temperature
     * @param humidity
     * @param airPressure
     * @param fixture
     * @param monitorPoint
     * @param diselCars
     * @param imgData
     * @return
     */
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
        if (file == null) {
            response.setSuccess(true);
            response.setMsg("成功");
            return response;
        }
        String contentType = file.getContentType();
        String rootFileName = file.getOriginalFilename();
        log.info("遥测数据上传, 上传图片 name:{}, type:{}", rootFileName, contentType);
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

    /**
     * 获取遥测点位实时遥测数据接口
     * @param fixture
     * @return
     */
    @GetMapping("/api/remoteSense/dynamic")
    public Result getDynamicRemote(@RequestParam("fixture") Integer fixture) {
        log.info("获取动态数据, 点位编号:{}", fixture);

        RemoteSenseDomain domain = dataService.findNewestRemoteSenseByFixture(fixture);
        Result result = new Result();
        if (domain == null) {
            result.success();
            result.setMsg("当前设备无信息");
            return result;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("color", domain.getColor());
        map.put("no", domain.getNo());
        map.put("co", domain.getCo());
        map.put("hc", domain.getHc());
        map.put("location", ConstantUtils.getLocationByFixture(domain.getFixture()));
        map.put("license", domain.getLicensePlate());
        map.put("result", domain.getState());
        map.put("count", dataService.findNumberOfRemoteSenseToday(domain.getMonitorPoint()));
        map.put("passTime", domain.getPassTime());
        map.put("id", domain.getId());
        map.put("imgUrl", "http://202.101.162.69:8081/api/remoteSense/img?id="+domain.getId());

        result.success(map);
        return result;
    }


    /**
     * 根据id查询遥测信息
     * @param id
     * @param size
     * @return
     */
    @GetMapping("/api/remoteSense/dynamicDetail")
    public HashMap<String, Object> getDynamicDetailRemote(@RequestParam("id") Long id,
                                                          @RequestParam(value = "size", required = false) Integer size) {
        log.info("获取动态最新数据");
        List<DynamicDetailRemoteSenseResponse> response = new ArrayList<>();


        List<RemoteSenseDomain> domains = dataService.findRemoteSenseAfterIDWithUpperBound(id);
        if (domains == null || domains.size() == 0)
            return new HashMap<String, Object>(){
                {
                    put("size", 0);
                    put("data", new ArrayList<>());
                }
            };
        if (size == null || size > domains.size())
            size = domains.size();
        for (int i = 0; i < size; i++) {
            RemoteSenseDomain domain = domains.get(i);
            DynamicDetailRemoteSenseResponse response1 = new DynamicDetailRemoteSenseResponse();
            // 设置详细信息
            response1.setId(domain.getId());
            response1.setHumidity(domain.getHumidity());
            response1.setAirPressure(domain.getAirPressure());
            response1.setTemperature(domain.getTemperature());
            response1.setWindDirection(domain.getWindDirection());
            response1.setWindVelocity(domain.getWindVelocity());
            response1.setLicensePlate(domain.getLicensePlate());
            response1.setColor(domain.getColor());
            response1.setCo(domain.getCo());
            response1.setCo2(domain.getCo2());
            response1.setNo(domain.getNo());
            response1.setHc(domain.getHc());
            response1.setVsp(domain.getVsp());
            response1.setVelocity(domain.getVelocity());
            response1.setAcceleration(domain.getAcceleration());
            response1.setState(domain.getState());
            response1.setPassTime(domain.getPassTime());
            response1.setFixture(domain.getFixture());
            response1.setMonitorPoint(domain.getMonitorPoint());
            if (domain.getMonitorPoint().startsWith("奔竞大道")) {
                response1.setLocationID(2);
            } else {
                response1.setLocationID(1);
            }
            response1.setCount(dataService.findNumberOfRemoteSenseToday(domain.getMonitorPoint()));
            response1.setImageUrl("http://202.101.162.69:8081/api/remoteSense/img?id="+domain.getId());
            response.add(response1);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("size", size);
        map.put("data", response);
        return map;
    }

    /**
     * 获取遥测设备编号列表
     * @return
     */
    @GetMapping("/api/remoteSense/fixtureList")
    public Result getFixtureList() {
        log.info("获取设备编号列表");
        Result result = new Result();
        List<Integer> fixtureList = dataService.findFixtureList();
        HashMap<String, Object> map = new HashMap<>();
        map.put("fixtures", fixtureList);

        return result.success(map);
    }

    /**
     * 遥测数据统计值接口
     * @param startString
     * @param endString
     * @param fixture
     * @return
     */
    @GetMapping("/api/remoteSense/num")
    public Result getRemoteSenseNumByFixtureAndTimeInternal(@RequestParam("start") String startString, @RequestParam("end") String endString,
                                                            @RequestParam("fixture") Integer fixture) {
        log.info("获取遥测数据统计值，开始时间{}, 结束时间{}, 设备编号{}", startString, endString, fixture);
        if (startString == null || endString == null || fixture == null)
            return new Result().fail("参数不可为空");
        Result result = new Result();
        Date start;
        Date end;
        try {
            start = DateUtils.dateStringToDate(startString, "yyyy-MM-dd HH:mm:ss");
            end = DateUtils.dateStringToDate(endString, "yyyy-MM-dd HH:mm:ss");
            HashMap<String, Object> map = new HashMap<>();
            map.put("exceeded", dataService.findRemoteSenseByTimeInternalAndFixtureAndState(start, end, fixture, false));
            map.put("pass", dataService.findRemoteSenseByTimeInternalAndFixtureAndState(start, end, fixture, true));
            map.put("total", (int)map.get("pass") + (int)map.get("exceeded"));
            return new Result().success(map);
        } catch (ParseException e) {
            e.printStackTrace();
            result.fail();
            result.setMsg("日期无法解析");
            return result;
        }
    }

    @GetMapping("/api/remoteSense/valid")
    public Result getRemoteSenseValidCount(@RequestParam("fixture") Integer fixture) {
        log.info("获取遥测有效/无效条数");
        List<RemoteSenseDomain> list = dataService.findRemoteSenseByFixture(fixture);
        int count = statisticService.getNumberOfInvalidRemoteSense(list);

        HashMap<String, Object> map = new HashMap<>();
        map.put("valid", list.size() - count);
        map.put("invalid", count);

        return new Result().success(map);
    }
}
