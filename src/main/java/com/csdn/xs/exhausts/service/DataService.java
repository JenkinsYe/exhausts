package com.csdn.xs.exhausts.service;


import com.csdn.xs.exhausts.domain.*;
import com.csdn.xs.exhausts.mapper.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 16:46 2019-12-07
 */
@Service
public class DataService {
    private SqlSessionFactory factory;
    private RemoteSenseMapper remoteSenseMapper;
    private ModelOutputMapper modelOutputMapper;
    private OptimizationMapper optimizationMapper;
    private AssessmentMapper assessmentMapper;
    private MeasurementMapper measurementMapper;
    private ResultMapper resultMapper;
    private ProcessMapper processMapper;
    private InternalMapper internalMapper;
    private ConstantMapper constantMapper;

    @PostConstruct
    public void init() {
        InputStream in = null;
        try {
            in = Resources.getResourceAsStream("sqlmap/sqlmapconfig.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        factory = new SqlSessionFactoryBuilder().build(in);
    }

    public List<RemoteSenseDomain> findRemoteSenseHoursAgo(int n) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            remoteSenseMapper = sqlSession.getMapper(RemoteSenseMapper.class);
            List<RemoteSenseDomain> list = remoteSenseMapper.findRemoteSenseHoursAgo(n);
            return list;
        } finally {
            sqlSession.close();
        }

    }

