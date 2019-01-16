package ${parentPack}.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import java.util.List;
import com.zengtengpeng.common.utils.PagingUtils;
import ${parentPack}.bean.${tableName};
import ${parentPack}.service.${tableName}Service;
import ${parentPack}.dao.${tableName}Dao;

@Service
public class ${tableName}ServiceImpl implements ${tableName}Service {

	@Resource
	private ${tableName}Dao ${tableValue}Dao;

	
	@Override
	public int deleteByPrimaryKey(${tableName} ${tableValue}){
		return ${tableValue}Dao.deleteByPrimaryKey(${tableValue}.get${primaryKey.javaProperty?cap_first}());
	}

	@Override
	public int insert(${tableName} ${tableValue}){
    	return ${tableValue}Dao.insert(${tableValue});
    }

	@Override
	public ${tableName} selectByPrimaryKey(${tableName} ${tableValue}){
    	return ${tableValue}Dao.selectByPrimaryKey(${tableValue}.get${primaryKey.javaProperty?cap_first}());
    }

   
	@Override
	public List<${tableName}> query${tableName}ByCondition(${tableName} ${tableValue}){
    	return ${tableValue}Dao.query${tableName}ByCondition(${tableValue});
    }

     
	@Override
	public ${tableName} selectAll(${tableName} ${tableValue}){
    	return PagingUtils.queryData(${tableValue}, ${tableValue}Dao, null);
    }

	
	@Override
	public int updateByPrimaryKey(${tableName} ${tableValue}){
    	return ${tableValue}Dao.update${tableName}ByKeyWithNotNull(${tableValue});
    }

	@Override
	public List<${tableName}> export(${tableName} ${tableValue}) {
    return ${tableValue}Dao.selectAll(${tableValue});
    }
}
