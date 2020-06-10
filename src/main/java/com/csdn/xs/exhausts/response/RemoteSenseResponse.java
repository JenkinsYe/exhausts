package com.csdn.xs.exhausts.response;

import com.csdn.xs.exhausts.domain.LocationDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author YJJ
 * @Date: Created in 20:38 2019-12-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoteSenseResponse {

    private Long id;

    private Timestamp remoteTime;

    private Double co2;

    private Double co;

    private Double no;

    private Double hc;

    private Double vsp;

    private Integer fixture;

    private String result;

    private LocationDomain location;
}
