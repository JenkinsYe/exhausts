package com.csdn.xs.exhausts.mapper;

import com.csdn.xs.exhausts.domain.ModelRoadDomain;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 16:11 2020-06-10
 */
public interface ModelRoadMapper {

    ModelRoadDomain findModelRoadByID(Long id);

    List<ModelRoadDomain> findModelRoadByChannelID(String channelID);

    List<ModelRoadDomain> findModelRoadByTimeInternal(@Param("t1") Date t1 , @Param("t2") Date t2);

    Date findNewestModelRoadDate();

    List<ModelRoadDomain> findModelRoadByTime(@Param("time") Date time);


    List<ModelRoadDomain> findModelRoadByTimeAndVehicleType(@Param("time") Date time, @Param("vehicleType")String type);

    List<ModelRoadDomain> findModelRoadOrderByPollutionType(@Param("time") Date time, @Param("vehicleType") String vehicleType,
                                                            @Param("pollutionType") String pollutionType);

    List<ModelRoadDomain> findModelRoadByTimeInternalAndVehicleType(@Param("start") Date start,
                                                                    @Param("end") Date end,
                                                                    @Param("vehicle") String type);

    List<ModelRoadDomain> findModelRoadByChannelIDAndTimeInternal(@Param("start") Date start,
                                                                  @Param("end") Date end,
                                                                  @Param("channelID") String channelID);
}
