package cn.com.gnnt.workingdaily.activity;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import cn.com.gnnt.workingdaily.Config;
import cn.com.gnnt.workingdaily.ConfigInfoRepository;
import cn.com.gnnt.workingdaily.Util.DateUtils;
import cn.com.gnnt.workingdaily.Util.ResponseTransformer;
import cn.com.gnnt.workingdaily.Util.WDUtils;
import cn.com.gnnt.workingdaily.VO.DailyItem;
import cn.com.gnnt.workingdaily.VO.WDConfigInfoRespVO;
import cn.com.gnnt.workingdaily.VO.WDInsertDailyReqVO;
import cn.com.gnnt.workingdaily.VO.WDInsertDailyRespVO;
import cn.com.gnnt.workingdaily.VO.WDModifyDailyReqVO;
import cn.com.gnnt.workingdaily.VO.WDModifyDailyRespVO;
import cn.com.gnnt.workingdaily.WDApi;
import gnnt.mebs.base.component.BaseViewModel;
import gnnt.mebs.base.model.BaseRepository;
import gnnt.mebs.common.CommonApp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WDAddDailyViewModel extends BaseViewModel {

    public static final String PAGE_TYPE_ADD = "page_type_add";
    public static final String PAGE_TYPE_MODIFY = "page_type_modify";

    private String pageType;

    public static int LOAD_CONFIG_CODE = 1;
    public static int SAVE_DAILY_CODE = 2;

    public MutableLiveData<String> projectLiveData = new MutableLiveData<>();
    public MutableLiveData<String> workTypeLiveData =  new MutableLiveData<>();
    public MutableLiveData<String> startTimeLiveData = new MutableLiveData<>();
    public MutableLiveData<String> endTimeLiveData = new MutableLiveData<>();
    public MutableLiveData<String> workConetntLiveData = new MutableLiveData<>();

    public MutableLiveData<Boolean> saveBtnEnable = new MutableLiveData<>();

    private ConfigInfoRepository infoRepository;
    private LiveData<WDConfigInfoRespVO> configLiveData;

    private List<String> projectNameList = new ArrayList<>();
    private List<String> workTypeList = new ArrayList<>();

    private DailyItem mDailyItem;

    public WDAddDailyViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    public void init() {
        WDApi api = getApplication().getRetrofitManager().getApi(Config.HOST, WDApi.class);
        infoRepository = (ConfigInfoRepository)((CommonApp)getApplication()).getRepository("ConfigInfo");
        if (infoRepository == null) {
            infoRepository = new ConfigInfoRepository(getApplication(), api);
            ((CommonApp)getApplication()).setRepository("ConfigInfo", infoRepository);
        }
        configLiveData = infoRepository.getData(loadCallback);
        configLiveData.observeForever(new Observer<WDConfigInfoRespVO>() {
            @Override
            public void onChanged(WDConfigInfoRespVO wdConfigInfoRespVO) {
                projectNameList.clear();
                for (WDConfigInfoRespVO.ProjectInfo info : wdConfigInfoRespVO.projectList) {
                    projectNameList.add(info.name);
                }
                workTypeList.clear();
                for (WDConfigInfoRespVO.WorkTypeInfo info : wdConfigInfoRespVO.workTypeList) {
                    workTypeList.add(info.name);
                }
            }
        });
    }

    @Override
    public void refreshDataIfNeed() {
        //查看,直接返回
        if (infoRepository != null) {
            infoRepository.refreshDataIfNeed(loadCallback);
        }
    }

    private BaseRepository.RemoteLoadCallback loadCallback = new BaseRepository.RemoteLoadCallback() {
        @Override
        public void onLoading() {
            setLoadStatus(LOADING, LOAD_CONFIG_CODE);
        }

        @Override
        public void onComplete() {
            setLoadStatus(LOAD_COMPLETE, LOAD_CONFIG_CODE);
        }

        @Override
        public void onError(Throwable e) {
            setLoadStatus(LOAD_ERROR, LOAD_CONFIG_CODE);
            setMessage(e.getMessage());
        }
    };

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public LiveData<WDConfigInfoRespVO> getConfigLiveData() {
        return configLiveData;
    }

    public List<String> getProjectNameList() {
        return projectNameList;
    }

    public List<String> getWorkTypeList() {
        return workTypeList;
    }

    public void initDefaultWithDailyItem(DailyItem item) {
        if (item == null) {
            startTimeLiveData.setValue("09:00");
            endTimeLiveData.setValue(DateUtils.stringFromDate(DateUtils.FORMAT_HH_MM, new Date()));
        }else {
            mDailyItem = item;
            projectLiveData.setValue(item.project);
            workTypeLiveData.setValue(item.workType);
            if (pageType.equals(PAGE_TYPE_ADD)) {//添加
                Date d = DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD_HH_MM, item.endTime);
                //开始时间为最后一条的结束时间
                startTimeLiveData.setValue(DateUtils.stringFromDate(DateUtils.FORMAT_HH_MM, d));
                //结束时间为当前时间
                endTimeLiveData.setValue(DateUtils.stringFromDate(DateUtils.FORMAT_HH_MM, new Date()));
            }else if (pageType.equals(PAGE_TYPE_MODIFY)) {//修改
                Date sd = DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD_HH_MM, item.startTime);
                Date ed = DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD_HH_MM, item.endTime);
                //开始时间
                startTimeLiveData.setValue(DateUtils.stringFromDate(DateUtils.FORMAT_HH_MM, sd));
                //结束时间
                endTimeLiveData.setValue(DateUtils.stringFromDate(DateUtils.FORMAT_HH_MM, ed));
                workConetntLiveData.setValue(item.content);
            }
        }
    }

    public void saveBtnCLick(View view) {
        if (!checkInputs()) {
            return;
        }
        if (pageType.equals(PAGE_TYPE_ADD)) {
            sendAddDailyReq();
        }else if (pageType.equals(PAGE_TYPE_MODIFY)) {
            sendModifyDailyReq();
        }
    }

    private void sendAddDailyReq() {
        WDApi api = getApplication().getRetrofitManager().getApi(Config.HOST, WDApi.class);
        WDInsertDailyReqVO reqVO = new WDInsertDailyReqVO();
        reqVO.sessionID = ((CommonApp)getApplication()).getSessionID();
        WDConfigInfoRespVO.ProjectInfo pInfo = getSelectedProjectInfo();
        reqVO.projectId = pInfo.id;
        reqVO.managerId = pInfo.managerId;
        reqVO.managerName = pInfo.managerName;
        WDConfigInfoRespVO.WorkTypeInfo wInfo = getSelectedWorkTypeInfo();
        reqVO.workTypeId = wInfo.id;
        reqVO.startTime = startTimeLiveData.getValue();
        reqVO.endTime =endTimeLiveData.getValue();
        reqVO.content = workConetntLiveData.getValue();
        api.insertDaily(reqVO)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ResponseTransformer<>())
                .subscribe(new ViewModelSingleObserver<WDInsertDailyRespVO>() {
                    @Override
                    public int getRequestCode() {
                        return SAVE_DAILY_CODE;
                    }
                });
    }

    private void sendModifyDailyReq() {
        WDApi api = getApplication().getRetrofitManager().getApi(Config.HOST, WDApi.class);
        WDModifyDailyReqVO reqVO = new WDModifyDailyReqVO();
        reqVO.sessionID = ((CommonApp)getApplication()).getSessionID();
        reqVO.dailyId = mDailyItem.id;
        WDConfigInfoRespVO.ProjectInfo pInfo = getSelectedProjectInfo();
        reqVO.projectId = pInfo.id;
        reqVO.managerId = pInfo.managerId;
        reqVO.managerName = pInfo.managerName;
        WDConfigInfoRespVO.WorkTypeInfo wInfo = getSelectedWorkTypeInfo();
        reqVO.workTypeId = wInfo.id;
        reqVO.startTime = startTimeLiveData.getValue();
        reqVO.endTime =endTimeLiveData.getValue();
        reqVO.content = workConetntLiveData.getValue();
        api.modifyDaily(reqVO)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ResponseTransformer<>())
                .subscribe(new ViewModelSingleObserver<WDModifyDailyRespVO>() {
                    @Override
                    public int getRequestCode() {
                        return SAVE_DAILY_CODE;
                    }
                });
    }

    private boolean checkInputs() {
        if (TextUtils.isEmpty(projectLiveData.getValue())) {
            setMessage("请选择工作项目");
            return false;
        }else if (TextUtils.isEmpty(workTypeLiveData.getValue())) {
            setMessage("请选择工作性质");
            return false;
        }else {
            Date sd = DateUtils.dateFromDateString(DateUtils.FORMAT_HH_MM, startTimeLiveData.getValue());
            Date ed = DateUtils.dateFromDateString(DateUtils.FORMAT_HH_MM, endTimeLiveData.getValue());
            if (sd.getTime() > ed.getTime()) {
                setMessage("开始时间不能超过结束时间");
                return false;
            }else if (sd.getTime() == ed.getTime()) {
                setMessage("结束时间不能等于开始时间");
                return false;
            }else if (TextUtils.isEmpty(workConetntLiveData.getValue())) {
                setMessage("请填写工作内容");
                return false;
            }
        }
        return true;
    }

    private WDConfigInfoRespVO.ProjectInfo getSelectedProjectInfo() {
        for (WDConfigInfoRespVO.ProjectInfo info : configLiveData.getValue().projectList) {
            if (info.name.equals(projectLiveData.getValue())) {
                return info;
            }
        }
        return null;
    }

    private WDConfigInfoRespVO.WorkTypeInfo getSelectedWorkTypeInfo() {
        for (WDConfigInfoRespVO.WorkTypeInfo info : configLiveData.getValue().workTypeList) {
            if (info.name.equals(workTypeLiveData.getValue())) {
                return info;
            }
        }
        return null;
    }
}
