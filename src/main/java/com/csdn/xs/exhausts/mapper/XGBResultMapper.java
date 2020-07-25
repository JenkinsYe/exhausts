package com.csdn.xs.exhausts.mapper;

import com.csdn.xs.exhausts.domain.XGBResultDomain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 13:21 2020-06-11
 */
public interface XGBResultMapper {

    List<XGBResultDomain> findALL();

    List<XGBResultDomain> findAfterID(@Param("id") Long id);

    void updateVEIBYID(XGBResultDomain xgbResultDomain);
}
