package com.csdn.xs.exhausts.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 年检结果
 * @author YJJ
 * @Date: Created in 19:43 2019-12-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultDomain {

    private String measureID;

    private String licenseCard;

    private String licenseType;

    private String recognizeCode;

    private String registerType;

    private String registerAddress;

    private Timestamp registerDate;

    private Character fuelType;

    private String dischargeStandard;

    private Double engineDischarge;

    private Double totalDistance;

    private Timestamp measureDate;

    private String measureMethod;

    private Integer weight;

    private String mdclsbmc;

    private String mdclsblx;

    private Double hc;

    private Double co;

    private Double no;

    private Integer result;
}
