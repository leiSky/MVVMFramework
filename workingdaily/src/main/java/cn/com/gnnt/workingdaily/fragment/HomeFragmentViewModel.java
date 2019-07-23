package cn.com.gnnt.workingdaily.fragment;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import cn.com.gnnt.workingdaily.Config;
import cn.com.gnnt.workingdaily.Util.ResponseTransformer;
import cn.com.gnnt.workingdaily.VO.DailyItem;
import cn.com.gnnt.workingdaily.VO.WDDeleteDailyReqVO;
import cn.com.gnnt.workingdaily.VO.WDDeleteDailyRespVO;
import cn.com.gnnt.workingdaily.VO.WDTodayDailyReqVO;
import cn.com.gnnt.workingdaily.VO.WDTodayDailyRespVO;
import cn.com.gnnt.workingdaily.VO.WDWeekDailyRespVO;
import cn.com.gnnt.workingdaily.WDApi;
import gnnt.mebs.base.component.BaseViewModel;
import gnnt.mebs.base.http.LoadException;
import gnnt.mebs.common.CommonApp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeFragmentViewModel extends BaseViewModel {

    private static int TOTAL_COUNT = 1000;

    public static int DELETE_DAILY_CODE = 1;

    private MutableLiveData<List<DailyItem>> respLiveData = new MutableLiveData<>();

    public HomeFragmentViewModel(Application application) {
        super(application);
        init();
    }

    @Override
    public void refreshDataIfNeed() {
        if (mRequestObserver.loadStatus != LOADING) {
            init();
        }
    }

    private void init() {
        final WDApi api = getApplication().getRetrofitManager().getApi(Config.HOST, WDApi.class);
        WDTodayDailyReqVO reqVO = new WDTodayDailyReqVO();
        reqVO.sessionID = ((CommonApp)getApplication()).getSessionID();
        api.todayDaily(reqVO)
                .subscribeOn(Schedulers.newThread())
                .compose(new ResponseTransformer<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mRequestObserver);
    }

    private ViewModelSingleObserver<WDTodayDailyRespVO> mRequestObserver = new ViewModelSingleObserver<WDTodayDailyRespVO>() {
        @Override
        public void onSuccess(WDTodayDailyRespVO wdTodayDailyRespVO) {
            super.onSuccess(wdTodayDailyRespVO);
            List<DailyItem> items = new ArrayList<>();
            for (WDTodayDailyRespVO.Daily daily : wdTodayDailyRespVO.dailyList) {
                DailyItem item = new DailyItem();
                item.id = daily.id;
                item.workTypeID = daily.workType.id;
                item.workType = daily.workType.name;
                item.projectID = daily.project.id;
                item.project = daily.project.name;
                item.content = daily.content;
                item.startTime = daily.startTime;
                item.endTime = daily.endTime;
                items.add(item);
            }
            respLiveData.setValue(items);
        }
    };


    public void delteDaily(DailyItem item) {
        final WDApi api = getApplication().getRetrofitManager().getApi(Config.HOST, WDApi.class);
        WDDeleteDailyReqVO reqVO = new WDDeleteDailyReqVO();
        reqVO.sessionID = ((CommonApp)getApplication()).getSessionID();
        reqVO.dailyId = item.id;
        api.deleteDaily(reqVO)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ResponseTransformer<>())
                .subscribe(new ViewModelSingleObserver<WDDeleteDailyRespVO>() {
                    @Override
                    public void onSuccess(WDDeleteDailyRespVO wdDeleteDailyRespVO) {
                        refreshDataIfNeed();
                    }
                });
    }

    public MutableLiveData<List<DailyItem>> getRespLiveData() {
        return respLiveData;
    }
}
