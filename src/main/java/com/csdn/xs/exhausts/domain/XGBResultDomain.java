package com.csdn.xs.exhausts.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * XGB结果统计实体类
 * @author YJJ
 * @Date: Created in 13:17 2020-06-11
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class XGBResultDomain {

    private Long id;

    private Boolean remoteState;

    private String license;

    private Double cCO;

    private Double cCO2;

    private Double cNO;

    private Double cHC;

    private String testID;

    private Double distance;

    private Double weight;

    private Boolean  testState;

    private Double velHC;

    private Double velCO;

    private Double velNO;

    private Double predictHC;

    private Double predictCO;

    private Double predictNO;

    private Double vei;
}
