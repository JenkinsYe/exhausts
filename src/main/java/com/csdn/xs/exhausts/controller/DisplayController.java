package com.csdn.xs.exhausts.controller;

import com.csdn.xs.exhausts.Response.*;
import com.csdn.xs.exhausts.domain.*;
import com.csdn.xs.exhausts.service.DataService;
import com.csdn.xs.exhausts.utils.ConstantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 16:03 2019-12-09
 */

@RestController
@ResponseBody
@Slf4j
public class DisplayController {

    @Autowired
    private DataService dataService;

    @GetMapping("/nameList")
    public List<NameResponse> getNameList(@RequestParam("start") Timestamp start, @RequestParam("end") Timestamp end, @RequestParam("amount") Integer amount) {
        log.info("免检名单请求: " + "开始: " + start + " end: " + end + " amount: " + amount);
        List<AssessmentDomain> domains = dataService.findAssessmentByTimeInternal(start, end);
        if (domains.size() == 0) {
            return new ArrayList<>();
        }
        log.info("免检名单查询完毕");
        List<NameResponse> responseList = new ArrayList<>();
        for (AssessmentDomain domain : domains) {
            if (domain.getResult().equals("不免检"))
                continue;
            NameResponse nameResponse = new NameResponse();
            nameResponse.setLicense(domain.getLicensePlate());
            nameResponse.setColor("蓝");
            ResultDomain resultDomain = dataService.findResultByCode(domain.getRecognizeCode()).get(0);
            nameResponse.setRegisterDate(resultDomain.getRegisterDate());
            nameResponse.setTotalDistance(resultDomain.getTotalDistance());
            nameResponse.setRegisterType(resultDomain.getRegisterType());
            nameResponse.setMeasureDate(resultDomain.getMeasureDate());
            nameResponse.setFuelType(resultDomain.getFuelType());
            nameResponse.setWeight(resultDomain.getWeight());
            nameResponse.setDischarge(resultDomain.getEngineDischarge());
            nameResponse.setYhc(resultDomain.getHc());
            nameResponse.setYco(resultDomain.getCo());
            nameResponse.setYno(resultDomain.getNo());
            List<RemoteSenseDomain> remoteSenseDomains = dataService.findRemoteSenseByLicenseAndColor(domain.getLicensePlate(), "蓝");
            for (RemoteSenseDomain remoteSenseDomain : remoteSenseDomains) {
                RemoteSenseResponse response = new RemoteSenseResponse();
                response.setId(remoteSenseDomain.getId());
                response.setResult(remoteSenseDomain.getState());
                response.setVsp(remoteSenseDomain.getVsp());
                response.setCo2(remoteSenseDomain.getCo2());
                response.setCo(remoteSenseDomain.getCo());
                response.setNo(remoteSenseDomain.getNo());
                response.setHc(remoteSenseDomain.getHc());
                response.setFixture(remoteSenseDomain.getFixture());
                response.setLocation(ConstantUtils.getLocationByFixture(remoteSenseDomain.getFixture()));
                response.setRemoteTime(remoteSenseDomain.getPassTime());
                nameResponse.getRemoteSenseResponses().add(response);

            }

            nameResponse.setAssessHc(domain.getHc());
            nameResponse.setAssessCo(domain.getCo());
            nameResponse.setAssessNo(domain.getNo());
            nameResponse.setVei(domain.getVei());
            responseList.add(nameResponse);
            if (responseList.size() == amount)
                break;
        }
        log.info("免检名单完成");
        return responseList;
    }



    @GetMapping("/measurement")
    public MeasurementResponse getMeasurementByLicense(@RequestParam("license") String name) {
        log.info("车检信息: " + name);
        MeasurementResponse response = new MeasurementResponse();
        List<ResultDomain> resultDomains = dataService.findResultByLicense(name);
        if (resultDomains.size() == 0)
            return response;
        ResultDomain domain = resultDomains.get(0);

        response.setLicense(domain.getLicenseCard());
        response.setRegisterDate(domain.getRegisterDate());
        response.setRegisterType(domain.getRegisterType());
        response.setFuelType(domain.getFuelType());
        response.setWeight(domain.getWeight());
        response.setCo(domain.getCo());
        response.setNo(domain.getNo());
        response.setHc(domain.getHc());
        response.setDischarge(domain.getEngineDischarge());
        response.setResult(domain.getResult() == 1 ? "合格" : "不合格");

        return response;
    }

