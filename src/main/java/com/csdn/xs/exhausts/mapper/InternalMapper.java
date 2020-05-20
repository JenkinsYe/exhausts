package com.csdn.xs.exhausts.mapper;

import com.csdn.xs.exhausts.domain.InternalDomain;

import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 16:26 2019-12-11
 */
public interface InternalMapper {

    List<InternalDomain> findAll();
}
