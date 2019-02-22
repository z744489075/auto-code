package com.zengtengpeng.common.dao;

import java.util.List;

public interface BaseDao<T> {

    /**
     * 新增
     * @param t
     * @return
     */
    int insert(T t);

    /**
     * 根据主键删除
     * @param t
     * @return
     */
    int deleteByPrimaryKey(T t);

    /**
     * 更新(忽略null)
     */
    Integer update(T t);

    /**
     * 根据主键查询
     * @param t
     * @return
     */
    T selectByPrimaryKey(T t);

    /**
     * 根据条件查询
     */
    List<T> selectByCondition(T t);

    /**
     * 查询所有
     * @return
     */
    List<T> selectAll(T t);

}
