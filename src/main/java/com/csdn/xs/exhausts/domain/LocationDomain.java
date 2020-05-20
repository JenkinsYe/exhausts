package com.csdn.xs.exhausts.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YJJ
 * @Date: Created in 10:52 2019-12-25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDomain {

    private Double longitude;

    private Double latitude;

    private String locationDescription;

}
