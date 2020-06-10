package com.csdn.xs.exhausts.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author YJJ
 * @Date: Created in 10:47 2020-05-12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicDetailRemoteSenseResponse {
    private Long id;

    private Double humidity;

    private Integer airPressure;

    private Double temperature;

    private Integer windDirection;

    private Double windVelocity;

    private String imageUrl;

    private String licensePlate;

    private String color;

    private Double co;

    private Double co2;

    private Double no;

    private Double hc;

    private Double vsp;

    private Double velocity;

    private Double acceleration;

    private String state;

    private Timestamp passTime;

    private Integer fixture;

    private String monitorPoint;

    private Integer locationID;

    private Integer count;
}
