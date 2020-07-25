package com.csdn.xs.exhausts.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author YJJ
 * @Date: Created in 11:13 2019-12-29
 */
public interface ConstantMapper {

    Long findLastFinalRemoteSenseId();

    void updateRemoteSenseId(@Param("id") Long id);

    Long findLastFinalOptimizeId();

    void updateOptimizeId(@Param("id") Long id);

    Long  findLastFinalXGBID();

    void updateLastFinalXGBID(@Param("id") Long id);

    Long findLastNameListVersion();

    void updateNameListVersion(@Param("version") Long version);
}
