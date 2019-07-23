package cn.com.gnnt.workingdaily.fragment;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.MutableLiveData;
import cn.com.gnnt.workingdaily.Config;
import cn.com.gnnt.workingdaily.Util.DateUtils;
import cn.com.gnnt.workingdaily.Util.ResponseTransformer;
import cn.com.gnnt.workingdaily.VO.DailyItem2;
import cn.com.gnnt.workingdaily.VO.WDHistoryRespVO;
import cn.com.gnnt.workingdaily.VO.WDWeekDailyReqVO;
import cn.com.gnnt.workingdaily.VO.WDWeekDailyRespVO;
import cn.com.gnnt.workingdaily.VO.WeekDailyProjectItem;
import cn.com.gnnt.workingdaily.VO.WeekDailyTimeItem;
import cn.com.gnnt.workingdaily.WDApi;
import gnnt.mebs.base.component.BaseViewModel;
import gnnt.mebs.common.CommonApp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WeekDailyViewModel extends BaseViewModel {

    public static int ITEM_STYLE_TIME = 0;
    public static int ITEM_STYLE_PROJECT = 1;

    private MutableLiveData<WDWeekDailyRespVO> respLiveData = new MutableLiveData<>();

    public WeekDailyViewModel(Application application) {
        super(application);
        init();
    }

    private void init() {
        final WDApi api = getApplication().getRetrofitManager().getApi(Config.HOST, WDApi.class);
        WDWeekDailyReqVO reqVO = new WDWeekDailyReqVO();
        reqVO.sessionID = ((CommonApp)getApplication()).getSessionID();
        api.weekDaily(reqVO)
                .subscribeOn(Schedulers.newThread())
                .compose(new ResponseTransformer<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mRequestObserver);
    }

    private ViewModelSingleObserver<WDWeekDailyRespVO> mRequestObserver = new ViewModelSingleObserver<WDWeekDailyRespVO>() {
        @Override
        public void onSuccess(WDWeekDailyRespVO wdWeekDailyRespVO) {
            super.onSuccess(wdWeekDailyRespVO);
            respLiveData.setValue(wdWeekDailyRespVO);
        }
    };

    @Override
    public void refreshDataIfNeed() {
        if (mRequestObserver.loadStatus != LOADING) {
            init();
        }
    }

    public MutableLiveData<WDWeekDailyRespVO> getRespLiveData() {
        return respLiveData;
    }

    public List<MultiItemEntity> getDataItems(int style, List<WDHistoryRespVO.Daily> dailies) {
        if (style == ITEM_STYLE_TIME) {
            return makeData1(dailies);
        }else if (style == ITEM_STYLE_PROJECT) {
            return makeData2(dailies);
        }
        return null;
    }

    private List<MultiItemEntity> makeData1(List<WDHistoryRespVO.Daily> dailies) {//按时间分组
        HashMap<String, List<WeekDailyTimeItem.Work>> map = new HashMap<>();
        for (WDHistoryRespVO.Daily daily : dailies) {
            Date d = DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD_HH_MM, daily.startTime);
            String key = DateUtils.stringFromDate(DateUtils.FORMAT_YYYY_MM_DD, d);
            List<WeekDailyTimeItem.Work> list = map.get(key);
            if (list == null) {
                list = new ArrayList<>();
                map.put(key, list);
            }

            WeekDailyTimeItem.Work work = new WeekDailyTimeItem.Work();
            work.id = daily.id;
            work.workTypeName = daily.workType.name;
            work.projectName = daily.project.name;
            work.startTime = daily.startTime;
            work.endTime = daily.endTime;
            work.content = daily.content;
            list.add(work);
        }
        List<MultiItemEntity> items = new ArrayList<>();
        for (Map.Entry<String, List<WeekDailyTimeItem.Work>> entry : map.entrySet()) {
            WeekDailyTimeItem item = new WeekDailyTimeItem();
            item.title = DateUtils.getWeekOfDate(DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD, entry.getKey()));
            item.time = entry.getKey();
            item.works = entry.getValue();
            items.add(item);
        }
        Collections.sort(items, (o1, o2) -> {
            Date d1 = DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD, ((DailyItem2)o1).time);
            Date d2 = DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD, ((DailyItem2)o2).time);
            return d1.compareTo(d2);
        });
        return items;
    }

    private List<MultiItemEntity> makeData2(List<WDHistoryRespVO.Daily> dailies) {//按项目分组
        HashMap<String, List<WeekDailyProjectItem.Work>> map = new HashMap<>();
        for (WDHistoryRespVO.Daily daily : dailies) {
            String key = daily.project.name;
            List<WeekDailyProjectItem.Work> list = map.get(key);
            if (list == null) {
                list = new ArrayList<>();
                map.put(key, list);
            }

            WeekDailyProjectItem.Work work = new WeekDailyProjectItem.Work();
            work.id = daily.id;
            work.workTypeName = daily.workType.name;
            work.projectName = daily.project.name;
            work.startTime = daily.startTime;
            work.endTime = daily.endTime;
            work.content = daily.content;
            list.add(work);
        }
        List<MultiItemEntity> items = new ArrayList<>();
        for (Map.Entry<String, List<WeekDailyProjectItem.Work>> entry : map.entrySet()) {
            WeekDailyProjectItem item = new WeekDailyProjectItem();
            item.title = entry.getKey();
            item.time = "";
            item.works = entry.getValue();
            items.add(item);
        }
        return items;
    }

    public static class WeekDailyPagerAdpater extends FragmentPagerAdapter {

        private Context mContext;
        private List<String> mTitles;
        private SparseArray<Fragment> mCaches = new SparseArray<>();

        public WeekDailyPagerAdpater(FragmentManager fm, Context context, List<String> titles) {
            super(fm);
            mContext = context;
            mTitles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = mCaches.get(position);
            if (fragment == null){
                fragment = new WDWeekDailyContentFragment();
                Bundle arguments = new Bundle();
                arguments.putInt("page_tag", position);
                fragment.setArguments(arguments);
                mCaches.put(position,fragment);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return mTitles.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }
}
