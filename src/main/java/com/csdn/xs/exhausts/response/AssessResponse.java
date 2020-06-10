package com.csdn.xs.exhausts.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YJJ
 * @Date: Created in 15:50 2019-12-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessResponse {

    private String license;

    private Double hc;

    private Double no;

    private Double co;

    private Double vei;

    private String result;

    private String assessStandard;
}
