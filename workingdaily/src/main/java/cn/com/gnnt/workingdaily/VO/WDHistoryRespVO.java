package cn.com.gnnt.workingdaily.VO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.com.gnnt.workingdaily.VO.Base.WDBaseRespVO;

public class WDHistoryRespVO extends WDBaseRespVO {

    public List<Daily> dailyList;

    public WDHistoryRespVO(int retCode, String message) {
        super(retCode, message);
    }

    public class Daily {

        @SerializedName("dailyId")
        public String id;

        public WorkType workType;

        public Project project;

        public String content;

        public String startTime;

        public String endTime;

        public class WorkType {

            @SerializedName("optionName")
            public String name;
        }

        public class Project {

            @SerializedName("optionName")
            public String name;
        }
    }
}
