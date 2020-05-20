package com.csdn.xs.exhausts.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author YJJ
 * @Date: Created in 19:59 2019-12-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessDomain {

    private String measureID;

    private String licenseCard;

    private String registerType;

    private String registerDistrict;

    private Timestamp registerDate;

    private Character fuelType;

    private Double totalDistance;

    private String measureMethod;

    private Integer weight;

    private Timestamp measureDate;

    private Double velocityPerSecond;

    private Double loadPerSecond;

    private Double hc;

    private Double co;

    private Double no;
}
