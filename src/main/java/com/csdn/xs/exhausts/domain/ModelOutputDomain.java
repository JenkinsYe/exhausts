package com.csdn.xs.exhausts.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author YJJ
 * @Date: Created in 17:39 2019-12-07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelOutputDomain {

    private Long id;

    private String licensePlate;

    private Timestamp remoteSenseTime;

    private Integer fixture;

    private Double hc;

    private Double so2;

    private Double no;

    private Double pm10;

    private Double pm2p5;

    private Double co;
}
