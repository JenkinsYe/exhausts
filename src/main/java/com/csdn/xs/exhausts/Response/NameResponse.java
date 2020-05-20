package com.csdn.xs.exhausts.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 15:25 2019-12-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NameResponse {

    private String license;

    private String color;

    private Timestamp registerDate;

    private Double totalDistance;

    private String registerType;

    private Timestamp measureDate;

    private Character fuelType;

    private Integer weight;

    private Double discharge;

    private String registerAddress = "萧山";

    private Double yhc;

    private Double yco;

    private Double yno;

    private List<RemoteSenseResponse> remoteSenseResponses = new ArrayList<>();

    private Double assessHc;

    private Double assessCo;

    private Double assessNo;

    private Double Vei;

    private String assessStandard = "评价总分>85";
}
