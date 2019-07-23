package cn.com.gnnt.workingdaily;

import cn.com.gnnt.workingdaily.VO.WDConfigInfoReqVO;
import cn.com.gnnt.workingdaily.VO.WDConfigInfoRespVO;
import cn.com.gnnt.workingdaily.VO.WDDeleteDailyReqVO;
import cn.com.gnnt.workingdaily.VO.WDDeleteDailyRespVO;
import cn.com.gnnt.workingdaily.VO.WDHistoryReqVO;
import cn.com.gnnt.workingdaily.VO.WDHistoryRespVO;
import cn.com.gnnt.workingdaily.VO.WDInsertDailyReqVO;
import cn.com.gnnt.workingdaily.VO.WDInsertDailyRespVO;
import cn.com.gnnt.workingdaily.VO.WDLoginReqVO;
import cn.com.gnnt.workingdaily.VO.WDLoginRespVO;
import cn.com.gnnt.workingdaily.VO.WDLogoutReqVO;
import cn.com.gnnt.workingdaily.VO.WDLogoutRespVO;
import cn.com.gnnt.workingdaily.VO.WDModifyDailyReqVO;
import cn.com.gnnt.workingdaily.VO.WDModifyDailyRespVO;
import cn.com.gnnt.workingdaily.VO.WDTodayDailyReqVO;
import cn.com.gnnt.workingdaily.VO.WDTodayDailyRespVO;
import cn.com.gnnt.workingdaily.VO.WDVerifyImageReqVO;
import cn.com.gnnt.workingdaily.VO.WDVerifyImageRespVO;
import cn.com.gnnt.workingdaily.VO.WDWeekDailyReqVO;
import cn.com.gnnt.workingdaily.VO.WDWeekDailyRespVO;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WDApi {

    @POST("VerifyCode")
    Single<WDVerifyImageRespVO>getVerifyCodeImage(@Body WDVerifyImageReqVO reqVO);

    @POST("Login")
    Single<WDLoginRespVO>login(@Body WDLoginReqVO reqVO);

    @POST("Logout")
    Single<WDLogoutRespVO>logout(@Body WDLogoutReqVO reqVO);

    @POST("HistoryDaily")
    Single<WDHistoryRespVO>history(@Body WDHistoryReqVO reqVO);

    @POST("Config")
    Single<WDConfigInfoRespVO>queryConfig(@Body WDConfigInfoReqVO reqVO);

    @POST("TodayDaily")
    Single<WDTodayDailyRespVO>todayDaily(@Body WDTodayDailyReqVO reqVO);

    @POST("InsertDaily")
    Single<WDInsertDailyRespVO>insertDaily(@Body WDInsertDailyReqVO reqVO);

    @POST("UpdateDaily")
    Single<WDModifyDailyRespVO>modifyDaily(@Body WDModifyDailyReqVO reqVO);

    @POST("DeleteDaily")
    Single<WDDeleteDailyRespVO>deleteDaily(@Body WDDeleteDailyReqVO reqVO);

    @POST("WeekDaily")
    Single<WDWeekDailyRespVO>weekDaily(@Body WDWeekDailyReqVO reqVO);
}
