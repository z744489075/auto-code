package ${parentPack}.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;
import com.zengtengpeng.common.utils.ExcelUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.zengtengpeng.common.bean.DataRes;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import ${parentPack}.bean.${tableName};
import ${parentPack}.service.${tableName}Service;

/**
 * 
 * @author zengtp
 *
 */
@RestController
public class ${tableName}Controller {
	
	@Resource
	private ${tableName}Service ${tableValue}Service;

	/**
	 * 删除
	 * @param ${tableValue}
	 * @return
	 */
	@RequestMapping("/${tableValue}/deleteByPrimaryKey")
	public DataRes deleteByPrimaryKey(${tableName} ${tableValue}, HttpServletRequest request, HttpServletResponse response){
		return DataRes.success(${tableValue}Service.deleteByPrimaryKey(${tableValue}));
	}

    /**
	 * 保存 如果id存在则修改否则新增
	 * @param ${tableValue}
	 * @return
	 */
	@RequestMapping("/${tableValue}/save")
	public DataRes save(${tableName} ${tableValue}, HttpServletRequest request, HttpServletResponse response){
		if(${tableValue}.getId()==null){
			return DataRes.success(${tableValue}Service.insert(${tableValue}));
		}
		return DataRes.success(${tableValue}Service.updateByPrimaryKey(${tableValue}));
	}

    /**
     * 根据主键查询
     * @param ${tableValue}
     * @return
     */
	@RequestMapping("/${tableValue}/selectByPrimaryKey")
	public DataRes selectByPrimaryKey(${tableName} ${tableValue}, HttpServletRequest request, HttpServletResponse response){
    	return DataRes.success(${tableValue}Service.selectByPrimaryKey(${tableValue}));
    }

    /**
	* 根据条件查询
	*/
	@RequestMapping("/${tableValue}/query${tableName}ByCondition")
	public DataRes query${tableName}ByCondition(${tableName} ${tableValue}, HttpServletRequest request, HttpServletResponse response){
    	return DataRes.success(${tableValue}Service.query${tableName}ByCondition(${tableValue}));
    }

   /**
	* 分页查询
	* @param ${tableValue} 参数
	* @return
	*/
	@RequestMapping("/${tableValue}/selectAll")
	public DataRes selectAll(${tableName} ${tableValue},HttpServletRequest request, HttpServletResponse response){
    	return DataRes.success(${tableValue}Service.selectAll(${tableValue}));
    }

	/**
	* 导出数据
	* @param tests 参数
	* @return
	*/
	@RequestMapping("/${tableValue}/export")
	public void export(${tableName} ${tableValue},HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<${tableName}> list= ${tableValue}Service.export(${tableValue});
		Map<String, String> header = new LinkedHashMap<>();
		<#list allColumns as c>
			<#if c.jdbcTypeName='TIMESTAMP' || c.jdbcTypeName='DATE' || cons[c.javaProperty]??>
				header.put("${c.javaProperty}_", "${c.remarks?json_string}");
			<#else >
				header.put("${c.javaProperty}", "${c.remarks?json_string}");
			</#if>
		</#list>
		ExcelUtils.exportExcel("${tableRemarks}",header,list,response,request);
    }

}
