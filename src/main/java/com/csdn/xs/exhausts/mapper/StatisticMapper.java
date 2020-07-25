package com.csdn.xs.exhausts.mapper;

import com.csdn.xs.exhausts.domain.RemoteSenseDomain;
import com.csdn.xs.exhausts.domain.StatisticDomain;

import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 11:29 2020-06-16
 */
public interface StatisticMapper {
    StatisticDomain findNewestStatistic();

    StatisticDomain findStatisticByVersion(Long version);

    void saveStatistic(StatisticDomain domain);

}
