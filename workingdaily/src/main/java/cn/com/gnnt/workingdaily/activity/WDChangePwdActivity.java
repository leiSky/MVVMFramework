package cn.com.gnnt.workingdaily.activity;

import android.app.Application;

import androidx.annotation.NonNull;
import butterknife.OnClick;
import cn.com.gnnt.workingdaily.R;
import cn.com.gnnt.workingdaily.activity.Base.WDBaseActivity;
import gnnt.mebs.base.component.BaseViewModel;

public class WDChangePwdActivity extends WDBaseActivity<WDChangePwdActivity.WDChangePwdViewModel> {
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_wdchange_pwd;
    }

    @NonNull
    @Override
    protected Class<? extends WDChangePwdViewModel> getViewModelClass() {
        return WDChangePwdViewModel.class;
    }

    @OnClick(R.id.nav_back)
    protected void backBtnClick() {
        super.onBackPressed();
    }

    public static class WDChangePwdViewModel extends BaseViewModel {

        public WDChangePwdViewModel(@NonNull Application application) {
            super(application);
        }
    }
}
