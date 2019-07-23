package cn.com.gnnt.workingdaily.fragment;

import android.app.Application;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import cn.com.gnnt.workingdaily.Config;
import cn.com.gnnt.workingdaily.R;
import cn.com.gnnt.workingdaily.VO.WDLogoutReqVO;
import cn.com.gnnt.workingdaily.VO.WDLogoutRespVO;
import cn.com.gnnt.workingdaily.WDApi;
import cn.com.gnnt.workingdaily.activity.WDChangePwdActivity;
import gnnt.mebs.base.component.BaseViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SettingViewModel extends BaseViewModel {

    public static final String SETTING_CHANGE_PWD = "change_pwd";
    public static final String SETTING_LOGOUT = "logout";

    public static final int LOGOUT_REQUEST_CODE = 1;

    private MutableLiveData<List<SettingMenuItem>> settingItems = new MutableLiveData<>();

    public SettingViewModel(@NonNull Application application) {
        super(application);
        settingItems.setValue(Arrays.asList(
                new SettingMenuItem(R.drawable.change_pwd_img, getApplication().getString(R.string.change_pwd_title), SETTING_CHANGE_PWD, WDChangePwdActivity.class),
                new SettingMenuItem(R.drawable.logout_img, getApplication().getString(R.string.logout_text), SETTING_LOGOUT, null)
        ));

    }

    public MutableLiveData<List<SettingMenuItem>> getSettingItems() {
        return settingItems;
    }

    public void logout() {
        final WDApi api = getApplication().getRetrofitManager().getApi(Config.HOST, WDApi.class);
        WDLogoutReqVO reqVO = new WDLogoutReqVO();
        api.logout(reqVO)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ViewModelSingleObserver<WDLogoutRespVO>(){
                    @Override
                    public int getRequestCode() {
                        return LOGOUT_REQUEST_CODE;
                    }

                    @Override
                    public boolean dispatchErrorToView() {
                        return false;
                    }
                });
    }

    public static class SettingMenuItem {

        public int imageID;
        public String settingName;
        public String tag;
        public Class turnCls;

        public SettingMenuItem(int imageID, String settingName, String tag, Class turnCls) {
            this.imageID = imageID;
            this.settingName = settingName;
            this.tag = tag;
            this.turnCls = turnCls;
        }
    }
}
