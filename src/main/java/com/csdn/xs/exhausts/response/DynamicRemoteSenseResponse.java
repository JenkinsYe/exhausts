package com.csdn.xs.exhausts.response;

import com.csdn.xs.exhausts.domain.LocationDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author YJJ
 * @Date: Created in 09:18 2019-12-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicRemoteSenseResponse {

    private Long id;

    private LocationDomain location;

    private Integer count;

    private String license;

    private String color;

    private String result;

    private Timestamp passTime;
}
