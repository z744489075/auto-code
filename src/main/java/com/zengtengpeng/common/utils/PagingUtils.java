package com.zengtengpeng.common.utils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

import com.zengtengpeng.common.bean.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


public class PagingUtils {

	static Logger logger = LoggerFactory.getLogger(PagingUtils.class);
	
	public static <T extends Page> T queryData(T t, Object pagingDao, String methodName){
		try {
			if (methodName==null) {
				methodName="selectAll";
			}
			Method method = pagingDao.getClass().getMethod(methodName, t.getClass());
			// 设置分页信息
			PageHelper.startPage(t.getPage(), t.getPageSize());
			List invoke = (List) method.invoke(pagingDao, t);
			PageInfo pageInfo = new PageInfo(invoke);
			// 设置返回结果
			t.setRows(invoke);
			t.setTotal(new Long(pageInfo.getTotal()).intValue());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return t;
	}
	
	private static int getCount(Object countsObj) {
		int count = 0;
		if (countsObj instanceof BigDecimal) {
			count = ((BigDecimal) countsObj).intValue();
		} else if (countsObj instanceof Long) {
			count = ((Long) countsObj).intValue();
		} else if (countsObj instanceof Integer) {
			count = ((Integer) countsObj).intValue();
		} else {
			logger.error("obj is error , the obj is :" + countsObj);
		}
		return count;
	}
}
