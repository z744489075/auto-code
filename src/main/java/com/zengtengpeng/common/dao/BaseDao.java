package com.zengtengpeng.common.dao;

import java.util.List;

public interface BaseDao<T> {
    int deleteByPrimaryKey(T t);

    int insert(T t);

    T selectByPrimaryKey(T t);

    /**
     * 根据条件查询
     */
    List<T> queryByCondition(T t);

    /**
     * 更新(忽略null)
     */
    Integer update(T t);

    /**
     * 分页查询
     * @return
     */
    List<T> selectAll(T t);

}
