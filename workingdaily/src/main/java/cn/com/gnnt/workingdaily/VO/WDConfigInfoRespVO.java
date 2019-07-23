package cn.com.gnnt.workingdaily.VO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.com.gnnt.workingdaily.VO.Base.WDBaseRespVO;

public class WDConfigInfoRespVO extends WDBaseRespVO {

    public WDConfigInfoRespVO(int retCode, String message) {
        super(retCode, message);
    }

    public List<ProjectInfo> projectList;

    public List<WorkTypeInfo> workTypeList;

    public class ProjectInfo {

        @SerializedName("projectId")
        public String id;

        @SerializedName("projectName")
        public String name;

        public String managerId;

        public String managerName;
    }

    public class WorkTypeInfo {

        @SerializedName("optionId")
        public String id;

        @SerializedName("optionName")
        public String name;
    }
}
