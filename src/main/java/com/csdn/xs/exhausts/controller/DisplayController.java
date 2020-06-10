package com.csdn.xs.exhausts.controller;

import com.csdn.xs.exhausts.Response.*;
import com.csdn.xs.exhausts.domain.*;
import com.csdn.xs.exhausts.service.DataService;
import com.csdn.xs.exhausts.utils.ConstantUtils;
import com.csdn.xs.exhausts.utils.DateUtils;
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
import java.text.ParseException;
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

    @GetMapping("/api/nameList")
    public Result getNameList(@RequestParam("start") String startString, @RequestParam("end") String endString, @RequestParam(value = "amount", required = false) Integer amount) {
        log.info("免检名单请求: " + "开始: " + startString + " end: " + endString + " amount: " + amount);
        Result result = new Result();
        Timestamp start;
        Timestamp end;
        try {
            start = DateUtils.dateStringToTimestamp(startString);
            end = DateUtils.dateStringToTimestamp(endString);
        } catch (ParseException e) {
            e.printStackTrace();
            result.fail();
            result.setMsg("日期无法解析");
            return result;
        }
        List<AssessmentDomain> domains = dataService.findAssessmentByTimeInternal(start, end);
        log.info("免检名单查询完毕");
        if (domains.size() == 0) {
            result.success();
            result.setMsg("该时间段内无免检信息");
            return result;
        }
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
        HashMap<String, Object> map = new HashMap<>();
        map.put("size", responseList.size());
        map.put("nameList", responseList);
        log.info("免检名单完成");
        return result.success(map);
    }



    @GetMapping("/api/measurement")
    public Result getMeasurementByLicense(@RequestParam("license") String name) {
        log.info("车检信息: " + name);
        Result result = new Result();
        MeasurementResponse response = new MeasurementResponse();
        List<ResultDomain> resultDomains = dataService.findResultByLicense(name);
        if (resultDomains.size() == 0) {
            result.success();
            result.setMsg("无该车牌信息");
            return result;
        }
        ResultDomain domain = resultDomains.get(0);
        HashMap<String, Object> map = new HashMap<>();

        map.put("license", domain.getLicenseCard());
        map.put("registerDate", domain.getRegisterDate());
        map.put("registerType", domain.getRegisterType());
        map.put("fuelType", domain.getFuelType());
        map.put("weight", domain.getWeight());
        map.put("co", domain.getCo());
        map.put("no", domain.getNo());
        map.put("hc", domain.getHc());
        map.put("discharge", domain.getEngineDischarge());
        map.put("measureDate", domain.getMeasureDate());
        map.put("result", domain.getResult() == 1 ? "合格" : "不合格");


        return result.success(map);
    }

    @GetMapping("/api/assess")
    public Result getAssess(@RequestParam("license") String license) {
        Result result = new Result();
        List<AssessmentDomain> list = dataService.findAssessmentByLicense(license);
        if (list.size() == 0) {
            result.success();
            result.setMsg("当前车牌号无信息");
            return result;
        }
        AssessmentDomain domain = list.get(0);

        HashMap<String, Object> map = new HashMap<>();
        map.put("license", license);
        map.put("co", domain.getCo());
        map.put("hc", domain.getHc());
        map.put("no", domain.getNo());
        map.put("result", domain.getResult());
        map.put("vei", domain.getVei());
        map.put("standard", "vei > 85");

        return result.success(map);
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


    @GetMapping(value = "/api/remoteSense/img")
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
