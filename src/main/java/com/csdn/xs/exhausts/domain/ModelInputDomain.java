package com.csdn.xs.exhausts.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author YJJ
 * @Date: Created in 15:25 2019-12-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelInputDomain {
    private Timestamp passTime;

    private String licensePlate;

    private Double vsp;

    private Double velocity;

    private Double acceleration;

    private Character fuelType;

    private Timestamp registerDate;

    private Double temperature;

    private Double humidity;
}
