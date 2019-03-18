package com.zengtengpeng.relation.oneToMany;

import com.zengtengpeng.autoCode.bean.BuildJavaField;
import com.zengtengpeng.autoCode.bean.BuildJavaMethod;
import com.zengtengpeng.autoCode.config.AutoCodeConfig;
import com.zengtengpeng.relation.bean.RelationTable;
import com.zengtengpeng.relation.build.BuildBaseBean;
import com.zengtengpeng.relation.config.RelationConfig;
import com.zengtengpeng.relation.utils.RelationBuilUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建一对多Bean
 */
@FunctionalInterface
public interface BuildOneToManyBean extends BuildBaseBean {



    /**
     * 主表字段
     * @return
     */
    default List<BuildJavaField> primaryFields(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        RelationTable foreign = relationConfig.getForeign();
        return RelationBuilUtils.getBeanListJavaFields(foreign,autoCodeConfig.getGlobalConfig());
    }



    /**
     * 主表方法
     * @return
     */
    default List<BuildJavaMethod> primaryMethods(AutoCodeConfig autoCodeConfig){
        RelationConfig relationConfig = autoCodeConfig.getGlobalConfig().getRelationConfig();
        return RelationBuilUtils.getBeanListJavaMethods(relationConfig.getForeign());
    }

}
