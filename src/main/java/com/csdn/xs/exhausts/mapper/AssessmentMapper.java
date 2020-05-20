package com.csdn.xs.exhausts.mapper;

import com.csdn.xs.exhausts.domain.AssessmentDomain;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 17:59 2019-12-07
 */
public interface AssessmentMapper {

    List<AssessmentDomain> findAll();

    void insertAll(List<AssessmentDomain> list);

    List<AssessmentDomain> findAssessmentByLicense(@Param("license") String license);

    List<AssessmentDomain> findByTimeInternal(@Param("t1") Timestamp t1,@Param("t2") Timestamp t2);
}
