package com.csdn.xs.exhausts.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author YJJ
 * @Date: Created in 16:16 2019-12-07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoteSenseDomain {
    private Long id;

    private String state;

    private Timestamp passTime;

    private String licensePlate;

    private String color;

    private Double co;

    private Double co2;

    private Double no;

    private Double hc;

    private Double ringelmanBlack;

    private Double kValue;

    private Double opacity;

    private Double averageOpacity;

    private Double vsp;

    private Double velocity;

    private Double acceleration;

    private Integer credence;

    private Double windVelocity;

    private Integer windDirection;

    private Double temperature;

    private Double humidity;

    private Integer airPressure;

    private Integer fixture;

    private String monitorPoint;

    private String diselCars;

    private String imageUrl;

    @Override
    public String toString() {
        return "RemoteSenseDomain{" +
                "id=" + id +
                ", state='" + state + '\'' +
                ", passTime=" + passTime +
                ", licensePlate='" + licensePlate + '\'' +
                ", color='" + color + '\'' +
                ", co=" + co +
                ", co2=" + co2 +
                ", no=" + no +
                ", hc=" + hc +
                ", ringelmanBlack=" + ringelmanBlack +
                ", kValue=" + kValue +
                ", opacity=" + opacity +
                ", averageOpacity=" + averageOpacity +
                ", vsp=" + vsp +
                ", velocity=" + velocity +
                ", acceleration=" + acceleration +
                ", credence=" + credence +
                ", windVelocity=" + windVelocity +
                ", windDirection=" + windDirection +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", airPressure=" + airPressure +
                ", fixture=" + fixture +
                ", monitorPoint='" + monitorPoint + '\'' +
                ", diselCars='" + diselCars + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
