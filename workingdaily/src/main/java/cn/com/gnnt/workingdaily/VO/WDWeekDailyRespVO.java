package cn.com.gnnt.workingdaily.VO;

import java.util.List;

import cn.com.gnnt.workingdaily.VO.Base.WDBaseRespVO;

public class WDWeekDailyRespVO extends WDBaseRespVO {

    public List<WDHistoryRespVO.Daily> thisWeek;

    public List<WDHistoryRespVO.Daily> lastWeek;

    public WDWeekDailyRespVO(int retCode, String message) {
        super(retCode, message);
    }
}
