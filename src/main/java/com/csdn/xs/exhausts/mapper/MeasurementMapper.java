package com.csdn.xs.exhausts.mapper;


import com.csdn.xs.exhausts.domain.MeasurementDomain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 19:08 2019-12-07
 */
public interface MeasurementMapper {

    List<MeasurementDomain> findAll();

    List<MeasurementDomain> findByLicense(@Param("Lisence") String name);

    Integer findMeasurementCountByLicense(@Param("License") String name);

    MeasurementDomain findNearestMeasurementByLicense(@Param("License") String name);
}