    public List<MeasurementDomain> findMeasurementByLicense(String license) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            measurementMapper = sqlSession.getMapper(MeasurementMapper.class);
            return measurementMapper.findByLicense(license);
        } finally {
            sqlSession.close();
        }
    }

    public void insertAllRemoteSense(List<RemoteSenseDomain> domains) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            remoteSenseMapper = sqlSession.getMapper(RemoteSenseMapper.class);
            remoteSenseMapper.insertAll(domains);
        } finally {
            sqlSession.close();
        }
    }

    public void saveOptimization(OptimizationDomain domain) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            optimizationMapper = sqlSession.getMapper(OptimizationMapper.class);
            optimizationMapper.saveOptimization(domain);
        } finally {
            sqlSession.close();
        }
    }

    public Integer findMeasurementCountByName(String name) {
        return measurementMapper.findMeasurementCountByLicense(name);
    }


    public void insertAllAssessment(List<AssessmentDomain> list) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            assessmentMapper = sqlSession.getMapper(AssessmentMapper.class);
            assessmentMapper.insertAll(list);
        } finally {
            sqlSession.close();
        }
    }

    public List<AssessmentDomain> findAssessmentByTimeInternal(Timestamp t1, Timestamp t2) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            assessmentMapper = sqlSession.getMapper(AssessmentMapper.class);
            return assessmentMapper.findByTimeInternal(t1, t2);
        } finally {
            sqlSession.close();
        }
    }

    public List<ResultDomain> findAllResult() {
        return resultMapper.findAll();
    }

    public ResultDomain findResultById(String id) {
        return resultMapper.findResultById(id);
    }

    public List<ProcessDomain> findAllProcess() {
        return processMapper.findAll();
    }

    public List<ProcessDomain> findProcessByID(String name) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            processMapper = sqlSession.getMapper(ProcessMapper.class);
            return processMapper.findByID(name);
        } finally {
            sqlSession.close();
        }
    }

    public List<ProcessDomain> findProcessByLicense(String license) {
        return processMapper.findByLicense(license);
    }

    public List<RemoteSenseDomain> findRemoteSenseByLicense(String license){
        return remoteSenseMapper.findRemoteSenseByLicense(license);
    }

    public List<ResultDomain> findResultByLicense( String license) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            resultMapper = sqlSession.getMapper(ResultMapper.class);
            return resultMapper.findResultByLicense(license);
        } finally {
            sqlSession.close();
        }
    }

    public List<AssessmentDomain> findAssessmentByLicense(String license) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            assessmentMapper = sqlSession.getMapper(AssessmentMapper.class);
            return assessmentMapper.findAssessmentByLicense(license);
        } finally {
            sqlSession.close();
        }

    }

    public List<InternalDomain> findAllInternal() {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            internalMapper = sqlSession.getMapper(InternalMapper.class);
            return internalMapper.findAll();
        } finally {
            sqlSession.close();
        }
    }

    public void saveOptimizations(List<OptimizationDomain> domains) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            optimizationMapper = sqlSession.getMapper(OptimizationMapper.class);
            optimizationMapper.saveOptimizations(domains);
        } finally {
            sqlSession.close();
        }
    }

    public void saveRemoteSense(RemoteSenseDomain domain) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            remoteSenseMapper = sqlSession.getMapper(RemoteSenseMapper.class);
            remoteSenseMapper.insert(domain);
        } finally {
            sqlSession.close();
        }
    }

    public List<RemoteSenseDomain> findRemoteSenseByLicenseAndColor(String license, String color) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            remoteSenseMapper = sqlSession.getMapper(RemoteSenseMapper.class);
            return remoteSenseMapper.findRemoteSenseByLicenseAndColor(license, color);
        } finally {
            sqlSession.close();
        }
    }

    public RemoteSenseDomain findNewestRemoteSenseByFixture(Integer fixture) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            remoteSenseMapper = sqlSession.getMapper(RemoteSenseMapper.class);
            List<RemoteSenseDomain> remoteSenseDomains = remoteSenseMapper.findNewestRemoteSenseByFixture(fixture);
            if (remoteSenseDomains.size() == 0)
                return null;
            return remoteSenseDomains.get(0);
        } finally {
            sqlSession.close();
        }
    }

    public RemoteSenseDomain findNewestRemoteSense() {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            remoteSenseMapper = sqlSession.getMapper(RemoteSenseMapper.class);
            List<RemoteSenseDomain> remoteSenseDomains = remoteSenseMapper.findNewestRemoteSense();
            if (remoteSenseDomains.size() == 0)
                return null;
            return remoteSenseDomains.get(0);
        } finally {
            sqlSession.close();
        }

    }

    public Integer findNumberOfRemoteSenseToday(String location) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            remoteSenseMapper = sqlSession.getMapper(RemoteSenseMapper.class);
            return remoteSenseMapper.findNumberToday(location);
        } finally {
            sqlSession.close();
        }
    }

    public List<OptimizationDomain> findOptimizeHoursAgo(Integer gap) {
        return optimizationMapper.findOptimizeHoursAgo(gap);
    }

    public List<ResultDomain> findResultByCode(String code) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            resultMapper = sqlSession.getMapper(ResultMapper.class);
            return resultMapper.findResultByCode(code);
        } finally {
            sqlSession.close();
        }
    }

    public List<RemoteSenseDomain> findRemoteById(Long id) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            remoteSenseMapper = sqlSession.getMapper(RemoteSenseMapper.class);
            return remoteSenseMapper.findRemoteSenseById(id);
        } finally {
            sqlSession.close();
        }
    }

    public List<RemoteSenseDomain> findRemoteByTimeIntergnal(Timestamp t1, Timestamp t2) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            remoteSenseMapper = sqlSession.getMapper(RemoteSenseMapper.class);
            return remoteSenseMapper.findRemoteByTimeInternal(t1, t2);
        } finally {
            sqlSession.close();
        }
    }

    public Integer findResultCountByCode(String code) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            resultMapper = sqlSession.getMapper(ResultMapper.class);
            return resultMapper.findResultCountByCode(code);
        } finally {
            sqlSession.close();
        }
    }

    public Long findLastRemoteSenseId() {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            constantMapper = sqlSession.getMapper(ConstantMapper.class);
            return constantMapper.findLastFinalRemoteSenseId();
        } finally {
            sqlSession.close();
        }

    }

    public void updateRemoteSenseId(Long id) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            constantMapper = sqlSession.getMapper(ConstantMapper.class);
            constantMapper.updateRemoteSenseId(id);
        } finally {
            sqlSession.close();
        }
    }

    public Long findLastOptimizeId() {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            constantMapper = sqlSession.getMapper(ConstantMapper.class);
            return constantMapper.findLastFinalOptimizeId();
        } finally {
            sqlSession.close();
        }
    }

    public void updateOptimizeId(Long id) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            constantMapper = sqlSession.getMapper(ConstantMapper.class);
            constantMapper.updateOptimizeId(id);
        } finally {
            sqlSession.close();
        }
    }

    public List<RemoteSenseDomain> findRemoteSenseAfterID(Long id) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            remoteSenseMapper = sqlSession.getMapper(RemoteSenseMapper.class);
            return remoteSenseMapper.findRemoteSenseAfterID(id);
        } finally {
            sqlSession.close();
        }
    }

    public List<OptimizationDomain> findOptimizeAfterID(Long id) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            optimizationMapper = sqlSession.getMapper(OptimizationMapper.class);
            return optimizationMapper.findOptimizeAfterID(id);
        } finally {
            sqlSession.close();
        }
    }

    public List<RemoteSenseDomain> findRemoteSenseAfterIDWithUpperBound(Long id) {
        SqlSession sqlSession;
        sqlSession = factory.openSession(true);
        try {
            remoteSenseMapper = sqlSession.getMapper(RemoteSenseMapper.class);
            return remoteSenseMapper.findRemoteSenseAfterIDWithUpperBound(id);
        } finally {
            sqlSession.close();
        }
    }
}
