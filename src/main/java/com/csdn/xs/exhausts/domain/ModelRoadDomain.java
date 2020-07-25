package com.csdn.xs.exhausts.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author YJJ
 * @Date: Created in 16:08 2020-06-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelRoadDomain {

    private Long id;

    private String channelID;

    private Date time;

    private String vehicleType;

    private Double hc;

    private Double co;

    private Double nox;

    private Double so2;

    private Double no;

    private Double no2;

    private Double pm25;

    private Double pm10;
}
