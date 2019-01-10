package ${parentPack}.service;

import ${parentPack}.bean.${tableName};
import java.util.List;
public interface ${tableName}Service {

	/**
	 * 删除
	 * @param ${tableValue}
	 * @return
	 */
	public abstract int deleteByPrimaryKey(${tableName} ${tableValue});

	/**
	 * 新增
	 * @param ${tableValue}
	 * @return
	 */
	public abstract int insert(${tableName} ${tableValue});

	/**
	 * 根据主键查询
	 * @param ${tableValue}
	 * @return
	 */
	public abstract ${tableName} selectByPrimaryKey(${tableName} ${tableValue});

	/**
	 * 根据条件查询
	 */
	public abstract List<${tableName}> query${tableName}ByCondition(${tableName} ${tableValue});

	/**
	 * 分页查询
	 * @param ${tableValue} 参数
	 * @return
	 */
	public abstract ${tableName} selectAll(${tableName} ${tableValue});

	/**
	 * 根据主键更新
	 * @param record
	 * @return
	 */
	public abstract int updateByPrimaryKey(${tableName} ${tableValue});

	/**
	 * 导出所有数据
	 * @param tests
	 * @return
	 */
	List<${tableName}> export(${tableName} ${tableValue});

}