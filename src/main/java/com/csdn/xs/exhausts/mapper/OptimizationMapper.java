package com.csdn.xs.exhausts.mapper;


import com.csdn.xs.exhausts.domain.OptimizationDomain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 17:52 2019-12-07
 */
public interface OptimizationMapper {

    List<OptimizationDomain> findAll();

    List<OptimizationDomain> findCurrentMonthData();

    void saveOptimization(@Param("domain") OptimizationDomain domain);


    void saveOptimizations(List<OptimizationDomain> domains);

    List<OptimizationDomain> findOptimizeHoursAgo(@Param("gap") Integer gap);

    List<OptimizationDomain> findOptimizeAfterID(@Param("id") Long id);
}
