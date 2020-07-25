package com.csdn.xs.exhausts.service;

import com.csdn.xs.exhausts.domain.TrafficDomain;
import com.csdn.xs.exhausts.mapper.TrafficMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 10:58 2020-06-17
 */
@Service
public class TrafficDataService {


    private SqlSessionFactory factory;


    @PostConstruct
    public void init() {
        InputStream in = null;
        try {
            in = Resources.getResourceAsStream("sqlmap/trafficsqlmapconfig.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        factory = new SqlSessionFactoryBuilder().build(in);
    }

    public Integer getFlowByTimeAndChannelAndType(Date start, Date end, String channelID, String vehicleType) {
        SqlSession sqlSession = factory.openSession(true);

        try {
            TrafficMapper mapper = sqlSession.getMapper(TrafficMapper.class);

            return mapper.getFlowByTimeInternalAndChannelIDAndVehicleType(start, end, channelID, vehicleType);
        } finally {
            sqlSession.close();
        }
    }

    public List<TrafficDomain> findTrafficByTimeString(String timeString) {
        SqlSession sqlSession = factory.openSession(true);

        try {
            TrafficMapper mapper = sqlSession.getMapper(TrafficMapper.class);

            return mapper.findTrafficByTimeString(timeString);
        } finally {
            sqlSession.close();
        }
    }
}
