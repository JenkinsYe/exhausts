package com.csdn.xs.exhausts.mapper;


import com.csdn.xs.exhausts.domain.ModelOutputDomain;

import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 17:42 2019-12-07
 */
public interface ModelOutputMapper {

    List<ModelOutputDomain> findAll();
}
