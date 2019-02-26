package com.zengtengpeng.relation.build;

import com.zengtengpeng.autoCode.StartCode;
import com.zengtengpeng.relation.bean.RelationTable;

public interface BuildOneToOne {



    default void build(RelationTable primaryKey, RelationTable foreign, StartCode startCode){

    }
}
