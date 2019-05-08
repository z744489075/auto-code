package com.zengtengpeng.common.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zengtengpeng.common.bean.Page;
import com.zengtengpeng.common.dao.BaseDao;

import java.util.List;

/**
 * base业务层 使用jdk8新特性进行实现方法
 * @param <T>
 * @param <D>
 */
public interface BaseService<T extends Page,D extends BaseDao<T>> {

	/**
	 * 初始化dao
	 * @return
	 */
	D initDao();

	/**
	 * 根据主键删除
	 * @param t
	 * @return
	 */
	default int deleteByPrimaryKey(T t){
		D baseDao = initDao();
		return baseDao.deleteByPrimaryKey(t);
	}

	/**
	 * 新增
	 * @param t
	 * @return
	 */
	default int insert(T t){
		D baseDao = initDao();
		return baseDao.insert(t);
	}

	/**
	 * 更加主键查询
	 * @param t
	 * @return
	 */
	default T selectByPrimaryKey(T t){
		D baseDao = initDao();
		return baseDao.selectByPrimaryKey(t);
	}

	/**
	 * 按照条件查询
	 * @param t
	 * @return
	 */
	default List<T> selectByCondition(T t){
		D baseDao = initDao();
		return baseDao.selectByCondition(t);
	}

	/**
	 * 按照条件查询(调用selectByCondition),只取第一条记录
	 * @param t
	 * @return
	 */
	default T selectByConditionFirst(T t){
		D baseDao = initDao();
		return baseDao.selectByConditionFirst(t);
	}

	/**
	 * 分页查询
	 * @param t
	 * @return
	 */
	default T selectAllByPaging(T t){
		D baseDao = initDao();
		PageHelper.startPage(t.getPage(), t.getPageSize());
		List<T> lists = baseDao.selectAll(t);
		PageInfo pageInfo = new PageInfo(lists);
		t.setRows(lists);
		t.setTotal((new Long(pageInfo.getTotal())).intValue());
		return t;
	}

	/**
	 * 更新
	 * @param t
	 * @return
	 */
	default int update(T t){
		BaseDao<T> baseDao = initDao();
		return baseDao.update(t);
	}

	/**
	 * 查询所有,不分页
	 * @param t
	 * @return
	 */
	default List<T> selectAll(T t) {
		BaseDao<T> baseDao = initDao();
		return baseDao.selectAll(t);
	}

}