package com.csdn.xs.exhausts.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Timestamp;

/**
 * @author YJJ
 * @Date: Created in 17:49 2019-12-07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptimizationDomain {

    private Long id;

    private String licensePlate;

    private Timestamp remoteSenseTime;

    private Integer fixture;

    private Double hc;

    private Double no;


    private Double co;

    private Double vei;

    private String result;

    private String licenseType;

    private String recognizeCode;

    @Override
    public String toString() {
        return "OptimizationDomain{" +
                "id=" + id +
                ", licensePlate='" + licensePlate + '\'' +
                ", remoteSenseTime=" + remoteSenseTime +
                ", fixture=" + fixture +
                ", hc=" + hc +
                ", no=" + no +
                ", co=" + co +
                ", vei=" + vei +
                ", result='" + result + '\'' +
                '}';
    }
}
