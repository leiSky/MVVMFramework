package cn.com.gnnt.workingdaily.VO;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class DailyItem2 implements MultiItemEntity {

    public String title;
    public String time;

    public List<Work> works;

    public static class Work {
        public String id;
        public String projectName;
        public String workTypeName;
        public String startTime;
        public String endTime;
        public String content;
    }

    @Override
    public int getItemType() {
        return 1;
    }
}