    @GetMapping("/assess")
    public AssessResponse getAssess(@RequestParam("license") String license) {
        List<AssessmentDomain> list = dataService.findAssessmentByLicense(license);
        if (list.size() == 0)
            return new AssessResponse();
        AssessmentDomain domain = list.get(0);

        AssessResponse response = new AssessResponse();
        response.setLicense(license);
        response.setCo(domain.getCo());
        response.setHc(domain.getNo());
        response.setNo(domain.getNo());
        response.setResult(domain.getResult());
        response.setVei(domain.getVei());
        response.setAssessStandard("评价总分>85");
        return response;
    }

    @GetMapping("/compare")
    public ComparisonResponse getComparison(@RequestParam("start") Timestamp start, @RequestParam("end") Timestamp end) {
        log.info("比较数据: " + "start: " + start + " end: " + end);
        List<RemoteSenseDomain> remoteSenseDomains = dataService.findRemoteByTimeIntergnal(start, end);
        log.info("size " + remoteSenseDomains.size());
        ComparisonResponse response = new ComparisonResponse();
        if (remoteSenseDomains.size() == 0)
            return new ComparisonResponse();
        Double no, co, hc, nowc, cowc, hcwc;
        int noc, coc, hcc, nowcc, cowcc, hcwcc;
        no = co = hc = nowc = cowc = hcwc = 0d;
        noc = coc = hcc = nowcc = cowcc = hcwcc = 0;
        for (RemoteSenseDomain domain : remoteSenseDomains) {
            List<AssessmentDomain> assessmentDomains = dataService.findAssessmentByLicense(domain.getLicensePlate());
            if (assessmentDomains.size() == 0)
                continue;
            AssessmentDomain assessmentDomain = assessmentDomains.get(0);
            if (assessmentDomain.getResult().equals("免检")) {
                nowc += domain.getNo();
                cowc += domain.getCo();
                hcwc += domain.getHc();
                nowcc++;
                cowcc++;
                hcwcc++;
            } else {
                no += domain.getNo();
                co += domain.getCo();
                hc += domain.getHc();
                noc++;
                coc++;
                hcc++;
            }
        }
        if (coc != 0) {
            response.setCo(co / coc);
            response.setHc(hc / hcc);
            response.setNo(no / noc);
        } else {
            response.setCo(-1d);
            response.setHc(-1d);
            response.setNo(-1d);
        }

        if (cowc != 0) {
            response.setCoWithoutChecking(cowc / cowcc);
            response.setNoWithoutChecking(nowc / nowcc);
            response.setHcWithoutChecking(hcwc / hcwcc);
        } else {
            response.setCoWithoutChecking(-1d);
            response.setNoWithoutChecking(-1d);
            response.setHcWithoutChecking(-1d);
        }

        return response;
    }


    @GetMapping("/dynamic")
    public DynamicRemoteSenseResponse getDynamicRemote(@RequestParam("fixture") Integer fixture) {
        log.info("获取动态数据");
        DynamicRemoteSenseResponse response = new DynamicRemoteSenseResponse();

        RemoteSenseDomain domain = dataService.findNewestRemoteSenseByFixture(fixture);
        if (domain == null)
            return new DynamicRemoteSenseResponse();
        response.setColor(domain.getColor());
        response.setLocation(ConstantUtils.getLocationByFixture(domain.getFixture()));
        response.setLicense(domain.getLicensePlate());
        response.setResult(domain.getState());
        response.setCount(dataService.findNumberOfRemoteSenseToday(domain.getMonitorPoint()));
        response.setPassTime(domain.getPassTime());
        response.setId(domain.getId());

        return response;
    }

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
            response1.setImageUrl("http://202.101.162.69:8081/downloadImage?id="+domain.getId());
            response.add(response1);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("size", size);
        map.put("data", response);
        return map;
    }

    @GetMapping(value = "/downloadImage")
    public String downloadImage(@RequestParam("id") Long id, HttpServletRequest request, HttpServletResponse response) {
        log.info("下载图片: " + "id: " + id);
        List<RemoteSenseDomain> domains = dataService.findRemoteById(id);
        if (domains.size() == 0)
            return null;
        RemoteSenseDomain domain = domains.get(0);

        log.info("下载文件");
        String fileUrl = domain.getImageUrl();
        log.info("文件地址: " + fileUrl);
        if (fileUrl != null) {
            File file = new File(fileUrl);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition",
                        "attachment;fileName=" + fileUrl);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

}
