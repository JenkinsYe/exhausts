package com.csdn.xs.exhausts.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author YJJ
 * @Date: Created in 11:25 2020-06-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDomain {
    Long id;

    Long nameListVersion;

    Double wcNO;

    Double wcCO;

    Double wcHC;

    Double cNO;

    Double cCO;

    Double cHC;

    Double rsNO;

    Double rsCO;

    Double rsHC;

    Date rsStartTime;

    Date rsEndTime;

    Double wcRate;
}
