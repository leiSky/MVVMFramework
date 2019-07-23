package cn.com.gnnt.workingdaily.VO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.com.gnnt.workingdaily.VO.Base.WDBaseRespVO;

public class WDTodayDailyRespVO extends WDBaseRespVO {

    public List<Daily> dailyList;

    public WDTodayDailyRespVO(int retCode, String message) {
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

            @SerializedName("optionId")
            public String id;

            @SerializedName("optionName")
            public String name;
        }

        public class Project {
            @SerializedName("optionId")
            public String id;

            @SerializedName("optionName")
            public String name;
        }
    }
}
