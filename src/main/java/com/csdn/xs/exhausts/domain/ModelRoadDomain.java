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

    @Override
    public String toString() {
        return "ModelRoadDomain{" +
                "id=" + id +
                ", channelID='" + channelID + '\'' +
                ", time=" + time +
                ", vehicleType='" + vehicleType + '\'' +
                ", hc=" + hc +
                ", co=" + co +
                ", nox=" + nox +
                ", so2=" + so2 +
                ", no=" + no +
                ", no2=" + no2 +
                ", pm25=" + pm25 +
                ", pm10=" + pm10 +
                '}';
    }
}
