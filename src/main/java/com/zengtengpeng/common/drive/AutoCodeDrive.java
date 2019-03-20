package com.zengtengpeng.common.drive;

import com.zengtengpeng.autoCode.StartCode;
import com.zengtengpeng.relation.manyToMany.BuildManyToMany;
import com.zengtengpeng.relation.oneToMany.BuildOneToMany;
import com.zengtengpeng.relation.oneToOne.BuildOneToOne;

/**
 * 生成代码驱动管理
 */
public class AutoCodeDrive {
    private static StartCode startCode=t->{};
    private static BuildOneToOne buildOneToOne=t->{};
    private static BuildOneToMany buildOneToMany=t->{};
    private static BuildManyToMany buildManyToMany=t->{};

    public static StartCode getStartCode() {
        return startCode;
    }

    public static void setStartCode(StartCode startCode) {
        AutoCodeDrive.startCode = startCode;
    }

    public static BuildOneToOne getBuildOneToOne() {
        return buildOneToOne;
    }

    public static void setBuildOneToOne(BuildOneToOne buildOneToOne) {
        AutoCodeDrive.buildOneToOne = buildOneToOne;
    }

    public static BuildOneToMany getBuildOneToMany() {
        return buildOneToMany;
    }

    public static void setBuildOneToMany(BuildOneToMany buildOneToMany) {
        AutoCodeDrive.buildOneToMany = buildOneToMany;
    }

    public static BuildManyToMany getBuildManyToMany() {
        return buildManyToMany;
    }

    public static void setBuildManyToMany(BuildManyToMany buildManyToMany) {
        AutoCodeDrive.buildManyToMany = buildManyToMany;
    }
}
