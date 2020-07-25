package com.csdn.xs.exhausts.mapper;


import com.csdn.xs.exhausts.domain.RemoteSenseDomain;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 16:55 2019-12-07
 */
public interface RemoteSenseMapper {

    List<RemoteSenseDomain> findAll();

    List<RemoteSenseDomain> findTodayRemoteSense();

    List<RemoteSenseDomain> findRemoteSenseHoursAgo(@Param("gap") int n);

    int insertAll( List<RemoteSenseDomain> domains);

    void insert(RemoteSenseDomain domain);

    List<RemoteSenseDomain> findRemoteSenseByLicense(@Param("name") String license);

    List<RemoteSenseDomain> findRemoteSenseByLicenseAndColor(@Param("name") String license,@Param("color") String color);

    List<RemoteSenseDomain> findRemoteSenseByFixture(@Param("fixture") Integer fixture);

    List<RemoteSenseDomain> findNewestRemoteSenseByFixture(@Param("fixture") Integer fixture);

    List<RemoteSenseDomain> findNewestRemoteSense();

    List<RemoteSenseDomain> findRemoteSenseById(@Param("id") Long id);

    Integer findNumberToday(@Param("location") String location);

    List<RemoteSenseDomain> findRemoteByTimeInternal(@Param("t1") Timestamp t1,@Param("t2") Timestamp t2);

    List<RemoteSenseDomain> findRemoteSenseAfterID(@Param("id") Long id);

    List<RemoteSenseDomain> findRemoteSenseAfterIDWithUpperBound(@Param("id") Long id);

    List<Integer> findDistictFixture();

    Integer findPassStateCountByTimeInternalAndFixture(@Param("start") Date start, @Param("end") Date end, @Param("fixture") Integer fixture);

    Integer findUnPassStateCountByTimeInternalAndFixture(@Param("start") Date start, @Param("end") Date end, @Param("fixture") Integer fixture);
}
