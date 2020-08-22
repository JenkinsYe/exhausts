package com.csdn.xs.exhausts.service;

import com.csdn.xs.exhausts.domain.ModelRoadDomain;
import com.csdn.xs.exhausts.mapper.ModelRoadMapper;
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
 * 模式道路数据库访问服务
 * @author YJJ
 * @Date: Created in 16:24 2020-06-10
 */
@Service
public class ModelRoadDataService {

    private SqlSessionFactory factory;


    @PostConstruct
    public void init() {
        InputStream in = null;
        try {
            in = Resources.getResourceAsStream("sqlmap/modelsqlmapconfig.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        factory = new SqlSessionFactoryBuilder().build(in);
    }

    public ModelRoadDomain findModelRoadByID(Long id) {
        SqlSession sqlSession = factory.openSession(true);

        try {
            ModelRoadMapper mapper = sqlSession.getMapper(ModelRoadMapper.class);
            return mapper.findModelRoadByID(id);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 根据通道编号查询
     * @param channelID
     * @return
     */
    public List<ModelRoadDomain> findModelRoadByChannelID(String channelID) {
        SqlSession sqlSession = factory.openSession(true);

        try {
            ModelRoadMapper mapper = sqlSession.getMapper(ModelRoadMapper.class);
            return mapper.findModelRoadByChannelID(channelID);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 根据时间间隔查询
     * @param t1
     * @param t2
     * @return
     */
    public List<ModelRoadDomain> findModelRoadByTimeInternal(Date t1, Date t2) {
        SqlSession sqlSession = factory.openSession(true);
        try {
            ModelRoadMapper mapper = sqlSession.getMapper(ModelRoadMapper.class);
            return mapper.findModelRoadByTimeInternal(t1, t2);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 获取所有道路最新尾气浓度记录
     * 可针对单一车型或者全类型
     * @return
     */
    public List<ModelRoadDomain> findNewestModelRoad(String vehicleType) {
        SqlSession sqlSession = factory.openSession(true);
        try {
            ModelRoadMapper mapper = sqlSession.getMapper(ModelRoadMapper.class);
            Date date = mapper.findNewestModelRoadDate();
            if (vehicleType == null || "".equals(vehicleType)) {
                return mapper.findModelRoadByTime(date);
            } else {
                return mapper.findModelRoadByTimeAndVehicleType(date, vehicleType);
            }
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 查询非实时尾气浓度
     * 根据时间和车型查询
     * @param date
     * @param vehicleType
     * @return
     */
    public List<ModelRoadDomain> findModelRoadByTimeAndVehicle(Date date, String vehicleType) {
        SqlSession sqlSession = factory.openSession(true);
        try {
            ModelRoadMapper mapper = sqlSession.getMapper(ModelRoadMapper.class);
            return mapper.findModelRoadByTimeAndVehicleType(date, vehicleType);
        } finally {
            sqlSession.close();
        }
    }


    /**
     * 获取单一车型某种类污染物排名
     * @param date
     * @param vehicleType
     * @param pollutionType
     * @return
     */
    public List<ModelRoadDomain> findModelRoadOrderByPollutionType(Date date, String vehicleType,
                                                                   String pollutionType) {
        SqlSession sqlSession = factory.openSession(true);
        try {
            ModelRoadMapper mapper = sqlSession.getMapper(ModelRoadMapper.class);
            return mapper.findModelRoadOrderByPollutionType(date, vehicleType, pollutionType);
        } finally {
            sqlSession.close();
        }
    }
}
