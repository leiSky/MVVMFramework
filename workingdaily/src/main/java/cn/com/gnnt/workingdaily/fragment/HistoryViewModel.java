package cn.com.gnnt.workingdaily.fragment;

import android.app.Application;
import android.view.View;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import cn.com.gnnt.workingdaily.Config;
import cn.com.gnnt.workingdaily.Util.DateUtils;
import cn.com.gnnt.workingdaily.VO.DailyItem;
import cn.com.gnnt.workingdaily.VO.DailyItem2;
import cn.com.gnnt.workingdaily.VO.WDHistoryReqVO;
import cn.com.gnnt.workingdaily.VO.WDHistoryRespVO;
import cn.com.gnnt.workingdaily.WDApi;
import gnnt.mebs.base.component.page.BasePageViewModel;
import gnnt.mebs.base.http.LoadException;
import gnnt.mebs.common.CommonApp;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class HistoryViewModel extends BasePageViewModel<WDHistoryRespVO.Daily> {

    private static final int TOTAL_COUNT = 10000;

    public static int ITEM_STYLE_NORMAL = 0;
    public static int ITEM_STYLE_TIME = 1;

    private int item_style = ITEM_STYLE_TIME;

    public MutableLiveData<String> startDate = new MutableLiveData<>();
    public MutableLiveData<String> endDate = new MutableLiveData<>();

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    private void init() {
        setDefaultDateString(startDate, endDate);
    }

    public int getItem_style() {
        return item_style;
    }

    public void setItem_style(int item_style) {
        if (item_style == this.item_style) {
            return;
        }
        this.item_style = item_style;
        ((MutableLiveData)getData()).setValue(getData().getValue());
    }

    public LiveData<List<MultiItemEntity>> getItemData(){
        return Transformations.map(getData(), new androidx.arch.core.util.Function<List<WDHistoryRespVO.Daily>, List<MultiItemEntity>>() {
            @Override
            public List<MultiItemEntity> apply(List<WDHistoryRespVO.Daily> input) {
                if (item_style == ITEM_STYLE_NORMAL){
                    return makeData1(input);
                }else if (item_style == ITEM_STYLE_TIME) {
                    return makeData2(input);
                }
                return null;
            }
        });
    }

    @Override
    public Single<ListResponse<WDHistoryRespVO.Daily>> onLoad(int pageNO, int pageSize) {
        final WDApi api = getApplication().getRetrofitManager().getApi(Config.HOST, WDApi.class);
        WDHistoryReqVO reqVO = new WDHistoryReqVO();
        reqVO.sessionID = ((CommonApp)getApplication()).getSessionID();
        reqVO.pageNO = pageNO;
        reqVO.pageSize = pageSize;
        reqVO.startDate = startDate.getValue();
        reqVO.endDate = endDate.getValue();
        return api.history(reqVO)
                .map(new Function<WDHistoryRespVO, ListResponse<WDHistoryRespVO.Daily>>() {
                    @Override
                    public ListResponse<WDHistoryRespVO.Daily> apply(WDHistoryRespVO wdHistoryRespVO) throws Exception {
                        if (wdHistoryRespVO.retCode < 0) {
                            throw new LoadException(wdHistoryRespVO.message);
                        }
                        return new ListResponse<>(TOTAL_COUNT, wdHistoryRespVO.dailyList);
                    }
                });
    }
    private List<MultiItemEntity> makeData1(List<WDHistoryRespVO.Daily> dailyItems) {
        List<MultiItemEntity> items = new ArrayList<>();
        for (WDHistoryRespVO.Daily daily : dailyItems) {
            DailyItem item = new DailyItem();
            item.id = daily.id;
            item.workType = daily.workType.name;
            item.project = daily.project.name;
            item.content = daily.content;
            item.startTime = daily.startTime;
            item.endTime = daily.endTime;
            items.add(item);
        }
        return items;
    }


    private List<MultiItemEntity> makeData2(List<WDHistoryRespVO.Daily> dailyItems) {
        HashMap<String, List<DailyItem2.Work>> map = new HashMap<>();
        for (WDHistoryRespVO.Daily daily : dailyItems) {
            Date d = DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD_HH_MM, daily.startTime);
            String key = DateUtils.stringFromDate(DateUtils.FORMAT_YYYY_MM_DD, d);
            List<DailyItem2.Work> list = map.get(key);
            if (list == null) {
                list = new ArrayList<>();
                map.put(key, list);
            }

            DailyItem2.Work work = new DailyItem2.Work();
            work.id = daily.id;
            work.workTypeName = daily.workType.name;
            work.projectName = daily.project.name;
            work.startTime = daily.startTime;
            work.endTime = daily.endTime;
            work.content = daily.content;
            list.add(work);
        }
        List<MultiItemEntity> item2s = new ArrayList<>();
        for (Map.Entry<String, List<DailyItem2.Work>> entry : map.entrySet()) {
            DailyItem2 item2 = new DailyItem2();
            item2.title = entry.getKey();
            item2.time = DateUtils.getWeekOfDate(DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD, entry.getKey()));
            item2.works = entry.getValue();
            item2s.add(item2);
        }
        Collections.sort(item2s, (o1, o2) -> {
            Date d1 = DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD, ((DailyItem2)o1).title);
            Date d2 = DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD, ((DailyItem2)o2).title);
            return d2.compareTo(d1);
        });
        return item2s;
    }

    public void searchBtnClick(View view) {
        loadRefresh();
    }

    private void setDefaultDateString(MutableLiveData<String> preDate, MutableLiveData<String> curDate) {
        long now = System.currentTimeMillis();
        long preDay = now - 24*60*60*1000;//前一天
        String dateString = DateUtils.stringFromDate(DateUtils.FORMAT_YYYY_MM_DD, new Date(preDay));
        curDate.setValue(dateString);
        preDate.setValue(dateString);
    }
}
