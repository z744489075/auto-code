package ${parentPack}.controller;

import javax.annotation.Resource;

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
import org.springframework.stereotype.Controller;
import com.zengtengpeng.common.annotation.Auth;
/**
 * 
 * @author zengtp
 *
 */
@Controller
public class ${tableName}Controller {
	
	@Resource
	private ${tableName}Service ${tableValue}Service;

	/**
	 * 删除
	 * @param ${tableValue}
	 * @return
	 */
	@RequestMapping("/${tableValue}/deleteByPrimaryKey")
	@ResponseBody
	public DataRes deleteByPrimaryKey(${tableName} ${tableValue}, HttpServletRequest request, HttpServletResponse response){
		return DataRes.success(${tableValue}Service.deleteByPrimaryKey(${tableValue}));
	}

    /**
	 * 保存 如果id存在则修改否则新增
	 * @param ${tableValue}
	 * @return
	 */
	@RequestMapping("/${tableValue}/save")
	@ResponseBody
	public DataRes save(${tableName} ${tableValue}, HttpServletRequest request, HttpServletResponse response){
		if(${tableValue}.get${primaryKey.javaProperty?cap_first}()==null){
			return DataRes.success(${tableValue}Service.insert(${tableValue}));
		}
		return DataRes.success(${tableValue}Service.update(${tableValue}));
	}

    /**
     * 根据主键查询
     * @param ${tableValue}
     * @return
     */
	@RequestMapping("/${tableValue}/selectByPrimaryKey")
	@ResponseBody
	public DataRes selectByPrimaryKey(${tableName} ${tableValue}, HttpServletRequest request, HttpServletResponse response){
    	return DataRes.success(${tableValue}Service.selectByPrimaryKey(${tableValue}));
    }

    /**
	* 根据条件查询
	*/
	@RequestMapping("/${tableValue}/query${tableName}ByCondition")
	@ResponseBody
	public DataRes queryByCondition(${tableName} ${tableValue}, HttpServletRequest request, HttpServletResponse response){
    	return DataRes.success(${tableValue}Service.queryByCondition(${tableValue}));
    }

   /**
	* 分页查询
	* @param ${tableValue} 参数
	* @return
	*/
	@RequestMapping("/${tableValue}/selectAll")
	@ResponseBody
	public DataRes selectAll(${tableName} ${tableValue},HttpServletRequest request, HttpServletResponse response){
    	return DataRes.success(${tableValue}Service.selectAllByPaging(${tableValue}));
    }

	/**
	* 导出数据
	* @param tests 参数
	* @return
	*/
	@RequestMapping("/${tableValue}/export")
	public void export(${tableName} ${tableValue},HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<${tableName}> list= ${tableValue}Service.selectAll(${tableValue});
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

	/**
	* 跳转到列表页面
	* @return
	*/
	@RequestMapping("/${tableValue}/gotoList")
	public String gotoList(${tableName} ${tableValue}, HttpServletRequest request, HttpServletResponse response){
		return "${mobelName}/${dataName}_list";
	}

	/**
	* 跳转到详情页面
	* @return
	*/
	@RequestMapping("/${tableValue}/gotoDetail")
	@Auth("${tableValue}/save")
	public String gotoDetail(${tableName} ${tableValue}, HttpServletRequest request, HttpServletResponse response){
		if(${tableValue}.get${primaryKey.javaProperty?cap_first}()!=null){
			request.setAttribute("${dataName}",${tableValue}Service.selectByPrimaryKey(${tableValue}));
		}else {
			request.setAttribute("${dataName}",${tableValue});
		}
		return "${mobelName}/${dataName}_detail";
	}
}
