package com.csdn.xs.exhausts.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author YJJ
 * @Date: Created in 20:21 2019-12-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementResponse {

    private String license;

    private String registerType;

    private Timestamp registerDate;

    private Character fuelType;

    private Integer weight;

    private Double hc;

    private Double co;

    private Double no;

    private Double discharge;

    private String result;
}
