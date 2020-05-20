package com.csdn.xs.exhausts.mapper;

import com.csdn.xs.exhausts.domain.ProcessDomain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 20:03 2019-12-09
 */
public interface ProcessMapper {

    List<ProcessDomain> findAll();

    List<ProcessDomain> findByID(@Param("name") String name);

    List<ProcessDomain> findByLicense(@Param("license") String license);
}
