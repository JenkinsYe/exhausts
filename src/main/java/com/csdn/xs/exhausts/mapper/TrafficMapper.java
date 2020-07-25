package com.csdn.xs.exhausts.mapper;

import com.csdn.xs.exhausts.domain.TrafficDomain;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 10:51 2020-06-17
 */
public interface TrafficMapper {

    Integer getFlowByTimeInternalAndChannelIDAndVehicleType(@Param("start") Date start,
                                                                 @Param("end") Date end,
                                                                 @Param("channelID") String channelID,
                                                                 @Param("vehicleType") String vehicleType);

    List<TrafficDomain> findTrafficByTimeString(@Param("timeString") String dateString);
}
