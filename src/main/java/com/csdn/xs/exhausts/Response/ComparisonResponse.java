package com.csdn.xs.exhausts.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YJJ
 * @Date: Created in 11:25 2019-12-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComparisonResponse {

    private Double hc;

    private Double no;

    private Double co;

    private Double hcWithoutChecking;

    private Double noWithoutChecking;

    private Double coWithoutChecking;
}
