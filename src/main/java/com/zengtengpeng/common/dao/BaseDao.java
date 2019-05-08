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
     * 按照条件查询(调用selectByCondition),只取第一条记录
     * @param t
     * @return
     */
    default T selectByConditionFirst(T t){
        List<T> ts = selectByCondition(t);
        if(ts!=null && ts.size()>0){
            return ts.get(0);
        }
        return null;
    }

    /**
     * 查询所有
     * @return
     */
    List<T> selectAll(T t);

}
