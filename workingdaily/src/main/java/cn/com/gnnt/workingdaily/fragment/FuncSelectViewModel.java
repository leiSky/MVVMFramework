package cn.com.gnnt.workingdaily.fragment;

import android.app.Application;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import cn.com.gnnt.workingdaily.R;
import gnnt.mebs.base.component.BaseViewModel;

public class FuncSelectViewModel extends BaseViewModel {

    public static final String WD_TODAY = "today";
    public static final String WD_HISTORY = "history";
    public static final String WD_WEEK = "week";
    public static final String WD_SETTING = "setting";
    public static final String WD_ABOUT = "about";

    private MutableLiveData<List<FuncMenuItem>> funcItems = new MutableLiveData<>();

    public FuncSelectViewModel(@NonNull Application application) {
        super(application);
        funcItems.setValue(Arrays.asList(
                new FuncMenuItem(R.drawable.current_img, getApplication().getString(R.string.home_page_title), WD_TODAY),
                new FuncMenuItem(R.drawable.history_img, getApplication().getString(R.string.history_page_title), WD_HISTORY),
                new FuncMenuItem(R.drawable.ic_week_daily, getApplication().getString(R.string.week_daily_title), WD_WEEK),
                new FuncMenuItem(R.drawable.about_img, getApplication().getString(R.string.about_page_title), WD_ABOUT),
                new FuncMenuItem(R.drawable.setting_img, getApplication().getString(R.string.setting_page_title), WD_SETTING)
        ));

    }

    public MutableLiveData<List<FuncMenuItem>> getFuncItems() {
        return funcItems;
    }

    public static class FuncMenuItem {

        public int imageID;
        public String funcName;
        public String tag;

        public FuncMenuItem(int imageID, String funcName, String tag) {
            this.imageID = imageID;
            this.funcName = funcName;
            this.tag = tag;
        }
    }
}
