package com.csdn.xs.exhausts.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 评价结果实体类
 * @author YJJ
 * @Date: Created in 17:58 2019-12-07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentDomain {

    private Long id;

    private String licensePlate;

    private Timestamp assessTime;

    private Double vei;

    private String result;

    private Integer numberOfRemoteSense;

    private Integer numberOfMeasurement;

    private Double co;

    private Double no;

    private Double hc;

    private String licenseType;

    private String recognizeCode;

    private Integer nextMeasure;

    private Long nameListVersion;
}
