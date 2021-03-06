package com.csdn.xs.exhausts.mapper;

import com.csdn.xs.exhausts.domain.MeasurementStatisticDomain;
import com.csdn.xs.exhausts.domain.ResultDomain;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 19:46 2019-12-09
 */
public interface ResultMapper {

    List<ResultDomain> findAll();

    ResultDomain findResultById(@Param("id") String id);

    List<ResultDomain> findResultByLicense(@Param("license") String license);

    List<ResultDomain> findResultByCode(@Param("code") String code);

    Integer findResultCountByCode(@Param("code") String code);

    MeasurementStatisticDomain findResultCountByDistanceInternal(@Param("start") Long start, @Param("end") Long end);

    Integer findResultCountByMeasurementDateInternal(@Param("start")Date start, @Param("end") Date end);

    MeasurementStatisticDomain findResultCountByYear(@Param("year1") int year1, @Param("year2") int year2);
}
