package ${parentPack}.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import java.util.List;
import com.zengtengpeng.common.utils.PagingUtils;
import ${parentPack}.bean.${tableName};
import ${parentPack}.service.${tableName}Service;
import ${parentPack}.dao.${tableName}Dao;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ${tableName}ServiceImpl implements ${tableName}Service {

	@Resource
	private ${tableName}Dao ${tableValue}Dao;


    @Override
    public ${tableName}Dao initDao() {
        return ${tableValue}Dao;
    }
}
