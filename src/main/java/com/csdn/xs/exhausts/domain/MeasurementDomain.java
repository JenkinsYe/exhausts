package com.csdn.xs.exhausts.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author YJJ
 * @Date: Created in 19:00 2019-12-07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementDomain {


    private String licensePlate;

    private String registerType;

    private String registerDistrict;

    private Timestamp registerDate;

    private Character fuelType;

    private String dischargeStandard;

    private Integer engineDisplacement;

    private Integer totalDistance;

    private String measureMethod;

    private Character fixtureName;

    private Character fixtureType;

    private Integer weight;

    private Timestamp measureDate;

    private Double velocityPerSecond;

    private Double loadPerSecond;

    private Double hc;

    private Double co;

    private Double no;
}
